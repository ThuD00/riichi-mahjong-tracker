package riichi.mahjong_tracker.web;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import riichi.mahjong_tracker.domain.Paikka;
import riichi.mahjong_tracker.repository.PaikkaRepository;

@RestController 
@RequestMapping("/api/paikat") 
public class PaikkaRestController {
  private PaikkaRepository paikkaRepository;

  public PaikkaRestController(PaikkaRepository paikkaRepository) {
    this.paikkaRepository = paikkaRepository;
  }

  @GetMapping
  //Iterable?
  public @ResponseBody List<Paikka> findAllPaikat() {
    //(List<Paikka>)?
    return paikkaRepository.findAll();
  }

  @GetMapping("{id}")
  //ResponseEntity?
  public @ResponseBody Optional<Paikka> findById(
          @PathVariable("id") long paikkaId) {
    //Optional?
    return paikkaRepository.findById(paikkaId);
  }

  @PostMapping
   public @ResponseBody Paikka savePaikkaRest(@RequestBody Paikka paikka) {
    return paikkaRepository.save(paikka);
   }
}
