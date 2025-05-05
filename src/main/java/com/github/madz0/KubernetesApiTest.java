package com.github.madz0;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class KubernetesApiTest {

    @PostConstruct
    public void testKubernetesApi() {
        try {
            ApiClient client = Config.defaultClient();
            Configuration.setDefaultApiClient(client);
            CoreV1Api api = new CoreV1Api();
            api.listNamespacedConfigMap(
                "default", // namespace
                null, // pretty
                null, // allowWatchBookmarks
                null, // continue
                null, // fieldSelector
                null, // labelSelector
                null, // limit
                null, // resourceVersion
                null, // resourceVersionMatch
                null, // timeoutSeconds
                null, // watch
                null  // async
            ).getItems().forEach(configMap -> System.out.println("Found ConfigMap: " + configMap.getMetadata().getName()));
        } catch (ApiException e) {
            System.err.println("Kubernetes API error: " + e.getMessage() + ", Code: " + e.getCode());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}