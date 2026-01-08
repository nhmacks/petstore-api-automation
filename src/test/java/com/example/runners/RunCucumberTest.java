package com.example.runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    plugin = {"pretty", "json:target/cucumber.json", "junit:target/cucumber.xml"},
    tags = "@api-test",
    glue = {"com.example.steps", "com.example.hooks"}
)
public class RunCucumberTest {
    // Runner para Cucumber + Serenity. Ejecútalo con `mvn clean verify`.
}
