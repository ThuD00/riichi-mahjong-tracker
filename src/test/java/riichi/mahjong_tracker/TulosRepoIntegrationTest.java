package riichi.mahjong_tracker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import riichi.mahjong_tracker.domain.Paikka;
import riichi.mahjong_tracker.domain.Pelaaja;
import riichi.mahjong_tracker.domain.Peli;
import riichi.mahjong_tracker.domain.Tulos;
import riichi.mahjong_tracker.repository.PaikkaRepository;
import riichi.mahjong_tracker.repository.PelaajaRepository;
import riichi.mahjong_tracker.repository.PeliRepository;
import riichi.mahjong_tracker.repository.TulosRepository;

@DataJpaTest 
@ActiveProfiles("test")
public class TulosRepoIntegrationTest {

  @Autowired
  private PaikkaRepository paikkaRepository;

  @Autowired
  private PeliRepository peliRepository;

  @Autowired
  private PelaajaRepository pelaajaRepository;

  @Autowired
  private TulosRepository tulosRepository;

  @Test
  void tulosRelationships() {
    //Luodaan ja tallennetaan pelipaikka
    Paikka paikka = new Paikka();
    paikka.setNimi("Testipaikka");
    paikka.setKaupunki("Helsinki");
    paikka = paikkaRepository.save(paikka);

    //Luodaan peli ja liitetään se pelipaikkaan
    Peli peli = new Peli();
    peli.setPvm(LocalDateTime.now());
    peli.setPaikka(paikka);
    peli = peliRepository.save(peli);

    //Luodaan pelaaja
    Pelaaja pelaaja = new Pelaaja();
    pelaaja.setKutsumaNimi("Testaaja");
    pelaaja = pelaajaRepository.save(pelaaja);

    //Luodaan tulos ja liitetään se peliin sekä pelaajaan
    Tulos tulos = new Tulos();
    tulos.setPisteet(25000);
    tulos.setSijoitus(1);
    tulos.setUmat(15);
    tulos.setPeli(peli);
    tulos.setPelaaja(pelaaja);
    tulos = tulosRepository.save(tulos);

    //Haetaan tulos TK:sta ID:n perusteella
    Optional<Tulos> haettuTulos = tulosRepository.findById(tulos.getTulosId());

    // Tarkistetaan et tulos ja sen suhteet löytyvät
    assertTrue(haettuTulos.isPresent());
    assertEquals("Testipaikka", haettuTulos.get().getPeli().getPaikka().getNimi());
    assertEquals("Testaaja", haettuTulos.get().getPelaaja().getKutsumaNimi());
  }
}
