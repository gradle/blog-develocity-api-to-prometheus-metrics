# DEVelocity-api-to-prometheus-grafana  

Sample code as a companion to Visualizing Gradle Enterprise Data with Prometheus and Grafana [Blog](https://gradle.com/blog/visualizing-gradle-enterprise-data-with-prometheus-and-grafana-gradle-enterprise-api-in-action/).

## Major refactor is done
Instead of using Prometheus PushGateway as described by the blog, the client server is used to easily accommodate more metrics types

## Build with:

```./gradlew clean build```

## Run with:

Set up the following 3 environment variables

```
export DVTOKEN=token
export DVURL=develocity-field.gradle.com
export DVSCRAPETIME=5

nohup java -jar ./build/libs/BuildScanExtractor-all.jar  &

```

The distribution in ./build/distributions/

Or you can just run from IDE (be sure to set up the env variables).

### The client metrics server is listening at port 8081.

## Sample Grafana UI

There are three metrics created:
1. Counter: Build Duration
2. Gauge: Build Duration for the last build
3. Counter: Number of Builds


<p align="center"><img src="/images/dv-api-metrics.png" width="100%" alt="Build Metrics by DEVelocity API" /></p>

## Sample Alerts to Slack

<p align="center"><img src="/images/alerts-to-slack.png" width="100%" alt="Alerts by DEVelocity API" /></p>