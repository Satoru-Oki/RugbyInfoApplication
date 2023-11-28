package com.rugbyInfo.rugbyInfoApi.controller;

import com.rugbyInfo.rugbyInfoApi.entity.RugbyPlayer;
import com.rugbyInfo.rugbyInfoApi.form.PlayerCreateForm;
import com.rugbyInfo.rugbyInfoApi.form.PlayerUpdateForm;
import com.rugbyInfo.rugbyInfoApi.service.RugbyPlayerInfoService;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
public class RugbyPlayerController {

    private final RugbyPlayerInfoService rugbyPlayerInfoService;

    public RugbyPlayerController(RugbyPlayerInfoService rugbyPlayerInfoService) {
        this.rugbyPlayerInfoService = rugbyPlayerInfoService;
    }

    @GetMapping("/rugbyPlayers")
    public String getRugbyPlayers(Model model, @RequestParam Optional<String> nationality) {
        List<String> nationalities = rugbyPlayerInfoService.getAvailableNationalities();
        var groupedPlayers = rugbyPlayerInfoService.findPlayersGroupedByNationalityAndCategory(nationality);
        model.addAttribute("nationalities", nationalities);
        model.addAttribute("groupedPlayers", groupedPlayers);
        return "rugbyPlayers";
    }

    @GetMapping("/rugbyPlayers/reference")
    public String getRugbyPlayers(Model model,
                                  @RequestParam(required = false) Integer height,
                                  @RequestParam(required = false) Integer weight,
                                  @RequestParam(required = false) String rugbyPosition) {
        List<String> availablePositions = rugbyPlayerInfoService.getAvailablePositions();
        model.addAttribute("positions", availablePositions);
        rugbyPosition = (!StringUtils.isEmpty(rugbyPosition)) ? rugbyPosition : null;

        Map<String, List<PlayerDataResponse>> players = rugbyPlayerInfoService.findPlayersByReference(height, weight, rugbyPosition);

        model.addAttribute("players", players);

        return "rugbyPlayerReference";
    }

    @GetMapping("/rugbyPlayers/average")
    public String getAverageRugbyPlayers(Model model) {
        List<String> nationalities = rugbyPlayerInfoService.getAvailableNationalities();
        List<PositionGroupAverageResponse> averages = rugbyPlayerInfoService.findAverageByPositionGroupAndNationality();

        model.addAttribute("nationalities", nationalities);
        model.addAttribute("averages", averages);
        return "rugbyPlayerAverages";
    }

    @PostMapping("/rugbyPlayers/create")
    public String createRugbyPlayer(@ModelAttribute @Validated PlayerCreateForm playerCreateForm, BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "playerCreateForm";
        }
        RugbyPlayer rugbyPlayer = rugbyPlayerInfoService.insertRugbyPlayers(playerCreateForm);
        redirectAttributes.addFlashAttribute("message", "選手が登録されました");
        return "redirect:/rugbyPlayers";
    }

    @GetMapping("/rugbyPlayers/new")
    public String showCreateForm(Model model) {
        model.addAttribute("playerCreateForm", new PlayerCreateForm());
        return "playerCreateForm";
    }

    @PatchMapping("/rugbyPlayers/edit/{id}")
    public String updateRugbyPlayer(@PathVariable("id") String id,
                                    @ModelAttribute @Validated PlayerUpdateForm playerUpdateForm,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "playerUpdateForm";
        }
        rugbyPlayerInfoService.updateRugbyPlayer(id, playerUpdateForm.getNationality(), playerUpdateForm.getName(),
                playerUpdateForm.getHeight(), playerUpdateForm.getWeight(), playerUpdateForm.getRugbyPosition());
        redirectAttributes.addFlashAttribute("message", "選手データが更新されました");
        return "redirect:/rugbyPlayers";
    }

    @GetMapping("/rugbyPlayers/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        PlayerUpdateForm playerUpdateForm = rugbyPlayerInfoService.getPlayerForUpdate(id);
        model.addAttribute("playerId", id);
        model.addAttribute("playerUpdateForm", playerUpdateForm);
        return "playerUpdateForm";
    }

    @DeleteMapping("/rugbyPlayers/delete/{id}")
    public String deleteRugbyPlayer(@PathVariable("id") String id, RedirectAttributes redirectAttributes) {
        rugbyPlayerInfoService.deleteRugbyPlayer(id);
        redirectAttributes.addFlashAttribute("message", "選手が消去されました");
        return "redirect:/rugbyPlayers";
    }
}
