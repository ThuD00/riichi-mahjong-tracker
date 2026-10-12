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

import riichi.mahjong_tracker.domain.Peli;
//import riichi.mahjong_tracker.repository.PaikkaRepository;
import riichi.mahjong_tracker.repository.PeliRepository;

@RestController 
@RequestMapping("/api/peli") 
public class PeliRestController {
  //private PaikkaRepository paikkaRepository;
  private PeliRepository peliRepository;

  public PeliRestController(
          PeliRepository peliRepository 
          //, PaikkaRepository paikkaRepository
          ) {
    this.peliRepository = peliRepository;
    //this.paikkaRepository = paikkaRepository;
  }

  @GetMapping
  //Alkiot läpi ja käsitella listan ominaisuuksi
  public List<Peli> findAllPelit() {
    return peliRepository.findAll();
  }

  @GetMapping("{id}")
  // 200 ok tai 404 not found
  public ResponseEntity<Peli> findById(
          @PathVariable("id") long peliId) {
    return peliRepository.findById(peliId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  //lisäys onnistunut ja 201 created
   public ResponseEntity<Peli> addPeliRest(@RequestBody Peli peli) {
    Peli uusiPeli = peliRepository.save(peli);
    //HttpStatus.CREATED = 201
    return ResponseEntity.status(HttpStatus.CREATED).body(uusiPeli);
   }

  @DeleteMapping("{id}")
  //204 no content tai 404 not found
  public ResponseEntity<Void> delPeli(@PathVariable("id") long peliId) {
    if (!peliRepository.existsById(peliId)) {
      return ResponseEntity.notFound().build();
    }
    peliRepository.deleteById(peliId);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("{id}")
  //muokkaus onnistunut tai 404 not found
  public ResponseEntity<Peli> updatePeli(
    @PathVariable("id") long peliId,
    @RequestBody Peli uusipeli) {
      if (!peliRepository.existsById(peliId)) {
      return ResponseEntity.notFound().build();
    }
      uusipeli.setPeliId(peliId);
      return ResponseEntity.ok(peliRepository.save(uusipeli));
    }
}
