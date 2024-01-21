package com.gradlelabs.dragonfly.apidemo;

public class BuildScanServiceConfig {

    final static String token = System.getenv("DVTOKEN");
    final static String geApiUrl = "https://" + System.getenv("DVURL") + "/api/builds/";

}
