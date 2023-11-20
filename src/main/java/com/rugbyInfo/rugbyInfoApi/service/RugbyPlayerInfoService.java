package com.rugbyInfo.rugbyInfoApi.service;


import com.rugbyInfo.rugbyInfoApi.controller.PlayerDataResponse;
import com.rugbyInfo.rugbyInfoApi.controller.PositionGroupAverageResponse;
import com.rugbyInfo.rugbyInfoApi.entity.RugbyPlayer;
import com.rugbyInfo.rugbyInfoApi.exception.PlayerIllegalArgumentException;
import com.rugbyInfo.rugbyInfoApi.exception.PlayerNotFoundException;
import com.rugbyInfo.rugbyInfoApi.form.PlayerCreateForm;
import com.rugbyInfo.rugbyInfoApi.form.PlayerUpdateForm;
import com.rugbyInfo.rugbyInfoApi.mapper.RugbyPlayerMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RugbyPlayerInfoService {

    private final RugbyPlayerMapper rugbyPlayerMapper;

    public RugbyPlayerInfoService(RugbyPlayerMapper rugbyPlayerMapper) {
        this.rugbyPlayerMapper = rugbyPlayerMapper;
    }

    public record CategoryGroup(String category, List<PlayerDataResponse> players) {
    }

    public Map<String, List<CategoryGroup>> findPlayersGroupedByNationalityAndCategory(Optional<String> nationalityOpt) {
        List<RugbyPlayer> players;
        if (nationalityOpt.isPresent()) {
            players = rugbyPlayerMapper.findPlayersByNationality(nationalityOpt.get());
        } else {
            players = rugbyPlayerMapper.findAllPlayers();
        }

        Map<String, List<CategoryGroup>> categoryGroups = players.stream()
                .sorted(Comparator.comparingInt(player -> getPositionOrder(player.getRugbyPosition())))
                .collect(Collectors.groupingBy(
                        RugbyPlayer::getNationality,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .collect(Collectors.groupingBy(
                                                player -> categorizePosition(player.getRugbyPosition()),
                                                Collectors.mapping(
                                                        player -> new PlayerDataResponse(
                                                                player.getId(),
                                                                player.getNationality(),
                                                                player.getName(),
                                                                player.getHeight(),
                                                                player.getWeight(),
                                                                player.getRugbyPosition()
                                                        ),
                                                        Collectors.toList()
                                                )
                                        ))
                                        .entrySet().stream()
                                        .map(entry -> new CategoryGroup(entry.getKey(), entry.getValue()))
                                        .toList()
                        )
                ));

        return categoryGroups;
    }

    private String categorizePosition(String position) {
        if (Arrays.asList("N8", "BR", "PR", "HO", "L", "FL").contains(position)) {
            return "FW";
        } else {
            return "BK";
        }
    }

    private int getPositionOrder(String position) {
        if (position == null) {
            return 12;
        }
        Map<String, Integer> positionOrder = Map.of(
                "PR", 1, "HO", 2, "L", 3, "FL", 4, "BR", 5,
                "SH", 6, "FH", 7, "HB", 8, "C", 9, "W", 10
        );
        return positionOrder.getOrDefault(position, 12);
    }

    public Map<String, List<PlayerDataResponse>> findPlayersByReference(Integer height, Integer weight, String rugbyPosition) {
        List<RugbyPlayer> players = rugbyPlayerMapper.findPlayersByReference(height, weight, rugbyPosition);

        if (players.isEmpty()) {
            throw new PlayerNotFoundException("条件に該当する選手は存在しないか、条件の指定が誤っています");
        } else {
            return players.stream()
                    .map(player -> new PlayerDataResponse(
                            player.getId(),
                            player.getNationality(),
                            player.getName(),
                            player.getHeight() != null ? player.getHeight() : null,
                            player.getWeight() != null ? player.getWeight() : null,
                            player.getRugbyPosition() != null ? player.getRugbyPosition() : null
                    ))
                    .collect(Collectors.groupingBy(PlayerDataResponse::getNationality));
        }
    }

    public List<PositionGroupAverageResponse> findAverageByPositionGroupAndNationality() {
        return rugbyPlayerMapper.findAverageByPositionGroupAndNationality();
    }

    public void insertRugbyPlayers(List<RugbyPlayer> rugbyPlayersList) {
        for (RugbyPlayer rugbyPlayer : rugbyPlayersList) {
            Optional<RugbyPlayer> existingPlayer = rugbyPlayerMapper.findPlayerById(rugbyPlayer.getId());

            if (existingPlayer.isEmpty()) {
                rugbyPlayerMapper.insertPlayerData(rugbyPlayer);
            }
        }
    }

    public RugbyPlayer insertRugbyPlayers(PlayerCreateForm playerCreateForm) {
        RugbyPlayer rugbyPlayer = new RugbyPlayer(playerCreateForm.getNationality(), playerCreateForm.getName(), playerCreateForm.getHeight(), playerCreateForm.getWeight(), playerCreateForm.getRugbyPosition());
        String shortId = UUID.randomUUID().toString().substring(0, 8);
        rugbyPlayer.setId(shortId);
        rugbyPlayerMapper.insertPlayerData(rugbyPlayer);
        return rugbyPlayer;
    }

    public void updateRugbyPlayer(String id, String nationality, String name, Integer height, Integer weight, String rugbyPosition) {
        rugbyPlayerMapper.findPlayerById(id).orElseThrow(() -> new PlayerNotFoundException("当該IDを持つ選手は存在しません"));
        if (name != null && name.trim().isEmpty()) {
            throw new PlayerIllegalArgumentException("空白の入力は許可されていません");
        }

        if (rugbyPosition != null && rugbyPosition.trim().isEmpty()) {
            throw new PlayerIllegalArgumentException("空白の入力は許可されていません");
        }

        if (nationality == null && name == null && height == null && weight == null && rugbyPosition == null) {
            throw new PlayerIllegalArgumentException("更新するための情報が不足しています");
        }

        rugbyPlayerMapper.updateRugbyPlayer(id, nationality, name, height, weight, rugbyPosition);
    }

    public void deleteRugbyPlayer(String id) {
        rugbyPlayerMapper.findPlayerById(id).orElseThrow(() -> new PlayerNotFoundException("当該IDを持つ選手は存在しません"));
        rugbyPlayerMapper.deleteRugbyPlayer(id);
    }

    //修正フォームに値を表示
    public PlayerUpdateForm getPlayerForUpdate(String id) {
        RugbyPlayer player = rugbyPlayerMapper.findPlayerById(id)
                .orElseThrow(() -> new PlayerNotFoundException("当該IDを持つ選手は存在しません"));

        PlayerUpdateForm form = new PlayerUpdateForm();
        form.setId(player.getId());
        form.setNationality(player.getNationality());
        form.setName(player.getName());
        form.setHeight(player.getHeight());
        form.setWeight(player.getWeight());
        form.setRugbyPosition(player.getRugbyPosition());

        return form;
    }

    //プルダウンメニュー表示用メソッド
    public List<String> getAvailableNationalities() {
        return rugbyPlayerMapper.findDistinctNationalities();
    }

    public List<String> getAvailablePositions() {
        return rugbyPlayerMapper.findDistinctRugbyPositions();
    }
}
