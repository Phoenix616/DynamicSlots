package de.themoep.dynamicslots.core.sources;
/*
 * DynamicSlots - core
 * Copyright (C) 2022. Max Lee aka Phoenix616 (max@themoep.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
    public int getSlots(int playerCount, int slotCount) {
        try (Connection conn = getConn(); PreparedStatement sta = conn.prepareStatement(getQuery(playerCount, slotCount))) {
            if (sta.execute()) {
                ResultSet rs = sta.getResultSet();
                if (rs.next()) {
                    try {
                        return rs.getInt(1);
                    } catch (SQLException e) { // Not a number
                        return parseString(rs.getString(1), playerCount, slotCount);
                    }
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Error while getting slots count from database.", e);
        }
        return -1;
    }
}
