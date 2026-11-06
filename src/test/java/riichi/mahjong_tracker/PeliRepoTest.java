package riichi.mahjong_tracker;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import riichi.mahjong_tracker.domain.Paikka;
import riichi.mahjong_tracker.domain.Peli;
import riichi.mahjong_tracker.repository.PaikkaRepository;
import riichi.mahjong_tracker.repository.PeliRepository;

@DataJpaTest 
public class PeliRepoTest {

  @Autowired 
  private PeliRepository peliRepository;

  @Autowired 
  private PaikkaRepository paikkaRepository;

  @Test 
  public void createPeli() {
    Paikka paikka = new Paikka("Testipaikka", "Helsinki");
    Paikka tallennettuPaikka = paikkaRepository.save(paikka);

    Peli peli = new Peli(LocalDateTime.now(), tallennettuPaikka);
    Peli tallennettuPeli = peliRepository.save(peli);

    assertThat(peliRepository.findById(tallennettuPeli.getPeliId())).isPresent();
    assertThat(tallennettuPeli.getPvm()).isNotNull();
    assertThat(tallennettuPeli.getPaikka().getNimi()).isEqualTo("Testipaikka");
  }

  @Test 
  public void searchPeliById() {
    Paikka paikka = paikkaRepository.save(
        new Paikka("Nagashi", "Turku"));
    Peli peli = peliRepository.save(
        new Peli(LocalDateTime.now(), paikka));

    assertThat(peliRepository.findById(peli.getPeliId())).isPresent();
    assertThat(peliRepository.findById(peli.getPeliId())
        .get().getPaikka().getNimi()).isEqualTo("Nagashi");
    assertThat(peliRepository.findById(peli.getPeliId())
        .get().getPaikka().getKaupunki()).isEqualTo("Turku");
  }

  @Test 
  public void editPeli() {
    Paikka paikka = paikkaRepository.save(
        new Paikka("Vanha paikka", "Turku"));
    Paikka paikka2 = paikkaRepository.save(
        new Paikka("Uusi paikka", "Helsinki"));
    Peli peli = peliRepository.save(
        new Peli(LocalDateTime.now(), paikka));

    peli.setPaikka(paikka2);
    peliRepository.save(peli);
    
    assertThat(peliRepository.findById(peli.getPeliId())).isPresent();
    assertThat(peliRepository.findById(peli.getPeliId())
        .get().getPaikka().getNimi()).isEqualTo("Uusi paikka");
    assertThat(peliRepository.findById(peli.getPeliId())
        .get().getPaikka().getKaupunki()).isEqualTo("Helsinki");
  }

  @Test
  void deletePeli() {
    Paikka paikka = paikkaRepository.save(
        new Paikka("Poistettava paikka", "Turku"));
    Peli peli = peliRepository.save(
        new Peli(LocalDateTime.now(), paikka));

    peliRepository.deleteById(peli.getPeliId());

    assertThat(peliRepository.findById(peli.getPeliId())).isEmpty();
  }
}
