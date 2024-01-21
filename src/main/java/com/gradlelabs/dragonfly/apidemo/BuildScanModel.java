package com.gradlelabs.dragonfly.apidemo;

import org.json.JSONObject;

public class BuildScanModel {
    String projectName;
    String buildScanId;
    String buildTool;
    String buildDuration;
    String buildStartTime;

    String localCache;

    String remoteCache;

    public BuildScanModel(String buildScanId, String buildTool, JSONObject jsonBuildScanData, JSONObject jsonBuildCacheData) {

        this.buildScanId = buildScanId;
        this.buildTool = buildTool;
        this.buildDuration = Integer.toString(jsonBuildScanData.getInt("buildDuration"));
        this.buildStartTime = Long.toString(jsonBuildScanData.getLong("buildStartTime"));
        this.projectName = jsonBuildScanData
                .getString(this.buildTool.equals("gradle") ? "rootProjectName" : "topLevelProjectName");

        try{
            this.localCache = String.valueOf(jsonBuildCacheData.getJSONObject("buildCaches").getJSONObject("local").getBoolean("isEnabled"));
            this.remoteCache = String.valueOf(jsonBuildCacheData.getJSONObject("buildCaches").getJSONObject("remote").getBoolean("isEnabled"));
        } catch (Exception e){
            this.localCache = "false";
            this.remoteCache = "false";
        }
    }

}
