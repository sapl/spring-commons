package org.sapl.commons.dao;

import java.util.Date;
import java.util.Map;

public interface ConfigDao {

    String CACHE_NAME = "config";

    Map getAll();

    Date getDate(String name);

    boolean getBoolean(String name);

    String get(String name);

    String get(String name, String def);

    String put(String name, Object value);

    void delete(String name);

}
