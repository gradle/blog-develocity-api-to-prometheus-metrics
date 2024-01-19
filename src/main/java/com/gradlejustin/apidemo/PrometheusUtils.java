package com.gradlejustin.apidemo;

import java.io.IOException;
import java.util.HashMap;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;

public class PrometheusUtils {

    public void pushMetrics(HashMap<String, BuildScanModel> buildScanData) throws IOException {

        // TODO: Replace the following value with your Prometheus PushGateway URL
//        final String PROMETHEUSURL = "192.168.66.2:9091";
        final String PROMETHEUSURL = "ec2-52-15-178-164.us-east-2.compute.amazonaws.com:9091";
        String prometheusGatewayUrl = PROMETHEUSURL;

        // Traverse the Builds
        int i = 0;
        CollectorRegistry registry = new CollectorRegistry();
        Gauge buildDurationMetric = Gauge.build()
                .name("build_duration")
                .help("Duration of the build")
                .labelNames("project")
                .register(registry);
        for (BuildScanModel bsm : buildScanData.values()) {

//            CollectorRegistry registry = new CollectorRegistry();
            String buildId = buildScanData.keySet().toArray()[i++].toString();
            String label = bsm.projectName.replace("-", "_").replace(" ", "_").replace(":", "");

            System.out.println("&&&&&&&&&&&&& label ="+label);
            // Create a gauge metric
//            Gauge buildDurationMetric = Gauge.build()
//                    .name("build_duration_")
//                    .help("Duration of the build")
//                    .labelNames("project")
//                    .register(registry);

            // Set the gauge value
            buildDurationMetric.labels(label ).set(Double.parseDouble(bsm.buildDuration));

            // Push metrics to Prometheus
            System.out.println("Pushing metrics for build " + buildId + " of " + label + " : Duration: "
                    + bsm.buildDuration);

            PushGateway pushGateway = new PushGateway(prometheusGatewayUrl);
            pushGateway.pushAdd(registry, "build_scans");

        }
    }

}
