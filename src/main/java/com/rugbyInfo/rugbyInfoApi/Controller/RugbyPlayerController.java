package com.rugbyInfo.rugbyInfoApi.Controller;

import com.rugbyInfo.rugbyInfoApi.Service.RugbyPlayerInfoService;
import com.rugbyInfo.rugbyInfoApi.entity.RugbyPlayer;
import com.rugbyInfo.rugbyInfoApi.form.PlayerCreateForm;
import com.rugbyInfo.rugbyInfoApi.form.PlayerUpdateForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
public class RugbyPlayerController {

    private final RugbyPlayerInfoService rugbyPlayerInfoService;

    public RugbyPlayerController(RugbyPlayerInfoService rugbyPlayerInfoService) {
        this.rugbyPlayerInfoService = rugbyPlayerInfoService;
    }

    @GetMapping("/rugbyPlayers")
    public ResponseEntity<List<PlayerDataResponse>> getRugbyPlayers(@RequestParam(required = false)String nationality, @RequestParam(required = false)String name, @RequestParam(required = false) Integer height, @RequestParam(required = false) Integer weight, @RequestParam(required = false) String rugbyPosition) {
        List<PlayerDataResponse> players = rugbyPlayerInfoService.findPlayersByReference(nationality, name,height, weight, rugbyPosition);
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @PostMapping("/rugbyPlayers")
    public ResponseEntity<Map<String, String>> createRugbyPlayer(@RequestBody @Validated PlayerCreateForm playerCreateForm, UriComponentsBuilder uriBuilder) {
        RugbyPlayer rugbyPlayer = rugbyPlayerInfoService.insertRugbyPlayers(playerCreateForm);
        URI location = uriBuilder.path("/rugbyPlayer/{id}").buildAndExpand(rugbyPlayer.getId()).toUri();
        return ResponseEntity.created(location).body(Map.of("message", "選手が登録されました"));
    }

    @PatchMapping("/rugbyPlayers/{id}")
    public ResponseEntity<Map<String, String>> updateRugbyPlayer(@PathVariable("id") String id, @RequestBody @Validated PlayerUpdateForm playerUpdateForm) {
        rugbyPlayerInfoService.updateRugbyPlayer(id, playerUpdateForm.getNationality(), playerUpdateForm.getName(), playerUpdateForm.getHeight(), playerUpdateForm.getWeight(), playerUpdateForm.getRugbyPosition());
        return ResponseEntity.ok(Map.of("message", "選手データが更新されました"));
    }

    @DeleteMapping("rugbyPlayers/{id}")
    public ResponseEntity<Map<String, String>> deleteRugbyPlayer(@PathVariable("id") String id) {
        rugbyPlayerInfoService.deleteRugbyPlayer(id);
        return ResponseEntity.ok(Map.of("message", "選手が消去されました"));
    }
}
