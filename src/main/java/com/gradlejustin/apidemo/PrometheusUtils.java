package com.gradlejustin.apidemo;

import java.io.IOException;
import java.util.HashMap;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.Counter;
import io.prometheus.client.exporter.PushGateway;

public class PrometheusUtils {

    public void pushMetrics(HashMap<String, BuildScanModel> buildScanData) throws IOException {

        // TODO: Replace the following value with your Prometheus PushGateway URL
//        final String PROMETHEUSURL = "192.168.66.2:9091";
//        String prometheusGatewayUrl = PROMETHEUSURL;

        // Traverse the Builds
        int i = 0;
        for (BuildScanModel bsm : buildScanData.values()) {

            String buildId = buildScanData.keySet().toArray()[i++].toString();
            String label = bsm.projectName.replace("-", "_").replace(" ", "_").replace(":", "");

//            System.out.println("&&&&&&&&&&&&& label ="+label);

            // Set the metrics value
            BuildScanExtractor.buildDurationMetric.labels(label ).set(Double.parseDouble(bsm.buildDuration));
            BuildScanExtractor.bDurationMetric.labels(label ).inc(Double.parseDouble(bsm.buildDuration));
            BuildScanExtractor.bDNumberMetric.labels(label ).inc(1);

            // Push metrics to Prometheus
            System.out.println("Pushing metrics for build " + buildId + " of " + label + " : Duration: "
                    + bsm.buildDuration);

//            PushGateway pushGateway = new PushGateway(prometheusGatewayUrl);
//            pushGateway.pushAdd(registry, "build_scans");

        }
    }

}
