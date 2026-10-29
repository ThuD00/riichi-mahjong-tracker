package riichi.mahjong_tracker.web;

import java.time.LocalDateTime;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import riichi.mahjong_tracker.domain.Peli;
import riichi.mahjong_tracker.repository.PaikkaRepository;
import riichi.mahjong_tracker.repository.PeliRepository;

@Controller 
@RequestMapping("/peli")
public class PeliController {
  private final PeliRepository peliRepository;
  private final PaikkaRepository paikkaRepository;

  public PeliController(PeliRepository peliRepository, PaikkaRepository paikkaRepository) {
    this.peliRepository = peliRepository;
    this.paikkaRepository = paikkaRepository;
  }

  @GetMapping("/lista")
  public String peliLista(Model model) {
    model.addAttribute("pelit", peliRepository.findAll());
    return "peli/lista";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/add")
  public String addPeli(Model model) {
    model.addAttribute("peli", new Peli());
    model.addAttribute("paikat", paikkaRepository.findAll());
    return "peli/add";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/edit/{id}")
  public String editPeli(@PathVariable("id") Long peliId, Model model) {
    Peli peli = peliRepository.findById(peliId).get();
    model.addAttribute("peli", peli);
    return "peli/edit";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/save")
  public String savePeli(@ModelAttribute Peli peli) {
    peli.setPvm(LocalDateTime.now());
    peliRepository.save(peli);
    return "redirect:/peli/lista";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/delete/{id}")
  public String deletePeli(@PathVariable("id") Long peliId, Model model) {
    peliRepository.deleteById(peliId);
    return "redirect:/peli/lista";
  }

  //hakee yksittäisen pelin ID:n perusteella ja avaa uuden näkymän
  @GetMapping("/{id}")
  public String naytaPelinTiedot(@PathVariable("id") Long peliId, Model model) {
    Peli peli = peliRepository.findById(peliId)
      .orElseThrow(() -> new RuntimeException("Peliä ei löytynyt"));
    
    model.addAttribute("peli", peli);

    return "peli/tiedot";
  }
}

