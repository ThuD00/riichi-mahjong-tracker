package riichi.mahjong_tracker.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import riichi.mahjong_tracker.service.TilastoService;

@Controller 
@RequestMapping("/tilasto")
public class TilastoController {

  public final TilastoService tilastoService;

  public TilastoController(TilastoService tilastoService) {
    this.tilastoService = tilastoService;
  }

  @GetMapping
  public String naytaPelaajaTilasto(Model model) {
    model.addAttribute(
      "pelaajaTilastot", 
      tilastoService.haePelaajaTilastot()
    );
    
    model.addAttribute(
      "pelienMaara", 
      tilastoService.haePelienMaara()
    );

    model.addAttribute(
      "pelaajienMaara", 
      tilastoService.haePelaajienMaara()
    );

    model.addAttribute(
      "pelipaikkojenMaara", 
      tilastoService.haePelipaikkojenMaara()
    );

    model.addAttribute(
      "tulostenMaara", 
      tilastoService.haeTulostenMaara()
    );
    
    return "tilasto/tilastot";
  }
}
