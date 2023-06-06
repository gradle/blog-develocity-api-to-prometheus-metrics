package com.gradlejustin.apidemo;

import java.io.IOException;
import java.util.HashMap;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;

public class PrometheusUtils {

    public void pushMetrics(HashMap<String, BuildScanModel> buildScanData) throws IOException {

        // TODO: Replace the following value with your Prometheus PushGateway URL
        final String PROMETHEUSURL = "localhost:9091";
        String prometheusGatewayUrl = PROMETHEUSURL;

        // Traverse the Builds
        int i = 0;
        for (BuildScanModel bsm : buildScanData.values()) {

            CollectorRegistry registry = new CollectorRegistry();
            String buildId = buildScanData.keySet().toArray()[i++].toString();
            // Create a gauge metric
            Gauge buildDurationMetric = Gauge.build()
                    .name("build_duration_" + bsm.projectName.replace("-", "_"))
                    .help("Duration of the build")
                    .register(registry);

            // Set the gauge value
            buildDurationMetric.set(Double.parseDouble(bsm.buildDuration));

            // Push metrics to Prometheus
            System.out.println("Pushing metrics for build " + buildId + " of " + bsm.projectName + " : Duration: "
                    + bsm.buildDuration);

            PushGateway pushGateway = new PushGateway(prometheusGatewayUrl);
            pushGateway.pushAdd(registry, "build_scans");

        }
    }

}
