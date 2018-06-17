package org.sapl.commons.dao;

import org.sapl.commons.util.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.HashMap;
import java.util.Map;


public interface ConfigMongoRepository extends MongoRepository<ConfigMongoRepository.ConfigProperty, String> {

    ConfigProperty getByName(String name);

    default String get(String name) {
        ConfigProperty p = getByName(name);
        return p != null ? p.value : null;
    }

    default String getDecoded(String name, String secret) {
        try {
            String encoded = get(name);
            if (encoded != null) return StringUtils.decryptDES(encoded, secret);
        } catch (Exception e) {
            //
        }
        return null;
    }

    default Map getAll() {
        Map<String, String> map = new HashMap<>();
        findAll().stream().forEach(item -> map.put(item.name, item.value));
        return map;
    }

    default void put(String name, String value) {
        ConfigProperty configProperty = getByName(name);
        if (configProperty == null) configProperty = new ConfigProperty(name);
        configProperty.value = value;
        save(configProperty);
    }

    default void putEncoded(String name, String value, String secret) {
        try {
            String encoded = StringUtils.encryptDES(value, secret);
            put(name, encoded);
        } catch (Exception e) {
            //
        }
    }

    @Document(collection = "config")
    class ConfigProperty {

        @Id String id;
        public String name;
        @SuppressWarnings("WeakerAccess") public String value;

        public ConfigProperty() {
        }

        ConfigProperty(String name) {
            this.name = name;
        }
    }
}
