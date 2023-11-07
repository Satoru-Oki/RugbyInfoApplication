package com.rugbyInfo.rugbyInfoApi.mapper;

import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RugbyPlayerSqlProvider {

    public String referencePlayers(Map<String, Object> params) {
        return new SQL() {{
            SELECT("*");
            FROM("rugby_players");

            if (params.get("nationality") != null) {
                WHERE("nationality = #{nationality}");
            }

            if (params.get("name") != null) {
                WHERE("name = #{name}");
            }

            if (params.get("height") != null) {
                WHERE("height >= #{height}");
            }
            if (params.get("weight") != null) {
                WHERE("weight >= #{weight}");
            }
            if (params.get("rugbyPosition") != null) {
                WHERE("rugby_position = #{rugbyPosition}");
            }
        }}.toString();
    }

    public String updateRugbyPlayer(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE rugby_players SET ");

        List<String> updates = new ArrayList<>();

        if (params.get("nationality") != null) {
            updates.add("nationality = #{nationality}");
        }

        if (params.get("name") != null) {
            updates.add("name = #{name}");
        }
        if (params.get("height") != null) {
            updates.add("height = #{height}");
        }
        if (params.get("weight") != null) {
            updates.add("weight = #{weight}");
        }
        if (params.get("rugbyPosition") != null) {
            updates.add("rugby_position = #{rugbyPosition}");
        }

        sql.append(String.join(", ", updates));
        sql.append(" WHERE id = #{id}");

        return sql.toString();
    }
}
