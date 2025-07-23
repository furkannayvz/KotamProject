/*
package com.i2i.intern.kotam.aom.repository;

import com.i2i.intern.kotam.aom.model.PackageEntity;
import com.i2i.intern.kotam.aom.service.VoltDBManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("packageRepositoryVoltdb")
public class PackageRepositoryVoltdb implements PackageRepositoryVoltdbBase {

    private final VoltDBManager voltDBManager;

    @Autowired
    public PackageRepositoryVoltdb(VoltDBManager voltDBManager) {
        this.voltDBManager = voltDBManager;
    }

    @Override
    public Optional<String> getPackageNameByMsisdn(String msisdn) {
        try {
            JSONObject response = voltDBManager.executeProcedure("GET_PACKAGE_NAME_BY_MSISDN", msisdn);
            if (response.has("status") && response.getInt("status") == 1) {
                JSONArray results = response.getJSONArray("results");
                if (results.length() > 0) {
                    String packageName = results.getJSONObject(0)
                            .getJSONArray("data")
                            .getJSONArray(0)
                            .getString(0);
                    return Optional.of(packageName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getPackageNameByPackageId(Long packageId) {
        try {
            JSONObject response = voltDBManager.executeProcedure("GET_PACKAGE_NAME_BY_PACKAGE_ID", packageId);
            if (response.has("status") && response.getInt("status") == 1) {
                JSONArray results = response.getJSONArray("results");
                if (results.length() > 0) {
                    String packageName = results.getJSONObject(0)
                            .getJSONArray("data")
                            .getJSONArray(0)
                            .getString(0);
                    return Optional.of(packageName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<PackageEntity> getPackageInfoByPackageId(Long packageId) {
        try {
            JSONObject response = voltDBManager.executeProcedure("GET_PACKAGE_INFO_BY_PACKAGE_ID", packageId);
            if (response.has("status") && response.getInt("status") == 1) {
                JSONArray results = response.getJSONArray("results");
                if (results.length() > 0) {
                    JSONArray row = results.getJSONObject(0).getJSONArray("data").getJSONArray(0);
                    PackageEntity entity = parseRowToPackage(row);
                    return Optional.of(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<PackageEntity> getPackageInfoByMsisdn(String msisdn) {
        try {
            JSONObject response = voltDBManager.executeProcedure("GET_PACKAGE_INFO_BY_MSISDN", msisdn);
            if (response.has("status") && response.getInt("status") == 1) {
                JSONArray results = response.getJSONArray("results");
                if (results.length() > 0) {
                    JSONArray row = results.getJSONObject(0).getJSONArray("data").getJSONArray(0);
                    PackageEntity entity = parseRowToPackage(row);
                    return Optional.of(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    private PackageEntity parseRowToPackage(JSONArray row) {
        PackageEntity entity = new PackageEntity();
        entity.setId(row.getLong(0));
        entity.setPackageName(row.getString(1));

        if (row.length() > 2 && !row.isNull(2)) entity.setMinutesQuota(row.getLong(2));
        if (row.length() > 3 && !row.isNull(3)) entity.setSmsQuota(row.getLong(3));
        if (row.length() > 4 && !row.isNull(4)) entity.setDataQuota(row.getLong(4));
        if (row.length() > 5 && !row.isNull(5)) entity.setPrice(row.getDouble(5));
        if (row.length() > 6 && !row.isNull(6)) entity.setIsActive(row.getBoolean(6));
        if (row.length() > 7 && !row.isNull(7)) entity.setPeriod(row.getInt(7));

        return entity;
    }
}

*/
