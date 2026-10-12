package riichi.mahjong_tracker.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
  //Alkiot läpi ja käsitella listan ominaisuuksi
  public List<Paikka> findAllPaikat() {
    return paikkaRepository.findAll();
  }

  @GetMapping("{id}")
  // 200 ok tai 404 not found
  public ResponseEntity<Paikka> findById(
          @PathVariable("id") long paikkaId) {
    return paikkaRepository.findById(paikkaId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  //lisäys onnistunut ja 201 created
   public ResponseEntity<Paikka> addPaikkaRest(@RequestBody Paikka paikka) {
    Paikka uusiPaikka = paikkaRepository.save(paikka);
    //HttpStatus.CREATED = 201
    return ResponseEntity.status(HttpStatus.CREATED).body(uusiPaikka);
   }

  @DeleteMapping("{id}")
  //204 no content tai 404 not found
  public ResponseEntity<Void> delPaikka(@PathVariable("id") long paikkaId) {
    if (!paikkaRepository.existsById(paikkaId)) {
      return ResponseEntity.notFound().build();
    }
    paikkaRepository.deleteById(paikkaId);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("{id}")
  //muokkaus onnistunut tai 404 not found
  public ResponseEntity<Paikka> updatePaikka(
    @PathVariable("id") long paikkaId,
    @RequestBody Paikka uusipaikka) {
      if (!paikkaRepository.existsById(paikkaId)) {
      return ResponseEntity.notFound().build();
    }
      uusipaikka.setPaikkaId(paikkaId);
      return ResponseEntity.ok(paikkaRepository.save(uusipaikka));
    }
}
