package org.igot.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PropertiesCache {
    @Autowired
    private Environment environment;
    public final Map<String, Float> attributePercentageMap = new ConcurrentHashMap<>();
    private final String[] fileName = {
            "cassandra.config.properties",
            "cassandratablecolumn.properties",
            "application.properties",
            "customerror.properties"
    };
    private final Properties configProp = new Properties();

    @PostConstruct
    public void init() {
        for (String file : fileName) {
            try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(file)) {
                if (in != null) {
                    configProp.load(in);
                } else {
                    log.warn("Property file not found: {}", file);
                }
            } catch (IOException e) {
                log.error("Failed to load property file: {}", file, e);
            }
        }
    }

    public String getProperty(String key) {
        String value = environment.getProperty(key);
        if (StringUtils.hasLength(value))
            return value;
        return configProp.getProperty(key) != null ? configProp.getProperty(key) : key;
    }
}
