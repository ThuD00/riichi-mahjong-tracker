package riichi.mahjong_tracker.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class AloitusControlleri {
   
  @GetMapping("/")
    public String aloitus() {
      return "redirect:/tilasto";
    }
}
