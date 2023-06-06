package com.gradlejustin.apidemo;

import org.json.JSONObject;

public class BuildScanModel {
    String projectName;
    String buildScanId;
    String buildTool;
    String buildDuration;
    String buildStartTime;

    public BuildScanModel(String buildScanId, String buildTool, JSONObject jsonBuildScanData) {

        this.buildScanId = buildScanId;
        this.buildTool = buildTool;
        this.buildDuration = Integer.toString(jsonBuildScanData.getInt("buildDuration"));
        this.buildStartTime = Long.toString(jsonBuildScanData.getLong("buildStartTime"));
        this.projectName = jsonBuildScanData
                .getString(this.buildTool.equals("gradle") ? "rootProjectName" : "topLevelProjectName");

    }

}
