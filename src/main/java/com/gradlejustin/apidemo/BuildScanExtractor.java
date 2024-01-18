package com.gradlejustin.apidemo;

import java.util.HashMap;
import java.time.Duration;
import java.time.Instant;

import org.json.*;

public class BuildScanExtractor {

   public static void main(String[] var0) throws Exception {

      HashMap<String, BuildScanModel> buildScanMetrics = new BuildScanExtractor().discoverBuilds(11900);
      new PrometheusUtils().pushMetrics(buildScanMetrics);

   }

   public HashMap<String, BuildScanModel> discoverBuilds(int timeSinceSeconds) throws Exception {

      String instantString = Long.toString(Instant.now().minus(Duration.ofSeconds(timeSinceSeconds)).toEpochMilli());
      String discoveryUrl = BuildScanServiceConfig.geApiUrl + "?fromInstant=" + instantString;
      String builds = HttpUtils.procUrlRequest(discoveryUrl);
//      System.out.println ("******** builds ==== ***********=" + builds );
      HashMap<String, BuildScanModel> buildScanMetrics = new HashMap<String, BuildScanModel>();

      JSONArray jsonBuilds = new JSONArray(builds);

      for (int i = 0; i < jsonBuilds.length(); i++) {
         String buildScanId = jsonBuilds.getJSONObject(i).getString("id");
         String buildTool = jsonBuilds.getJSONObject(i).getString("buildToolType");
         buildScanMetrics.put(buildScanId, extractBuildScanDetails(buildScanId, buildTool));
      }

      return buildScanMetrics;

   }

   public BuildScanModel extractBuildScanDetails(String buildScanId, String buildTool) throws Exception {

      String buildScanApiUrl = BuildScanServiceConfig.geApiUrl + buildScanId + "/";

      String tempUrl = buildScanApiUrl + buildTool + "-attributes";
      String tempBuildScanData = HttpUtils.procUrlRequest(tempUrl);
      JSONObject jsonBuildScanData = new JSONObject(tempBuildScanData);
      BuildScanModel myBs = new BuildScanModel(buildScanId, buildTool, jsonBuildScanData);

      System.out.println("Project Name: " + myBs.projectName +
            "\tBuild Scan ID: " + myBs.buildScanId +
            "\tBuild Start Time: " + myBs.buildStartTime +
            "\tBuild Duration: " + myBs.buildDuration);
      return myBs;

   }

}
