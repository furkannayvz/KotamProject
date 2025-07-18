package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.exception.DataNotFoundException;
import com.i2i.intern.kotam.aom.helper.DatabaseConnectionManager;
import com.i2i.intern.kotam.aom.model.Token;
import com.i2i.intern.kotam.aom.enums.TokenType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TokenRepository {

    private final DatabaseConnectionManager dbManager;
    private final Logger logger = LoggerFactory.getLogger(TokenRepository.class);

    public TokenRepository(DatabaseConnectionManager dbManager) {
        this.dbManager = dbManager;
    }

    // Kullanıcının geçerli tokenlarını bulma (farklı metod ismi)
    public List<Token> retrieveValidTokensForUser(Integer userId) throws SQLException, ClassNotFoundException {
        logger.info("Retrieving valid tokens for user ID: {}", userId);

        Connection connection = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        List<Token> validTokens = new ArrayList<>();

        try {
            connection = dbManager.getOracleConnection();

            // Farklı stored procedure ismi ve yapı
            stmt = buildValidTokensQuery(connection, userId);
            rs = executeValidTokensQuery(stmt);

            validTokens = buildTokenListFromResultSet(rs);

            logger.info("Retrieved {} valid tokens for user: {}", validTokens.size(), userId);
            return validTokens;

        } catch (Exception e) {
            logger.error("Error retrieving valid tokens for user: {}", userId, e);
            throw new RuntimeException("Failed to retrieve valid tokens: " + e.getMessage());
        } finally {
            cleanupResources(rs, stmt, connection);
        }
    }

    // Token değerine göre token bulma (farklı metod ismi)
    public Optional<Token> searchTokenByValue(String tokenValue) {
        logger.info("Searching token by value: {}", maskToken(tokenValue));

        Connection connection = null;
        CallableStatement stmt = null;
        ResultSet rs = null;

        try {
            connection = dbManager.getOracleConnection();

            // Farklı stored procedure ismi ve yapı
            stmt = buildTokenSearchQuery(connection, tokenValue);
            rs = executeTokenSearchQuery(stmt);

            Optional<Token> token = buildTokenFromResultSet(rs);

            if (token.isPresent()) {
                logger.info("Token found successfully");
            } else {
                logger.warn("Token not found with provided value");
            }

            return token;

        } catch (Exception e) {
            logger.error("Error searching token by value", e);
            return Optional.empty();
        } finally {
            cleanupResources(rs, stmt, connection);
        }
    }

    // Token ekleme (farklı metod ismi)
    public void saveToken(Token token) {
        logger.info("Saving new token for user: {}", token.getUserId());

        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = dbManager.getOracleConnection();

            // Farklı stored procedure ismi ve yapı
            stmt = buildTokenInsertQuery(connection, token);
            executeTokenInsertQuery(stmt);

            logger.info("Token saved successfully for user: {}", token.getUserId());

        } catch (Exception e) {
            logger.error("Error saving token for user: {}", token.getUserId(), e);
            throw new RuntimeException("Failed to save token: " + e.getMessage());
        } finally {
            cleanupResources(null, stmt, connection);
        }
    }

    // Token durumu güncelleme (farklı metod ismi)
    public void modifyTokenStatus(Token token) {
        logger.info("Modifying token status for token ID: {}", token.getTokenId());

        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = dbManager.getOracleConnection();

            // Farklı stored procedure ismi ve yapı
            stmt = buildTokenUpdateQuery(connection, token);
            executeTokenUpdateQuery(stmt);

            logger.info("Token status modified successfully for token ID: {}", token.getTokenId());

        } catch (Exception e) {
            logger.error("Error modifying token status for token ID: {}", token.getTokenId(), e);
            throw new RuntimeException("Failed to modify token status: " + e.getMessage());
        } finally {
            cleanupResources(null, stmt, connection);
        }
    }

    // Token iptal etme (ek özellik)
    public void revokeUserTokens(Integer userId) {
        logger.info("Revoking all tokens for user: {}", userId);

        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = dbManager.getOracleConnection();

            // Tüm kullanıcı tokenlarını iptal et
            stmt = buildTokenRevokeQuery(connection, userId);
            executeTokenRevokeQuery(stmt);

            logger.info("All tokens revoked successfully for user: {}", userId);

        } catch (Exception e) {
            logger.error("Error revoking tokens for user: {}", userId, e);
            throw new RuntimeException("Failed to revoke tokens: " + e.getMessage());
        } finally {
            cleanupResources(null, stmt, connection);
        }
    }

    // Token geçerlilik kontrolü (ek özellik)
    public boolean validateTokenStatus(String tokenValue) {
        logger.debug("Validating token status");

        Optional<Token> token = searchTokenByValue(tokenValue);

        if (token.isPresent()) {
            Token t = token.get();
            boolean isValid = !t.isExpired() && !t.isRevoked();
            logger.debug("Token validation result: {}", isValid);
            return isValid;
        }

        logger.debug("Token not found, validation failed");
        return false;
    }

    // Token temizleme (ek özellik)
    public void cleanupExpiredTokens() {
        logger.info("Cleaning up expired tokens");

        Connection connection = null;
        CallableStatement stmt = null;

        try {
            connection = dbManager.getOracleConnection();

            stmt = buildTokenCleanupQuery(connection);
            executeTokenCleanupQuery(stmt);

            logger.info("Expired tokens cleaned up successfully");

        } catch (Exception e) {
            logger.error("Error cleaning up expired tokens", e);
            throw new RuntimeException("Failed to cleanup expired tokens: " + e.getMessage());
        } finally {
            cleanupResources(null, stmt, connection);
        }
    }

    // Private helper methods - farklı isimler ve yapılar
    private CallableStatement buildValidTokensQuery(Connection connection, Integer userId) throws SQLException {
        logger.debug("Building valid tokens query for user: {}", userId);

        CallableStatement stmt = connection.prepareCall("{call GET_VALID_USER_TOKENS(?, ?)}");
        stmt.setInt(1, userId);
        stmt.registerOutParameter(2, Types.REF_CURSOR);

        return stmt;
    }

    private ResultSet executeValidTokensQuery(CallableStatement stmt) throws SQLException {
        logger.debug("Executing valid tokens query");
        stmt.execute();
        return (ResultSet) stmt.getObject(2);
    }

    private List<Token> buildTokenListFromResultSet(ResultSet rs) throws SQLException {
        logger.debug("Building token list from result set");

        List<Token> tokens = new ArrayList<>();

        while (rs.next()) {
            Token token = constructTokenFromRow(rs);
            tokens.add(token);
        }

        return tokens;
    }

    private CallableStatement buildTokenSearchQuery(Connection connection, String tokenValue) throws SQLException {
        logger.debug("Building token search query");

        CallableStatement stmt = connection.prepareCall("{call SEARCH_TOKEN_BY_VALUE(?, ?)}");
        stmt.setString(1, tokenValue);
        stmt.registerOutParameter(2, Types.REF_CURSOR);

        return stmt;
    }

    private ResultSet executeTokenSearchQuery(CallableStatement stmt) throws SQLException {
        logger.debug("Executing token search query");
        stmt.execute();
        return (ResultSet) stmt.getObject(2);
    }

    private Optional<Token> buildTokenFromResultSet(ResultSet rs) throws SQLException {
        logger.debug("Building token from result set");

        if (rs.next()) {
            Token token = constructTokenFromRow(rs);
            return Optional.of(token);
        }

        return Optional.empty();
    }

    private CallableStatement buildTokenInsertQuery(Connection connection, Token token) throws SQLException {
        logger.debug("Building token insert query");

        CallableStatement stmt = connection.prepareCall("{call SAVE_NEW_TOKEN(?, ?, ?, ?, ?)}");
        stmt.setString(1, token.getToken());
        stmt.setString(2, token.getTokenType().name());
        stmt.setString(3, convertBooleanToString(token.isExpired()));
        stmt.setString(4, convertBooleanToString(token.isRevoked()));
        stmt.setInt(5, token.getUserId());

        return stmt;
    }

    private void executeTokenInsertQuery(CallableStatement stmt) throws SQLException {
        logger.debug("Executing token insert query");
        stmt.execute();
    }

    private CallableStatement buildTokenUpdateQuery(Connection connection, Token token) throws SQLException {
        logger.debug("Building token update query");

        CallableStatement stmt = connection.prepareCall("{call MODIFY_TOKEN_STATUS(?, ?, ?)}");
        stmt.setInt(1, token.getTokenId());
        stmt.setString(2, convertBooleanToString(token.isExpired()));
        stmt.setString(3, convertBooleanToString(token.isRevoked()));

        return stmt;
    }

    private void executeTokenUpdateQuery(CallableStatement stmt) throws SQLException {
        logger.debug("Executing token update query");
        stmt.execute();
    }

    private CallableStatement buildTokenRevokeQuery(Connection connection, Integer userId) throws SQLException {
        logger.debug("Building token revoke query");

        CallableStatement stmt = connection.prepareCall("{call REVOKE_USER_TOKENS(?)}");
        stmt.setInt(1, userId);

        return stmt;
    }

    private void executeTokenRevokeQuery(CallableStatement stmt) throws SQLException {
        logger.debug("Executing token revoke query");
        stmt.execute();
    }

    private CallableStatement buildTokenCleanupQuery(Connection connection) throws SQLException {
        logger.debug("Building token cleanup query");

        CallableStatement stmt = connection.prepareCall("{call CLEANUP_EXPIRED_TOKENS()}");

        return stmt;
    }

    private void executeTokenCleanupQuery(CallableStatement stmt) throws SQLException {
        logger.debug("Executing token cleanup query");
        stmt.execute();
    }

    private Token constructTokenFromRow(ResultSet rs) throws SQLException {
        logger.debug("Constructing token from database row");

        return Token.builder()
                .tokenId(rs.getInt("TOKEN_ID"))
                .token(rs.getString("TOKEN_VALUE"))
                .tokenType(TokenType.valueOf(rs.getString("TOKEN_TYPE")))
                .expired(convertStringToBoolean(rs.getString("IS_EXPIRED")))
                .revoked(convertStringToBoolean(rs.getString("IS_REVOKED")))
                .userId(rs.getInt("USER_ID"))
                .build();
    }

    private String convertBooleanToString(boolean value) {
        return value ? "Y" : "N";
    }

    private boolean convertStringToBoolean(String value) {
        return "Y".equals(value);
    }

    private String maskToken(String token) {
        if (token == null || token.length() < 8) {
            return "***";
        }
        return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
    }

    private void cleanupResources(AutoCloseable... resources) {
        logger.debug("Cleaning up database resources");

        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    logger.warn("Error cleaning up resource", e);
                }
            }
        }
    }
}
