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

import riichi.mahjong_tracker.domain.Tulos;
import riichi.mahjong_tracker.repository.TulosRepository;

@RestController 
@RequestMapping("/api/tulos")
public class TulosRestController {
  private TulosRepository tulosRepository;

  public TulosRestController(TulosRepository tulosRepository) {
    this.tulosRepository = tulosRepository;
  }

  @GetMapping
  //Alkiot läpi ja käsitella listan ominaisuuksi
  public List<Tulos> findAllTulos() {
    return tulosRepository.findAll();
  }

  @GetMapping("{id}")
  // 200 ok tai 404 not found
  public ResponseEntity<Tulos> findById(
          @PathVariable("id") long tulosId) {
    return tulosRepository.findById(tulosId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  //lisäys onnistunut ja 201 created
   public ResponseEntity<Tulos> addTulosRest(@RequestBody Tulos tulos) {
    Tulos uusiTulos = tulosRepository.save(tulos);
    //HttpStatus.CREATED = 201
    return ResponseEntity.status(HttpStatus.CREATED).body(uusiTulos);
   }

  @DeleteMapping("{id}")
  //204 no content tai 404 not found
  public ResponseEntity<Void> delPelaaja(@PathVariable("id") long tulosId) {
    if (!tulosRepository.existsById(tulosId)) {
      return ResponseEntity.notFound().build();
    }
    tulosRepository.deleteById(tulosId);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("{id}")
  //muokkaus onnistunut tai 404 not found
  public ResponseEntity<Tulos> updateTulos(
    @PathVariable("id") long tulosId,
    @RequestBody Tulos uusitulos) {
      if (!tulosRepository.existsById(tulosId)) {
      return ResponseEntity.notFound().build();
    }
      uusitulos.setTulosId(tulosId);
      return ResponseEntity.ok(tulosRepository.save(uusitulos));
    }
}
