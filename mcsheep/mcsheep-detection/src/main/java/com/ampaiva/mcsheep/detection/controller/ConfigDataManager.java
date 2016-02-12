package com.ampaiva.mcsheep.detection.controller;

import com.ampaiva.mcsheep.detection.util.Config;

public class ConfigDataManager extends DataManager {
    private final Config config;

    public ConfigDataManager(Config config) {
        super(config.get("pu.name"));
        this.config = config;
        put("javax.persistence.jdbc.url");
        put("javax.persistence.jdbc.user");
        put("javax.persistence.jdbc.password");
        put("javax.persistence.jdbc.driver");
        put("eclipselink.logging.level");
    }

    private void put(String key) {
        getProperties().put(key, config.get(key));
    }
}
