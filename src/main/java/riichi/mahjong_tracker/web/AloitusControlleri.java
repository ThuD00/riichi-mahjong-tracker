package riichi.mahjong_tracker.web;

import org.springframework.web.bind.annotation.GetMapping;

public class AloitusControlleri {
   
  @GetMapping("/")
    public String aloitus() {
      return "redirect:/peli/lista";
    }
}
