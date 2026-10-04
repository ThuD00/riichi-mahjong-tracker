package riichi.mahjong_tracker.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import riichi.mahjong_tracker.domain.Paikka;
import riichi.mahjong_tracker.repository.PaikkaRepository;

@Controller 
@RequestMapping("/paikka")
public class PaikkaController {
  private final PaikkaRepository paikkaRepository;

  public PaikkaController(PaikkaRepository paikkaRepository) {
    this.paikkaRepository = paikkaRepository;
  }

  @GetMapping("/lista")
  public String paikkaLista(Model model) {
    model.addAttribute("paikat", paikkaRepository.findAll());
    return "paikka/lista";
  }

  @GetMapping("/add")
  public String addPaikka(Model model) {
    model.addAttribute("paikka", new Paikka());
    return "paikka/add";
  }

  @GetMapping("/edit/{id}")
  public String editPaikka(@PathVariable("id") Long paikkaId, Model model) {
    Paikka paikka = paikkaRepository.findById(paikkaId).get();
    model.addAttribute("paikka", paikka);
    return "paikka/edit";
  }

  @PostMapping("/save")
    public String savePaikka(@ModelAttribute Paikka paikka) {
      paikkaRepository.save(paikka);
      return "redirect:/paikka/lista";
  } 
  
  @GetMapping("/delete/{id}")
    public String deletePaikka(@PathVariable("id") Long paikkaId, Model model) {
      paikkaRepository.deleteById(paikkaId);
      return "redirect:/paikka/lista";
  }
  
}
