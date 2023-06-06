package com.gradlejustin.apidemo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpUtils {

   public static String procUrlRequest(String baseUrl) throws Exception {

      URI targetURI = new URI(baseUrl);
      HttpRequest httpRequest = HttpRequest.newBuilder()
            .uri(targetURI)
            .setHeader("Authorization", "Bearer " + BuildScanServiceConfig.token)
            .GET()
            .build();

      HttpClient httpClient = HttpClient.newHttpClient();
      HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

      return response.body();

   }

}
