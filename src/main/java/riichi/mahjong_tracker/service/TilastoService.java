package riichi.mahjong_tracker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import riichi.mahjong_tracker.domain.Pelaaja;
import riichi.mahjong_tracker.domain.Peli;
import riichi.mahjong_tracker.domain.Tulos;
import riichi.mahjong_tracker.dto.PelaajaTilasto;
import riichi.mahjong_tracker.repository.PaikkaRepository;
import riichi.mahjong_tracker.repository.PelaajaRepository;
import riichi.mahjong_tracker.repository.PeliRepository;
import riichi.mahjong_tracker.repository.TulosRepository;

@Service 
//lukee/hakee dataa ilman et muuttaa tietokantaa
@Transactional(readOnly = true)
public class TilastoService {

  private final PelaajaRepository pelaajaRepository;
  private final PaikkaRepository paikkaRepository;
  private final PeliRepository peliRepository;
  private final TulosRepository tulosRepository;
  
  public TilastoService(
          PelaajaRepository pelaajaRepository, 
          PaikkaRepository paikkaRepository,
          PeliRepository peliRepository, 
          TulosRepository tulosRepository) {

    this.pelaajaRepository = pelaajaRepository;
    this.paikkaRepository = paikkaRepository;
    this.peliRepository = peliRepository;
    this.tulosRepository = tulosRepository;
  }
  //jokaiselle pelaajille oma tilastolista
  public List<PelaajaTilasto> haePelaajaTilastot() {

    List<Pelaaja> pelaajat = pelaajaRepository.findAll();
    List<Tulos> kaikkiTulokset = tulosRepository.findAll();
    List<PelaajaTilasto> tilastot = new ArrayList<>();

    for (Pelaaja pelaaja : pelaajat) {
      //tuloslista virran avulla
      List<Tulos> tulokset = kaikkiTulokset.stream()
          //filter: vain ehdot täyttävät tuloset
          //tarkistaa et tulokselle on liitetty pelaaja
          .filter(t -> t.getPelaaja() != null
          //tuloksen pelaajan tunnus = käsiteltävän pelaajan tunnus
          && t.getPelaaja().getPelaajaId() 
          == pelaaja.getPelaajaId()).toList();
      
      //ohitus, jos ei ole tuloksia
      if (tulokset.isEmpty()) {
        continue;
      }

      long pelienMaara = tulokset.stream()
          //ottaa jokaisest tuloksest siihen liittyvä peli
          .map(Tulos::getPeli)
          //ohittaa joilta puuttuu peli
          .filter(Objects::nonNull)
          //ottaa peleist niiden tunnukset
          .map(Peli::getPeliId)
          //pois maholliset toistuvat pelit 
          //virheellisen tallennuksen vuoks
          .distinct()
          //laskee mont eri peliä jäi jäljel
          .count();
      
      long voittojenMaara = tulokset.stream()
          //valitsee tulokset jois sijoitus on 1. ja laskee ne 
          .filter(t -> t.getSijoitus() != null
            && t.getSijoitus() == 1).count();

      double keskiarvoSijoitus = tulokset.stream()
          .map(Tulos::getSijoitus)
          .filter(Objects::nonNull)
          //arvot kokonaisluvuksi
          .mapToInt(Integer::intValue)
          .average()
          //jos pelejä ei ole
          .orElse(0.0);

      double keskiarvoPisteet = tulokset.stream()
          .map(Tulos::getPisteet)
          .filter(Objects::nonNull)
          .mapToInt(Integer::intValue)
          .average()
          .orElse(0.0);

      double keskiarvoUmat = tulokset.stream()
          .map(Tulos::getUmat)
          .filter(Objects::nonNull)
          .mapToInt(Integer::intValue)
          .average()
          .orElse(0.0);

      Integer suurinPistemäärä = tulokset.stream()
          .map(Tulos::getPisteet)
          .filter(Objects::nonNull)
          .max(Integer::compareTo)
          .orElse(null);

      long pisteetYht = tulokset.stream()
          .map(Tulos::getPisteet)
          .filter(Objects::nonNull)
          .mapToLong(Integer::longValue)
          .sum();

      tilastot.add(new PelaajaTilasto(
          pelaaja.getKutsumaNimi(),
          pelienMaara,
          voittojenMaara,
          keskiarvoSijoitus,
          keskiarvoPisteet,
          keskiarvoUmat,
          suurinPistemäärä,
          pisteetYht
      ));
    }
    
    return tilastot;
  }

  public long haePelienMaara() {
    return peliRepository.count();
  }

  public long haePelaajienMaara() {
    return pelaajaRepository.count();
  }

  public long haePeliPaikkojenMaara() {
    return paikkaRepository.count();
  }

  public long haeTulostenMaara() {
    return tulosRepository.count();
  }
}
