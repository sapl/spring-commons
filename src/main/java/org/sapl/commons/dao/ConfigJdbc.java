package org.sapl.commons.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ConfigJdbc {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void init(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        try {
            jdbcTemplate.execute("" +
                    "CREATE TABLE IF NOT EXISTS config (" +
                    "id INT AUTO_INCREMENT," +
                    "name VARCHAR(120) NOT NULL," +
                    "value text, " +
                    "CONSTRAINT config_pkey PRIMARY KEY (id) " +
                    ");");

            jdbcTemplate.execute("CREATE UNIQUE INDEX config_udx ON config (name);");
        } catch (Exception e) {
            //
        }
    }

    public Map getAll() {
        TreeMap<String, String> map = new TreeMap<>();
        List<Map<String, Object>> rows = getJdbcTemplate().queryForList("SELECT * FROM config ORDER BY name");
        for (Map row : rows) map.put((String) row.get("name"), (String) row.get("value"));
        return map;
    }

    private JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public Date getDate(String name) {
        String value = get(name);
        return value != null ? (Date) Timestamp.valueOf(value) : null;
    }

    public boolean getBoolean(String name) {
        String value = get(name);
        return "true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value);
    }

    public String get(String name) {
        return get(name, null);
    }

    public String get(String name, String def) {
        try {
            return getJdbcTemplate().queryForObject("SELECT value FROM config WHERE name = ?",
                    String.class, name.trim());
        } catch (EmptyResultDataAccessException e) {
            return def;
        }
    }

    public String put(String name, Object value) {
        if (value == null) {
            delete(name);
        } else {
            if (getJdbcTemplate().update("UPDATE config SET value = ? WHERE  name = ? ", toString(value), name) == 0) {
                getJdbcTemplate().update("INSERT INTO config(name, value) VALUES(?,?)", name, toString(value));
            }
        }
        return get(name);
    }

    public void delete(String name) {
        if (name != null) getJdbcTemplate().update("DELETE FROM config WHERE name = ?", name.trim());
    }

    private String toString(Object o) {
        if (o instanceof Date) {
            return new Timestamp(((Date) o).getTime()).toString();
        }
        return o.toString().trim();
    }
}
