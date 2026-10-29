package riichi.mahjong_tracker.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import riichi.mahjong_tracker.domain.Peli;
import riichi.mahjong_tracker.domain.Tulos;
import riichi.mahjong_tracker.repository.PelaajaRepository;
import riichi.mahjong_tracker.repository.PeliRepository;
import riichi.mahjong_tracker.repository.TulosRepository;

@Controller 
@RequestMapping("/tulos")
public class TulosController {

  private final TulosRepository tulosRepository;
  private final PelaajaRepository pelaajaRepository;
  private final PeliRepository peliRepository;

  public TulosController(TulosRepository tulosRepository, PelaajaRepository pelaajaRepository,
      PeliRepository peliRepository) {
    this.tulosRepository = tulosRepository;
    this.pelaajaRepository = pelaajaRepository;
    this.peliRepository = peliRepository;
  }

  @GetMapping("/lista")
  public String TulosLista(Model model) {
    model.addAttribute("tulokset", tulosRepository.findAll());
    return "tulos/lista";
  }

  @GetMapping("/add")
  public String addTulos(@RequestParam Long peliId, Model model) {
    Peli peli = peliRepository.findById(peliId)
      .orElseThrow(() -> new RuntimeException("Peliä ei löytynyt"));
    model.addAttribute("tulos", new Tulos());
    model.addAttribute("pelaajat", pelaajaRepository.findAll());
    model.addAttribute("peli", peli);
    return "tulos/add";
  }

  @GetMapping("/edit/{id}")
  public String editTulos(@PathVariable("id") Long tulosId, Model model) {
    Tulos tulos = tulosRepository.findById(tulosId)
      .orElseThrow(() -> new RuntimeException("Tulosta ei löytynyt"));
    model.addAttribute("tulos", tulos);
    model.addAttribute("peli", tulos.getPeli());
    model.addAttribute("pelit", peliRepository.findAll());
    model.addAttribute("pelaajat", pelaajaRepository.findAll());
    return "tulos/edit";
  }

  @PostMapping("/save")
  //param: haetaan arvo URL-osoitteen parametrista tai 
  //lomakkeen lähettämästä parametrista
  public String saveTulos(@ModelAttribute Tulos tulos, 
                          @RequestParam Long peliId) {
    Peli peli = peliRepository.findById(peliId)
        .orElseThrow(() -> new RuntimeException("Peliä ei löytynyt"));  
    //jotta tallennettava tulos yhdistyy oikeaan peliin
    tulos.setPeli(peli);
    tulosRepository.save(tulos);
    return "redirect:/peli/" + peliId;
  }

  @GetMapping("/delete/{id}")
  public String deleteTulos(@PathVariable("id") Long tulosId, Model model) {
    //hakee poistettava tulos tietokannasta
    Tulos tulos = tulosRepository.findById(tulosId)
        .orElseThrow(() -> new RuntimeException("Tulosta ei löytynyt"));
    //talteen sen pelin ID, johon tulos kuuluu
    Long peliId = tulos.getPeli().getPeliId();
    tulosRepository.deleteById(tulosId);
    return "redirect:/peli/" + peliId;
  }
}

