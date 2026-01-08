package com.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// Clase de configuración auxiliar.
public class ApiConfig {
    // Eliminada la constante BASE_URL porque no se usaba y puede causar confusión

    public static String baseUrl() {
        String fromSystem = System.getProperty("base.url");
        if (fromSystem != null && !fromSystem.trim().isEmpty()) {
            return fromSystem;
        }
        // Intentar leer serenity.properties desde el classpath
        Properties props = new Properties();
        try (InputStream is = ApiConfig.class.getClassLoader().getResourceAsStream("serenity.properties")) {
            if (is != null) {
                props.load(is);
                String fromProps = props.getProperty("base.url");
                if (fromProps != null && !fromProps.trim().isEmpty()) {
                    return fromProps;
                }
            }
        } catch (IOException e) {
            // ignorar y usar fallback
        }
        // Valor por defecto
        return "https://petstore.swagger.io/v2";
    }
}
