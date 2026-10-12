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

import riichi.mahjong_tracker.domain.Pelaaja;
import riichi.mahjong_tracker.repository.PelaajaRepository;

@RestController 
@RequestMapping("/api/pelaaja")
public class PelaajaRestController {
  private PelaajaRepository pelaajaRepository;

  public PelaajaRestController(PelaajaRepository pelaajaRepository) {
    this.pelaajaRepository = pelaajaRepository;
  }

  @GetMapping
  //Alkiot läpi ja käsitella listan ominaisuuksi
  public List<Pelaaja> findAllPelaaja() {
    return pelaajaRepository.findAll();
  }

  @GetMapping("{id}")
  // 200 ok tai 404 not found
  public ResponseEntity<Pelaaja> findById(
          @PathVariable("id") long pelaajaId) {
    return pelaajaRepository.findById(pelaajaId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  //lisäys onnistunut ja 201 created
   public ResponseEntity<Pelaaja> addPelaajaRest(@RequestBody Pelaaja pelaaja) {
    Pelaaja uusiPelaaja = pelaajaRepository.save(pelaaja);
    //HttpStatus.CREATED = 201
    return ResponseEntity.status(HttpStatus.CREATED).body(uusiPelaaja);
   }

  @DeleteMapping("{id}")
  //204 no content tai 404 not found
  public ResponseEntity<Void> delPelaaja(@PathVariable("id") long pelaajaId) {
    if (!pelaajaRepository.existsById(pelaajaId)) {
      return ResponseEntity.notFound().build();
    }
    pelaajaRepository.deleteById(pelaajaId);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("{id}")
  //muokkaus onnistunut tai 404 not found
  public ResponseEntity<Pelaaja> updatePelaaja(
    @PathVariable("id") long pelaajaId,
    @RequestBody Pelaaja uusipelaaja) {
      if (!pelaajaRepository.existsById(pelaajaId)) {
      return ResponseEntity.notFound().build();
    }
      uusipelaaja.setPelaajaId(pelaajaId);
      return ResponseEntity.ok(pelaajaRepository.save(uusipelaaja));
    }
}
