package com.rugbyInfo.rugbyInfoApi.service;

import com.rugbyInfo.rugbyInfoApi.controller.PlayerDataResponse;
import com.rugbyInfo.rugbyInfoApi.controller.PositionGroupAverageResponse;
import com.rugbyInfo.rugbyInfoApi.entity.RugbyPlayer;
import com.rugbyInfo.rugbyInfoApi.form.PlayerCreateForm;
import com.rugbyInfo.rugbyInfoApi.mapper.RugbyPlayerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RugbyPlayerInfoServiceTest {

    @InjectMocks
    public RugbyPlayerInfoService rugbyPlayerInfoService;

    @Mock
    private RugbyPlayerMapper rugbyPlayerMapper;

    @Test
    public void insertRugbyPlayers_テーブルにデータが存在しないときにデータを登録できること() {
        RugbyPlayer rugbyPlayer1 = new RugbyPlayer("1", "Japan", "Kenki, Fukuoka", 175, 85, "WTB");
        RugbyPlayer rugbyPlayer2 = new RugbyPlayer("2", "Japan", "Takeo, Ishizuka", 172, 85, "FL");
        RugbyPlayer rugbyPlayer3 = new RugbyPlayer("3", "Japan", "Seiji, Hirao", 177, 80, "SO");

        List<RugbyPlayer> rugbyPlayerList = List.of(rugbyPlayer1, rugbyPlayer2, rugbyPlayer3);

        doReturn(Optional.empty()).when(rugbyPlayerMapper).findPlayerById("1");
        doReturn(Optional.empty()).when(rugbyPlayerMapper).findPlayerById("2");
        doReturn(Optional.empty()).when(rugbyPlayerMapper).findPlayerById("3");

        rugbyPlayerInfoService.insertRugbyPlayers(rugbyPlayerList);

        verify(rugbyPlayerMapper, times(1)).insertPlayerData(rugbyPlayer1);
        verify(rugbyPlayerMapper, times(1)).insertPlayerData(rugbyPlayer2);
        verify(rugbyPlayerMapper, times(1)).insertPlayerData(rugbyPlayer3);
    }

    @Test
    public void insertRugbyPlayers_複数のデータを登録しようとしたときテーブルにすでに同じIDが存在する場合INSERTがスキップされること() {
        RugbyPlayer existingPlayer = new RugbyPlayer("1", "Japan", "Kenki, Fukuoka", 175, 85, "WTB");
        RugbyPlayer newPlayer = new RugbyPlayer("2", "Japan", "Takeo, Ishizuka", 172, 85, "FL");

        List<RugbyPlayer> rugbyPlayerList = List.of(existingPlayer, newPlayer);

        doReturn(Optional.of(existingPlayer)).when(rugbyPlayerMapper).findPlayerById("1");
        doReturn(Optional.empty()).when(rugbyPlayerMapper).findPlayerById("2");

        rugbyPlayerInfoService.insertRugbyPlayers(rugbyPlayerList);

        verify(rugbyPlayerMapper, never()).insertPlayerData(existingPlayer);
        verify(rugbyPlayerMapper, times(1)).insertPlayerData(newPlayer);
    }

    @Test
    public void findPlayersByReference_検索したときにPlayerDataResponseのリストを返すこと() {
        RugbyPlayer rugbyPlayer = new RugbyPlayer("1", "Japan", "Kenki, Fukuoka", 175, 85, "WTB");

        doReturn(List.of(rugbyPlayer)).when(rugbyPlayerMapper).findPlayersByReference(175, 85, "WTB");

        Map<String, List<PlayerDataResponse>> response = rugbyPlayerInfoService.findPlayersByReference(175, 85, "WTB");

        PlayerDataResponse expectedResponse = new PlayerDataResponse("1", "Japan", "Kenki, Fukuoka", 175, 85, "WTB");
        assertEquals(List.of(expectedResponse), response.get("Japan"));
    }

    @Test
    public void findPlayersByReference_検索したデータが存在しないときに例外を返すこと() {

        doReturn(List.of()).when(rugbyPlayerMapper).findPlayersByReference(175, 85, "WTB");

        assertThrows(PlayerNotFoundException.class, () -> {
            rugbyPlayerInfoService.findPlayersByReference(175, 85, "WTB");
        });
    }

    @Test
    void findAverageByPositionGroupAndNationality_チーム毎にFW_BKの平均身長体重を取得できること() {

        List<PositionGroupAverageResponse> mockData = List.of(
                new PositionGroupAverageResponse("Japan", "FW", 180, 90),
                new PositionGroupAverageResponse("New Zealand", "BK", 185, 95)
        );

        when(rugbyPlayerMapper.findAverageByPositionGroupAndNationality()).thenReturn(mockData);

        List<PositionGroupAverageResponse> result = rugbyPlayerInfoService.findAverageByPositionGroupAndNationality();

        PositionGroupAverageResponse firstResponse = result.get(0);
        assertEquals("Japan", firstResponse.getNationality());
        assertEquals("FW", firstResponse.getPositionGroup());
        assertEquals(180, firstResponse.getAverageHeight());
        assertEquals(90, firstResponse.getAverageWeight());
    }

    @Test
    public void createRugbyPlayer_RugbyCreateFormで取得した選手を登録できること() {
        PlayerCreateForm createForm = new PlayerCreateForm("Japan", "Kenki, Fukuoka", 175, 85, "WTB");
        RugbyPlayer expectedRugbyPlayer = new RugbyPlayer("Japan", "Kenki, Fukuoka", 175, 85, "WTB");
        expectedRugbyPlayer.setId(UUID.randomUUID().toString().substring(0, 8));
        lenient().doNothing().when(rugbyPlayerMapper).insertPlayerData(expectedRugbyPlayer);
        RugbyPlayer actual = rugbyPlayerInfoService.insertRugbyPlayers(createForm);

        assertEquals(expectedRugbyPlayer.getNationality(), actual.getNationality());
        assertEquals(expectedRugbyPlayer.getName(), actual.getName());
        assertEquals(expectedRugbyPlayer.getHeight(), actual.getHeight());
        assertEquals(expectedRugbyPlayer.getWeight(), actual.getWeight());
        assertEquals(expectedRugbyPlayer.getRugbyPosition(), actual.getRugbyPosition());
    }

    @ParameterizedTest
    @MethodSource("updateRugbyPlayerParameters")
    public void updateRugbyPlayer_IDがある場合に他の属性がnullであっても正常に更新ができること(String playerId, String nationality, String name, Integer height, Integer weight, String rugbyPosition) throws Exception {
        RugbyPlayer updatePlayer = new RugbyPlayer(playerId, "Japan", "Kenki, Fukuoka", 175, 85, "WTB");
        doReturn(Optional.of(updatePlayer)).when(rugbyPlayerMapper).findPlayerById(playerId);

        rugbyPlayerInfoService.updateRugbyPlayer(playerId, nationality, name, height, weight, rugbyPosition);
        verify(rugbyPlayerMapper, times(1)).updateRugbyPlayer(playerId, nationality, name, height, weight, rugbyPosition);
    }

    private static Stream<Arguments> updateRugbyPlayerParameters() {
        String[] nationalities = {null, "Japan"};
        String[] names = {null, "Takeo, Ishizuka"};
        Integer[] heights = {null, 171};
        Integer[] weights = {null, 80};
        String[] rugbyPositions = {null, "FL"};

        return Arrays.stream(nationalities)
                .flatMap(nationality -> Arrays.stream(names)
                        .flatMap(name -> Arrays.stream(heights)
                                .flatMap(height -> Arrays.stream(weights)
                                        .flatMap(weight -> Arrays.stream(rugbyPositions)
                                                .filter(rugbyPosition -> !(nationality == null && name == null && height == null && weight == null && rugbyPosition == null))
                                                .map(rugbyPosition -> Arguments.of("1", nationality, name, height, weight, rugbyPosition))))));
    }

    @Test
    public void updateRugbyPlayer_指定したIDが存在しないとき例外を返すこと() {
        doReturn(Optional.empty()).when(rugbyPlayerMapper).findPlayerById("999");

        assertThatThrownBy(() -> rugbyPlayerInfoService.updateRugbyPlayer("999", "Japan", "Kenki, Fukuoka", 175, 85, "WTB"))
                .isInstanceOfSatisfying(PlayerNotFoundException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo("当該IDを持つ選手は存在しません");
                });

        verify(rugbyPlayerMapper, times(1)).findPlayerById("999");
    }

    @Test
    public void updateRugbyPlayer_名前が空白のときに例外を返すこと() {

        doReturn(Optional.of(new RugbyPlayer())).when(rugbyPlayerMapper).findPlayerById("1");

        assertThatThrownBy(() -> rugbyPlayerInfoService.updateRugbyPlayer("1", "Japan", " ", 175, 85, "WTB"))
                .isInstanceOfSatisfying(PlayerIllegalArgumentException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo("空白の入力は許可されていません");
                });

        verify(rugbyPlayerMapper, times(1)).findPlayerById("1");
    }

    @Test
    public void updateRugbyPlayer_ポジションが空白だったときに例外を返すこと() {
        doReturn(Optional.of(new RugbyPlayer())).when(rugbyPlayerMapper).findPlayerById("1");

        assertThatThrownBy(() -> rugbyPlayerInfoService.updateRugbyPlayer("1", "Japan", "Kenki, Fukuoka", 175, 85, " "))
                .isInstanceOfSatisfying(PlayerIllegalArgumentException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo("空白の入力は許可されていません");
                });

        verify(rugbyPlayerMapper, times(1)).findPlayerById("1");
    }

    @Test
    public void deleteRugbyPlayer_IDがある場合に選手を削除できること() {
        doReturn(Optional.of(new RugbyPlayer())).when(rugbyPlayerMapper).findPlayerById("1");

        rugbyPlayerInfoService.deleteRugbyPlayer("1");

        verify(rugbyPlayerMapper, times(1)).findPlayerById("1");
        verify(rugbyPlayerMapper, times(1)).deleteRugbyPlayer("1");
    }

    @Test
    public void deleteRugbyPlayer_指定したIDが存在しないときに例外を返すこと() {
        doReturn(Optional.empty()).when(rugbyPlayerMapper).findPlayerById("999");

        assertThatThrownBy(() -> rugbyPlayerInfoService.deleteRugbyPlayer("999"))
                .isInstanceOfSatisfying(PlayerNotFoundException.class, e -> {
                    assertThat(e.getMessage()).isEqualTo("当該IDを持つ選手は存在しません");
                });

        verify(rugbyPlayerMapper, times(1)).findPlayerById("999");
        verify(rugbyPlayerMapper, never()).deleteRugbyPlayer("999");
    }
}
