package com.rugbyInfo.rugbyInfoApi.integrationtest;

import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class RugbyPlayerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @Transactional
    public void 全ての選手を表示できること() throws Exception {
        mockMvc.perform(get("/rugbyPlayers"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Gunter, Ben")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("192 cm")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("115 kg")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("FL")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Kenki, Fukuoka")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("175 cm")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("81 kg")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("WTB")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Seiji, Hirao")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("178 cm")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("78 kg")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("SO")));
    }

    @Test
    public void チーム名を選択したときにチームの選手が表示されること() throws Exception {
        // Similar to above but with a query parameter for nationality
        mockMvc.perform(get("/rugbyPlayers").param("nationality", "Japan"))
                .andExpect(status().isOk())
                .andExpect(view().name("rugbyPlayers"))
                .andExpect(model().attributeExists("nationalities"))
                .andExpect(model().attributeExists("groupedPlayers"));
    }

    @Test
    @Transactional
    public void 身長体重ポジションで選手を検索できること() throws Exception {
        mockMvc.perform(get("/rugbyPlayers/reference")
                        .param("height", "178")
                        .param("weight", "78")
                        .param("rugbyPosition", "SO"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Seiji, Hirao")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("178 cm")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("78 kg")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("SO")));
    }

    @Test
    @Transactional
    void 検索した選手が存在しない時に例外を出力すること() throws Exception {
        String response =
                mockMvc.perform(get("/rugbyPlayers/reference?height=200"))
                        .andExpect(status().isNotFound())
                        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                {
                  "message": "条件に該当する選手は存在しないか、条件の指定が誤っています"
                }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
                new Customization("message", ((o1, o2) -> true))));
    }

    @Test
    public void チーム別の平均身長体重が表示されること() throws Exception {
        mockMvc.perform(get("/rugbyPlayers/average"))
                .andExpect(status().isOk())
                .andExpect(view().name("rugbyPlayerAverages"))
                .andExpect(model().attributeExists("nationalities"))
                .andExpect(model().attributeExists("averages"));
    }

    @Test
    @Transactional
    public void 選手が登録できること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/rugbyPlayers/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                {
                                  "nationality": "Japan",
                                  "name": "Takeo, Ishizuka",
                                  "height": 172,
                                  "weight": 85,
                                  "rugby_position": "FL"
                                }
                                """))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @Transactional
    void POSTの際nullの項目を登録したときに登録されずにそのままの画面であること() throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/rugbyPlayers/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                                 {
                                   "nationality": "Japan",
                                   "name": null,
                                   "height": 172,
                                   "weight": 85,
                                   "rugby_position": "FL"
                                  }
                                """))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Transactional
    void POSTの際に身長が100から300の間に無い数値を登録したときに登録されずにそのままの画面であること() throws Exception {
        String response =
                mockMvc.perform(MockMvcRequestBuilders.post("/rugbyPlayers/create")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("""
                                        {
                                            "name": "Satoru, Oki",
                                            "height": 900,
                                            "weight": 80,
                                            "rugby_position": "FL"
                                        }
                                        """))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @Transactional
    void POSTの際に体重が10から300の間に無い数値を登録したときに登録されずにそのままの画面であること() throws Exception {
        String response =
                mockMvc.perform(MockMvcRequestBuilders.post("/rugbyPlayers/create")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("""
                                        {
                                            "name": "Satoru, Oki",
                                            "height": 172,
                                            "weight": 9,
                                            "rugby_position": "FL"
                                        }
                                        """))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @Transactional
    void 選手が更新できること() throws Exception {
        String playerId = "1";
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/rugbyPlayers/edit/" + playerId)
                .param("nationality", "Japan")
                .param("name", "Kenki, Fukuoka")
                .param("height", "177")
                .param("weight", "81")
                .param("rugbyPosition", "WTB");

        String response = mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @Transactional
    void UPDATEで指定したIDが存在しない時にエラーメッセージが返されること() throws Exception {
        String nonExistentId = "999";
        String response =
                mockMvc.perform(MockMvcRequestBuilders.patch("/rugbyPlayers/edit/" + nonExistentId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("""
                                        {
                                           "name": "Kenki, Fukuoka",
                                           "height": 175,
                                           "weight": 81,
                                           "rugby_position": "WTB"
                                         }
                                        """))
                        .andExpect(status().isNotFound())
                        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    @Test
    @Transactional
    void UPDATEですべての項目がnullだった場合にエラーメッセージが返されること() throws Exception {
        String playerId = "1";
        String response =
                mockMvc.perform(MockMvcRequestBuilders.patch("/rugbyPlayers/edit/" + playerId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("""
                                        {
                                           "nationality": null,
                                           "name": null,
                                           "height": null,
                                           "weight": null,
                                           "rugby_position": null
                                         }
                                        """))
                        .andExpect(status().isBadRequest())
                        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                {
                  "message": "更新するための情報が不足しています"
                }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
                new Customization("message", ((o1, o2) -> true))));
    }

    @Test
    @Transactional
    void UPDATEの際に身長が100から300の間に無い数値を登録したときにエラーメッセージが返されること() throws Exception {
        String playerId = "1";
        String response =
                mockMvc.perform(MockMvcRequestBuilders.patch("/rugbyPlayers/edit/" + playerId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("""
                                        {
                                            "name": "Satoru, Oki",
                                            "height": 900,
                                            "weight": 80,
                                            "rugby_position": "FL"
                                        }
                                        """))
                        .andExpect(status().isBadRequest())
                        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                {
                    "message": "身長は100から300の間で登録してください"
                }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
                new Customization("message", ((o1, o2) -> true))));
    }

    @Test
    @Transactional
    void UPDATEの際に体重が10から300の間に無い数値を登録したときにエラーメッセージが返されること() throws Exception {
        String playerId = "1";
        String response =
                mockMvc.perform(MockMvcRequestBuilders.patch("/rugbyPlayers/edit/" + playerId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("""
                                        {
                                            "name": "Satoru, Oki",
                                            "height": 172,
                                            "weight": 9,
                                            "rugby_position": "FL"
                                        }
                                        """))
                        .andExpect(status().isBadRequest())
                        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                {
                    "message": "体重は10から300の間で登録してください"  
                }
                """, response, new CustomComparator(JSONCompareMode.STRICT,
                new Customization("message", ((o1, o2) -> true))));
    }

    @Test
    @Transactional
    void 選手データが削除できること() throws Exception {
        String playerId = "1";
        String response =
                mockMvc.perform(MockMvcRequestBuilders.delete("/rugbyPlayers/delete/" + playerId))
                        .andExpect(status().is3xxRedirection())
                        .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }
}
