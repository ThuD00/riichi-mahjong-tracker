package riichi.mahjong_tracker.web;

import java.time.LocalDateTime;
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

import jakarta.validation.Valid;
import riichi.mahjong_tracker.domain.Paikka;
import riichi.mahjong_tracker.domain.Peli;
import riichi.mahjong_tracker.dto.PeliPyynto;
import riichi.mahjong_tracker.repository.PaikkaRepository;
import riichi.mahjong_tracker.repository.PeliRepository;
import riichi.mahjong_tracker.repository.TulosRepository;

@RestController 
@RequestMapping("/api/peli") 
public class PeliRestController {
  private PaikkaRepository paikkaRepository;
  private PeliRepository peliRepository;

  public PeliRestController(PaikkaRepository paikkaRepository, TulosRepository tulosRepository,
      PeliRepository peliRepository) {
    this.paikkaRepository = paikkaRepository;
    this.peliRepository = peliRepository;
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
   public ResponseEntity<Peli> addPeliRest(@Valid @RequestBody PeliPyynto pyynto) {
    //paikan haku ID:n avul
    Paikka paikka = paikkaRepository.findById(pyynto.getPaikkaId())
        .orElseThrow(() -> new RuntimeException("Paikkaa ei löytynyt"));
    Peli peli = new Peli();
    peli.setPvm(LocalDateTime.now());
    //paikka peliin
    peli.setPaikka(paikka);
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
    @Valid @RequestBody PeliPyynto pyynto) {
      
      Peli peli = peliRepository.findById(peliId).orElse(null);
      if (peli == null) {
        return ResponseEntity.notFound().build();
      }

      Paikka paikka = paikkaRepository.findById(pyynto.getPaikkaId())
            .orElse(null);
        if (paikka == null) {
        return ResponseEntity.notFound().build();
      }
      
      peli.setPaikka(paikka);
      return ResponseEntity.ok(peliRepository.save(peli));
    }
}
