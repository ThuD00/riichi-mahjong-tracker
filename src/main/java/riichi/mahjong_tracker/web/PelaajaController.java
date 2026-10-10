package riichi.mahjong_tracker.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import riichi.mahjong_tracker.domain.Pelaaja;
import riichi.mahjong_tracker.repository.PelaajaRepository;

@Controller 
@RequestMapping("/pelaaja")
public class PelaajaController {

  private final PelaajaRepository pelaajaRepository;

  public PelaajaController(PelaajaRepository pelaajaRepository) {
    this.pelaajaRepository = pelaajaRepository;
  }

  @GetMapping("/lista")
  public String pelaajaLista(Model model) {
    model.addAttribute("pelaajat", pelaajaRepository.findAll());
    return "pelaaja/lista";
  }

  @GetMapping("/add")
  public String addPelaaja(Model model) {
    model.addAttribute("pelaaja", new Pelaaja());
    return "pelaaja/add";
  }

  @GetMapping("/edit/{id}")
  public String editPelaaja(@PathVariable("id") Long pelaajaId, Model model) {
    Pelaaja pelaaja = pelaajaRepository.findById(pelaajaId).get();
    model.addAttribute("pelaaja", pelaaja);
    return "pelaaja/edit";
  }

  @PostMapping("/save")
  public String savePelaaja(@ModelAttribute Pelaaja pelaaja) {
    pelaajaRepository.save(pelaaja);
    return "redirect:/pelaaja/lista";
  }

  @GetMapping("/delete/{id}")
  public String deletePelaaja(@PathVariable("id") Long pelaajaId, Model model) {
    pelaajaRepository.deleteById(pelaajaId);
    return "redirect:/pelaaja/lista";
  }
}
