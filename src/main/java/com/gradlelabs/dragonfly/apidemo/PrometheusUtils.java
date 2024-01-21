package com.gradlelabs.dragonfly.apidemo;

import java.io.IOException;
import java.util.HashMap;

public class PrometheusUtils {

    public void postMetrics(HashMap<String, BuildScanModel> buildScanData) throws IOException {

        // Traverse the Builds
        int i = 0;
        for (BuildScanModel bsm : buildScanData.values()) {

            String buildId = buildScanData.keySet().toArray()[i++].toString();
            String labelProject = bsm.projectName.replace("-", "_").replace(" ", "_").replace(":", "");

            // Set the metrics value
            BuildScanExtractor.buildDurationMetric.labels(labelProject, bsm.localCache, bsm.remoteCache ).set(Double.parseDouble(bsm.buildDuration));
            BuildScanExtractor.bDurationMetric.labels(labelProject, bsm.localCache, bsm.remoteCache).inc(Double.parseDouble(bsm.buildDuration));
            BuildScanExtractor.bDNumberMetric.labels(labelProject, bsm.localCache, bsm.remoteCache).inc(1);

            // feedbacks
            System.out.println("Pushing metrics for build " + buildId + " of " + labelProject + " : Duration: "
                    + bsm.buildDuration + " : local cache isEnabled: " + bsm.localCache + " : remote cache isEnabled: " + bsm.remoteCache);

        }
    }

}
