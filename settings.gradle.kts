plugins {
    id("com.gradle.enterprise") version "3.15"
    id("com.gradle.common-custom-user-data-gradle-plugin") version "1.11.2"

    id("org.gradle.toolchains.foojay-resolver-convention") version("0.7.0")
}

gradleEnterprise {
    server = "https://develocity-field.gradle.com"
//    server = "https://ec2-18-117-85-150.us-east-2.compute.amazonaws.com"
//    allowUntrustedServer = true
    buildScan {
        publishAlways()
        isUploadInBackground = System.getenv("CI") == null
        tag("DEVelocity-Metrics-svc")
        link("Blog","https://gradle.com/blog/visualizing-gradle-enterprise-data-with-prometheus-and-grafana-gradle-enterprise-api-in-action/")
        capture {
            isTaskInputFiles = true
        }

        obfuscation {
            ipAddresses { addresses -> addresses.map { _ -> "0.0.0.0" } }
        }

//        buildFinished {
//            value("Disk usage (output dir)", buildDir.walkTopDown().map { it.length() }.sum().toString())
//        }



    }
//    projectId = "fzhu-onboarding"
}

buildCache {
    local { isEnabled = true }
    remote(gradleEnterprise.buildCache) {
        isPush = java.lang.System.getenv("CI") != null
        //isPush = true
        isEnabled = true
//        path = "/cache/exp5-oct-6-2023-take1"
    }
}

rootProject.name = "DEVelocity-Metrics-svc"