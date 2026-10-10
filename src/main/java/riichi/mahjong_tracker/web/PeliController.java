package riichi.mahjong_tracker.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import riichi.mahjong_tracker.domain.Peli;
import riichi.mahjong_tracker.repository.PeliRepository;

@Controller 
@RequestMapping("/peli")
public class PeliController {
  private final PeliRepository peliRepository;

  public PeliController(PeliRepository peliRepository) {
    this.peliRepository = peliRepository;
  }

  @GetMapping("/lista")
  public String peliLista(Model model) {
    model.addAttribute("pelit", peliRepository.findAll());
    return "peli/lista";
  }

  @GetMapping("/add")
  public String addPeli(Model model) {
    model.addAttribute("peli", new Peli());
    return "peli/add";
  }

  @GetMapping("/edit/{id}")
  public String editPeli(@PathVariable("id") Long peliId, Model model) {
    Peli peli = peliRepository.findById(peliId).get();
    model.addAttribute("peli", peli);
    return "peli/edit";
  }

  @PostMapping("/save")
  public String savePeli(@ModelAttribute Peli peli) {
    peliRepository.save(peli);
    return "redirect:/peli/lista";
  }

  @GetMapping("/delete/{id}")
  public String deletePeli(@PathVariable("id") Long peliId, Model model) {
    peliRepository.deleteById(peliId);
    return "redirect:/peli/lista";
  }
}

