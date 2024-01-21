package com.gradlelabs.dragonfly.apidemo;

import java.io.IOException;
import java.util.HashMap;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;

import org.json.*;

import static java.util.concurrent.TimeUnit.*;

public class BuildScanExtractor {

    static public final int SCRAPE_TIME = Integer.parseInt(System.getenv("DVSCRAPETIME"));
    static public Counter bDurationMetric = Counter.build()
            .name("build_duration_counter")
            .help("Duration of the build")
            .labelNames("project")
            .register();
    static public Counter bDNumberMetric = Counter.build()
            .name("build_duration_number_counter")
            .help("number of the build")
            .labelNames("project")
            .register();
    static public Gauge buildDurationMetric = Gauge.build()
            .name("build_duration")
            .help("Duration of the build")
            .labelNames("project")
            .register();

    public static void main(String[] var0) throws Exception, IOException {

        // Prometheus metrics point
        HTTPServer server = new HTTPServer(8081);

        // scrape DV API periodically
        new GrabMetrics().repeat();
    }

    public HashMap<String, BuildScanModel> discoverBuilds(int timeSinceSeconds) throws Exception {

        String instantString = Long.toString(Instant.now().minus(Duration.ofSeconds(timeSinceSeconds)).toEpochMilli());
        String discoveryUrl = BuildScanServiceConfig.geApiUrl + "?fromInstant=" + instantString;
        String builds = HttpUtils.procUrlRequest(discoveryUrl);
        System.out.println("******** builds ==== ***********=" + builds);
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

class GrabMetrics {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void repeat() {
        final Runnable logger = new Runnable() {
            @Override
            public void run() {
                HashMap<String, BuildScanModel> buildScanMetrics = null;
                try {
                    buildScanMetrics = new BuildScanExtractor().discoverBuilds(BuildScanExtractor.SCRAPE_TIME * 60);
                    new PrometheusUtils().postMetrics(buildScanMetrics);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        };
        final ScheduledFuture loggerHandle =
                scheduler.scheduleAtFixedRate(logger, 0, BuildScanExtractor.SCRAPE_TIME, MINUTES);
        //In case you want to kill this after some time like 24 hours
//      scheduler.schedule(new Runnable() {
//         public void run() { loggerHandle.cancel(true); }
//      }, 24, HOURS );
    }
}
