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

import jakarta.validation.Valid;
import riichi.mahjong_tracker.domain.Pelaaja;
import riichi.mahjong_tracker.domain.Peli;
import riichi.mahjong_tracker.domain.Tulos;
import riichi.mahjong_tracker.dto.TulosPyynto;
import riichi.mahjong_tracker.repository.PelaajaRepository;
import riichi.mahjong_tracker.repository.PeliRepository;
import riichi.mahjong_tracker.repository.TulosRepository;

@RestController 
@RequestMapping("/api/tulos")
public class TulosRestController {
  private TulosRepository tulosRepository;
  private PeliRepository peliRepository;
  private PelaajaRepository pelaajaRepository;
  
  public TulosRestController(TulosRepository tulosRepository, PeliRepository peliRepository,
      PelaajaRepository pelaajaRepository) {
    this.tulosRepository = tulosRepository;
    this.peliRepository = peliRepository;
    this.pelaajaRepository = pelaajaRepository;
  }

  @GetMapping
  //Alkiot läpi ja käsitellä listan ominaisuuksi
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
   public ResponseEntity<Tulos> addTulosRest(@Valid @RequestBody TulosPyynto pyynto) {
    Peli peli = peliRepository.findById(pyynto.getPeliId())
        .orElse(null);
    if (peli == null) {
      return ResponseEntity.notFound().build();
    }

    Pelaaja pelaaja = pelaajaRepository.findById(pyynto.getPelaajaId())
        .orElse(null);
    if (pelaaja == null) {
      return ResponseEntity.notFound().build();
    }

    Tulos tulos = new Tulos();
    //tiedot tulokseen
    tulos.setPisteet(pyynto.getPisteet());
    tulos.setSijoitus(pyynto.getSijoitus());
    tulos.setUmat(pyynto.getUmat());

    //pelaaja ja peli tulokseen
    tulos.setPeli(peli);
    tulos.setPelaaja(pelaaja);
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
    @Valid @RequestBody TulosPyynto pyynto) {

    Tulos tulos = tulosRepository.findById(tulosId)
        .orElse(null);
    if (tulos == null) {
      return ResponseEntity.notFound().build();
    }
    
    Peli peli = peliRepository.findById(pyynto.getPeliId())
        .orElse(null);
    if (peli == null) {
      return ResponseEntity.notFound().build();
    }

    Pelaaja pelaaja = pelaajaRepository.findById(pyynto.getPelaajaId())
        .orElse(null);
    if (pelaaja == null) {
      return ResponseEntity.notFound().build();
    }
    //tuloksen tiedon ja yhteyden päivitys
    tulos.setPisteet(pyynto.getPisteet());
    tulos.setSijoitus(pyynto.getSijoitus());
    tulos.setUmat(pyynto.getUmat());
    tulos.setPelaaja(pelaaja);
    tulos.setPeli(peli);

    return ResponseEntity.ok(tulosRepository.save(tulos));
  }
}
