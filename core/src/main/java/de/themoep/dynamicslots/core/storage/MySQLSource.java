package de.themoep.dynamicslots.core.storage;
/*
 * Copyright 2017 Max Lee (https://github.com/Phoenix616/)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Mozilla Public License as published by
 * the Mozilla Foundation, version 2.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License v2.0 for more details.
 * 
 * You should have received a copy of the Mozilla Public License v2.0
 * along with this program. If not, see <http://mozilla.org/MPL/2.0/>.
 */

import com.zaxxer.hikari.HikariDataSource;
import de.themoep.dynamicslots.core.DynamicSlotsPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class MySQLSource extends SlotSource {
    private final HikariDataSource ds;
    private final String query;

    public MySQLSource(DynamicSlotsPlugin plugin) throws SQLException {
        super(plugin);

        query = (String) plugin.getSetting("source.query");

        String host = (String) plugin.getSetting("source.host");
        int port = (int) plugin.getSetting("source.port");
        String database = (String) plugin.getSetting("source.database");

        ds = new HikariDataSource();
        ds.setDriverClassName(org.mariadb.jdbc.Driver.class.getName());
        ds.setJdbcUrl("jdbc:mariadb://" + host + ":" + port + "/" + database);
        ds.setUsername((String) plugin.getSetting("source.username"));
        ds.setPassword((String) plugin.getSetting("source.password"));
        ds.setConnectionTimeout(5000);
    }

    public Connection getConn() throws SQLException {
        return ds.getConnection();
    }

    @Override
    public void disable() {
        ds.close();
    }

    @Override
    public int getSlots() {
        try (Connection conn = getConn(); PreparedStatement sta = conn.prepareStatement(query)) {
            if (sta.execute()) {
                ResultSet rs = sta.getResultSet();
                if (rs.next()) {
                    try {
                        return rs.getInt(1);
                    } catch (SQLException e) { // Not a number
                        return parseString(rs.getString(1));
                    }
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Error while getting slots count from database.", e);
        }
        return -1;
    }
}
