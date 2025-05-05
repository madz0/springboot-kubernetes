package com.github.madz0;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

@SpringBootApplication
public class App {

  private final ConfigurableEnvironment environment;

  public App(ConfigurableEnvironment environment) {
    this.environment = environment;
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @EventListener({ApplicationReadyEvent.class})
  public void runner() {
    System.out.println(
        "Application started successfully! message: " + environment.getProperty("message"));
  }

  @Bean
  @ConditionalOnCloudPlatform(CloudPlatform.KUBERNETES)
  ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener() {
    return event -> {
      System.out.println(
          "Application is running on kubernetes!");
      StringBuilder result = new StringBuilder();
      for (PropertySource<?> propertySource : environment.getPropertySources()) {
        result.append("Property Source: ").append(propertySource.getName()).append("\n");
        if (propertySource.getName().contains("configmap") && propertySource instanceof MapPropertySource) {
          Object message = ((MapPropertySource) propertySource).getProperty("message");
          result.append("Contains message: ").append(message != null ? message : "null").append("\n");
        }
      }
      System.out.println("Property Sources:\n" + result);

    };
  }
}
