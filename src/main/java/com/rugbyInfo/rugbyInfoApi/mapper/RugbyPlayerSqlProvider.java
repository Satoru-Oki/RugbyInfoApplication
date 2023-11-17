package com.rugbyInfo.rugbyInfoApi.mapper;

import org.apache.ibatis.jdbc.SQL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RugbyPlayerSqlProvider {

    public String referencePlayers(Map<String, Object> params) {
        return new SQL() {{
            SELECT("*");
            FROM("rugby_players_world_cup");

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

    public String findAverageByPositionGroupAndNationality() {

        List<String> fwPositions = Arrays.asList("PR", "HO", "L", "BR", "FL", "N8");
        List<String> bkPositions = Arrays.asList("SH", "HB", "C", "W", "FB");

        String sqlQuery = new SQL() {{
            SELECT("nationality",
                    "CASE WHEN rugby_position IN ('PR', 'HO', 'L', 'BR', 'FL') THEN 'FW' ELSE 'BK' END AS positionGroup",
                    "ROUND(AVG(height)) AS averageHeight",
                    "ROUND(AVG(weight)) AS averageWeight");
            FROM("rugby_players_world_cup");
            WHERE("height IS NOT NULL AND weight IS NOT NULL");
            GROUP_BY("nationality, positionGroup");
        }}.toString();

        return sqlQuery;
    }

    public String updateRugbyPlayer(Map<String, Object> params) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE rugby_players_world_cup SET ");

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
