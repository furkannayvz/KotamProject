package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.configuration.DataSourceConfig;
import com.i2i.intern.kotam.aom.model.PackageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class  PackageRepository implements PackageRepositoryOracle {

    private final DataSource dataSource;

    /*
    public PackageRepository() {
        // Manuel olarak kendi DataSource'unu oluştur
        this.dataSource = DataSourceConfig.customDataSource();
    }*/

    @Autowired
    public PackageRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<PackageEntity> getAllPackages() {
        List<PackageEntity> packages = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall("{call SELECT_ALL_PACKAGES(?)}")) {

            System.out.println("Basladim");

            stmt.registerOutParameter(1, Types.REF_CURSOR);
            stmt.execute();

            System.out.println("bitirdim");

            ResultSet rs = (ResultSet) stmt.getObject(1);

            while (rs.next()) {
                PackageEntity pkg = new PackageEntity();
                pkg.setId(rs.getLong("PACKAGE_ID"));
                pkg.setName(rs.getString("PACKAGE_NAME"));
                pkg.setPrice(rs.getDouble("PRICE"));
                pkg.setMinutesQuota(rs.getLong("AMOUNT_MINUTES"));
                pkg.setDataQuota(rs.getLong("AMOUNT_DATA"));
                pkg.setSmsQuota(rs.getLong("AMOUNT_SMS"));
                pkg.setPeriod(rs.getInt("PERIOD")); // varsa bu metodun modelde olduğuna emin ol

                packages.add(pkg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return packages;
    }

    public Optional<PackageEntity> getPackageDetailsById(Long id) {
        try (Connection conn = dataSource.getConnection()) {
            CallableStatement stmt = conn.prepareCall("{call SELECT_PACKAGE_DETAILS_BY_ID(?, ?, ?, ?, ?)}");

            System.out.println("Basladim");

            stmt.setLong(1, id);
            stmt.registerOutParameter(2, Types.BIGINT);
            stmt.registerOutParameter(3, Types.BIGINT);
            stmt.registerOutParameter(4, Types.BIGINT);
            stmt.registerOutParameter(5, Types.INTEGER);

            stmt.execute();

            System.out.println("bitirdim");

            PackageEntity pkg = new PackageEntity();
            pkg.setId(id);
            pkg.setMinutesQuota(stmt.getLong(2));
            pkg.setSmsQuota(stmt.getLong(3));
            pkg.setDataQuota(stmt.getLong(4));
            pkg.setPeriod(stmt.getInt(5));

            return Optional.of(pkg);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }


    public Optional<PackageEntity> getPackageDetailsByIdCursor(Long id) {
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall("{call SELECT_PACKAGE_DETAILS_BY_ID_CURSOR(?, ?)}")) {

            System.out.println("Basladim");

            stmt.setLong(1, id);
            stmt.registerOutParameter(2, Types.REF_CURSOR);
            stmt.execute();

            System.out.println("bitirdim");

            ResultSet rs = (ResultSet) stmt.getObject(2);
            if (rs.next()) {
                PackageEntity pkg = new PackageEntity();
                pkg.setDataQuota(rs.getLong("AMOUNT_DATA"));
                pkg.setSmsQuota(rs.getLong("AMOUNT_SMS"));
                pkg.setMinutesQuota(rs.getLong("AMOUNT_MINUTES"));
                pkg.setPeriod(rs.getInt("PERIOD")); // eğer setPeriod eklendiyse
                return Optional.of(pkg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<PackageEntity> getPackageDetailsByName(String name) {
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall("{call SELECT_PACKAGE_DETAILS_BY_NAME(?, ?, ?, ?)}")) {

            System.out.println("Basladim");

            stmt.setString(1, name);
            stmt.registerOutParameter(2, Types.NUMERIC); // amount_minutes
            stmt.registerOutParameter(3, Types.NUMERIC); // amount_sms
            stmt.registerOutParameter(4, Types.NUMERIC); // amount_data

            stmt.execute();
            System.out.println("bitirdim");


            PackageEntity pkg = new PackageEntity();
            pkg.setName(name);
            pkg.setMinutesQuota(stmt.getLong(2));
            pkg.setSmsQuota(stmt.getLong(3));
            pkg.setDataQuota(stmt.getLong(4));

            return Optional.of(pkg);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<Long> getPackageIdByName(String name) {
        try (Connection conn = dataSource.getConnection();
             CallableStatement stmt = conn.prepareCall("{call SELECT_PACKAGE_ID(?, ?)}")) {

            System.out.println("Basladim");

            stmt.setString(1, name);
            stmt.registerOutParameter(2, Types.NUMERIC);
            stmt.execute();

            System.out.println("bitirdim");

            return Optional.of(stmt.getLong(2));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
