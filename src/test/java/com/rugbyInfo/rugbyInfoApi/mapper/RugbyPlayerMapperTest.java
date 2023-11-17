package com.rugbyInfo.rugbyInfoApi.mapper;

import com.github.database.rider.spring.api.DBRider;
import com.rugbyInfo.rugbyInfoApi.controller.PositionGroupAverageResponse;
import com.rugbyInfo.rugbyInfoApi.entity.RugbyPlayer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DBRider
@MybatisTest
class RugbyPlayerMapperTest {

    @Autowired
    RugbyPlayerMapper rugbyPlayerMapper;

    @Test
    @Transactional
    void insertPlayerData_データベースにデータを登録できること() {
        rugbyPlayerMapper.insertPlayerData(new RugbyPlayer("4", "Japan", "Kenki, Fukuoka", 175, 81, "WTB"));
    }

    @Test
    @Transactional
    void findPlayerById_指定したIDを持つラグビー選手のデータが取得できること() {
        assertThat(rugbyPlayerMapper.findPlayerById("2")).contains(new RugbyPlayer("2", "Japan", "Seiji, Hirao", 178, 78, "SO"));
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    @Transactional
    void findPlayersByReference_nullを含んだnatonality_name_身長_体重_ポジションの組み合わせで検索ができること(Integer height, Integer weight, String rugbyPosition) {
        List<RugbyPlayer> players = rugbyPlayerMapper.findPlayersByReference(height, weight, rugbyPosition);

        if (height != null) {
            players.forEach(player -> assertTrue(player.getHeight() >= height));
        }
        if (weight != null) {
            players.forEach(player -> assertTrue(player.getWeight() >= weight));
        }
        if (rugbyPosition != null) {
            players.forEach(player -> assertEquals(rugbyPosition, player.getRugbyPosition()));
        }
    }

    static Stream<Arguments> provideParameters() {
        Integer[] heights = {null, 177};
        Integer[] weights = {null, 83};
        String[] rugbyPositions = {null, "WTB"};

        return Arrays.stream(heights)
                .flatMap(height -> Arrays.stream(weights)
                        .flatMap(weight -> Arrays.stream(rugbyPositions)
                                .map(rugbyPosition -> Arguments.of(height, weight, rugbyPosition)))
                );
    }

    @ParameterizedTest
    @MethodSource("playerSearchCriteria")
    @Transactional
    void findPlayersByReference_nullを含んだ身長体重ポジションの組み合わせで検索ができること(Integer height, Integer weight, String rugbyPosition, List<RugbyPlayer> expectedPlayers) {

        List<RugbyPlayer> players = rugbyPlayerMapper.findPlayersByReference(height, weight, rugbyPosition);

        assertEquals(expectedPlayers, players);
    }

    static Stream<Arguments> playerSearchCriteria() {
        return Stream.of(
                Arguments.of(null, null, null, List.of(new RugbyPlayer("1", "Japan", "Kenki, Fukuoka", 175, 81, "WTB"), new RugbyPlayer("2", "Japan", "Seiji, Hirao", 178, 78, "SO"),
                        new RugbyPlayer("3", "Japan", "Gunter, Ben", 192, 115, "FL"))),
                Arguments.of(177, 100, "WTB", Collections.emptyList()),
                Arguments.of(176, null, null, List.of(new RugbyPlayer("2", "Japan", "Seiji, Hirao", 178, 78, "SO"), new RugbyPlayer("3", "Japan", "Gunter, Ben", 192, 115, "FL"))),
                Arguments.of(null, 100, null, List.of(new RugbyPlayer("3", "Japan", "Gunter, Ben", 192, 115, "FL"))),
                Arguments.of(null, null, "WTB", List.of(new RugbyPlayer("1", "Japan", "Kenki, Fukuoka", 175, 81, "WTB"))),
                Arguments.of(178, 78, null, List.of(new RugbyPlayer("2", "Japan", "Seiji, Hirao", 178, 78, "SO"), new RugbyPlayer("3", "Japan", "Gunter, Ben", 192, 115, "FL"))),
                Arguments.of(192, null, "FL", List.of(new RugbyPlayer("3", "Japan", "Gunter, Ben", 192, 115, "FL"))),
                Arguments.of(null, 81, "WTB", List.of(new RugbyPlayer("1", "Japan", "Kenki, Fukuoka", 175, 81, "WTB")))
        );
    }

    @Test
    @Transactional
    void findAverageByPositionGroupAndNationality_チーム別にFW_BKグループで平均身長体重が取得できること() {

        List<PositionGroupAverageResponse> averages = rugbyPlayerMapper.findAverageByPositionGroupAndNationality();

        assertThat(averages).isNotNull();

        boolean matchesCondition = averages.stream().anyMatch(average ->
                "Japan".equals(average.getNationality()) &&
                        Arrays.asList("FW", "BK").contains(average.getPositionGroup())
        );
        assertTrue(matchesCondition);
    }


    @ParameterizedTest
    @MethodSource("updateRugbyPlayerRealParameters")
    @Transactional
    public void updateRugbyPlayer_チーム身長体重ポジションすべての組み合わせで更新ができること(String id, String nationality, String name, Integer height, Integer weight, String rugbyPosition, RugbyPlayer expectedPlayer) {

        rugbyPlayerMapper.updateRugbyPlayer(id, nationality, name, height, weight, rugbyPosition);

        Optional<RugbyPlayer> playerOptional = rugbyPlayerMapper.findPlayerById(id);
        assertTrue(playerOptional.isPresent(), "当該IDを持つ選手は存在しません");
        RugbyPlayer updatedPlayer = playerOptional.get();

        assertEquals(expectedPlayer.getNationality(), updatedPlayer.getNationality());
        assertEquals(expectedPlayer.getName(), updatedPlayer.getName());
        assertEquals(expectedPlayer.getHeight(), updatedPlayer.getHeight());
        assertEquals(expectedPlayer.getWeight(), updatedPlayer.getWeight());
        assertEquals(expectedPlayer.getRugbyPosition(), updatedPlayer.getRugbyPosition());
    }

    private static Stream<Arguments> updateRugbyPlayerRealParameters() {
        return Stream.of(
                Arguments.of("1", "Japan", "Takeo, Ishizuka", null, null, null, new RugbyPlayer("1", "Japan", "Takeo, Ishizuka", 175, 81, "WTB")),
                Arguments.of("1", null, null, 172, null, null, new RugbyPlayer("1", "Japan", "Kenki, Fukuoka", 172, 81, "WTB")),
                Arguments.of("1", null, null, null, 83, null, new RugbyPlayer("1", "Japan", "Kenki, Fukuoka", 175, 83, "WTB")),
                Arguments.of("1", null, null, null, null, "FL", new RugbyPlayer("1", "Japan", "Kenki, Fukuoka", 175, 81, "FL")),
                Arguments.of("1", null, "Takeo, Ishizuka", 172, null, null, new RugbyPlayer("1", "Japan", "Takeo, Ishizuka", 172, 81, "WTB")),
                Arguments.of("1", null, "Takeo, Ishizuka", null, 83, null, new RugbyPlayer("1", "Japan", "Takeo, Ishizuka", 175, 83, "WTB")),
                Arguments.of("1", null, "Takeo, Ishizuka", null, null, "FL", new RugbyPlayer("1", "Japan", "Takeo, Ishizuka", 175, 81, "FL")),
                Arguments.of("1", null, null, 172, 83, null, new RugbyPlayer("1", "Japan", "Kenki, Fukuoka", 172, 83, "WTB")),
                Arguments.of("1", null, null, 172, null, "FL", new RugbyPlayer("1", "Japan", "Kenki, Fukuoka", 172, 81, "FL")),
                Arguments.of("1", null, null, null, 83, "FL", new RugbyPlayer("1", "Japan", "Kenki, Fukuoka", 175, 83, "FL")),
                Arguments.of("1", null, "Takeo, Ishizuka", 172, 83, null, new RugbyPlayer("1", "Japan", "Takeo, Ishizuka", 172, 83, "WTB")),
                Arguments.of("1", null, "Takeo, Ishizuka", null, 83, "FL", new RugbyPlayer("1", "Japan", "Takeo, Ishizuka", 175, 83, "FL")),
                Arguments.of("1", null, null, 172, 83, "FL", new RugbyPlayer("1", "Japan", "Kenki, Fukuoka", 172, 83, "FL")),
                Arguments.of("1", null, "Takeo, Ishizuka", 172, null, "FL", new RugbyPlayer("1", "Japan", "Takeo, Ishizuka", 172, 81, "FL")),
                Arguments.of("1", null, "Takeo, Ishizuka", 172, 83, "FL", new RugbyPlayer("1", "Japan", "Takeo, Ishizuka", 172, 83, "FL"))
        );
    }

    @Test
    @Transactional
    void 指定したIDの選手データが消去できること() {
        rugbyPlayerMapper.deleteRugbyPlayer("1");
    }
}
