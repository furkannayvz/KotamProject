package com.i2i.intern.kotam.aom.model;


import com.i2i.intern.kotam.aom.enums.TokenType;
import lombok.Builder;
import java.util.Date;

@Builder
public class Token {
    private Integer tokenId;
    private Integer userId;
    private String token;
    private TokenType tokenType;
    private boolean expired;
    private boolean revoked;
    private Date createdDate;
    private Date updatedDate;

    // Parametreli constructor
    public Token(Integer tokenId, Integer userId, String token, TokenType tokenType,
                 boolean expired, boolean revoked, Date createdDate, Date updatedDate) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.token = token;
        this.tokenType = tokenType;
        this.expired = expired;
        this.revoked = revoked;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    // Boş constructor
    public Token() {}

    // Getter ve Setter metodları
    public Integer getTokenId() { return tokenId; }
    public void setTokenId(Integer tokenId) { this.tokenId = tokenId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public TokenType getTokenType() { return tokenType; }
    public void setTokenType(TokenType tokenType) { this.tokenType = tokenType; }

    public boolean isExpired() { return expired; }
    public void setExpired(boolean expired) { this.expired = expired; }

    public boolean isRevoked() { return revoked; }
    public void setRevoked(boolean revoked) { this.revoked = revoked; }

    public Date getCreatedDate() { return createdDate; }
    public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

    public Date getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(Date updatedDate) { this.updatedDate = updatedDate; }

    @Override
    public String toString() {
        return "Token{" +
                "tokenId=" + tokenId +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", tokenType=" + tokenType +
                ", expired=" + expired +
                ", revoked=" + revoked +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
