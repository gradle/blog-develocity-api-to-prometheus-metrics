package com.gradlelabs.dragonfly.apidemo;

import java.io.IOException;
import java.util.HashMap;

public class PrometheusUtils {

    public void postMetrics(HashMap<String, BuildScanModel> buildScanData) throws IOException {

        // Traverse the Builds
        int i = 0;
        for (BuildScanModel bsm : buildScanData.values()) {

            String buildId = buildScanData.keySet().toArray()[i++].toString();
            String label = bsm.projectName.replace("-", "_").replace(" ", "_").replace(":", "");

            // Set the metrics value
            BuildScanExtractor.buildDurationMetric.labels(label).set(Double.parseDouble(bsm.buildDuration));
            BuildScanExtractor.bDurationMetric.labels(label).inc(Double.parseDouble(bsm.buildDuration));
            BuildScanExtractor.bDNumberMetric.labels(label).inc(1);

            // feedbacks
            System.out.println("Pushing metrics for build " + buildId + " of " + label + " : Duration: "
                    + bsm.buildDuration);

        }
    }

}
