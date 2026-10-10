package riichi.mahjong_tracker.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import riichi.mahjong_tracker.domain.Tulos;
import riichi.mahjong_tracker.repository.TulosRepository;

@Controller 
@RequestMapping("/tulos")
public class TulosController {

  private final TulosRepository tulosRepository;

  public TulosController(TulosRepository tulosRepository) {
    this.tulosRepository = tulosRepository;
  }

  @GetMapping("/lista")
  public String TulosLista(Model model) {
    model.addAttribute("tulokset", tulosRepository.findAll());
    return "tulos/lista";
  }

  @GetMapping("/add")
  public String addTulos(Model model) {
    model.addAttribute("tulos", new Tulos());
    return "tulos/add";
  }

  @GetMapping("/edit/{id}")
  public String editTulos(@PathVariable("id") Long tulosId, Model model) {
    Tulos tulos = tulosRepository.findById(tulosId).get();
    model.addAttribute("tulos", tulos);
    return "tulos/edit";
  }

  @PostMapping("/save")
  public String saveTulos(@ModelAttribute Tulos tulos) {
    tulosRepository.save(tulos);
    return "redirect:/tulos/lista";
  }

  @GetMapping("/delete/{id}")
  public String deleteTulos(@PathVariable("id") Long tulosId, Model model) {
    tulosRepository.deleteById(tulosId);
    return "redirect:/tulos/lista";
  }
}

