package riichi.mahjong_tracker;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import riichi.mahjong_tracker.domain.Paikka;
import riichi.mahjong_tracker.domain.Pelaaja;
import riichi.mahjong_tracker.domain.Peli;
import riichi.mahjong_tracker.domain.Tulos;
import riichi.mahjong_tracker.repository.PaikkaRepository;
import riichi.mahjong_tracker.repository.PelaajaRepository;
import riichi.mahjong_tracker.repository.PeliRepository;
import riichi.mahjong_tracker.repository.TulosRepository;

@DataJpaTest 
public class TulosRepoTest {

  @Autowired 
  private PeliRepository peliRepository;

  @Autowired 
  private PaikkaRepository paikkaRepository;

  @Autowired 
  private PelaajaRepository pelaajaRepository;

  @Autowired 
  private TulosRepository tulosRepository;

  @Test 
  public void createTulos() {
    Paikka paikka = paikkaRepository.save(
        new Paikka("Testipaikka", "Helsinki"));
    Peli peli = peliRepository.save(
        new Peli(LocalDateTime.now(), paikka));
    Pelaaja pelaaja = pelaajaRepository.save(
        new Pelaaja("Testipelaaja", "Club", 12345));
    Tulos tulos = new Tulos(25000, 1, 15000, pelaaja, peli);
    Tulos tallennettu = tulosRepository.save(tulos);

    assertThat(tallennettu.getTulosId()).isNotNull();
    assertThat(tulosRepository.findById(tallennettu.getTulosId())).isPresent();
    assertThat(tallennettu.getPisteet()).isEqualTo(25000);
    assertThat(tallennettu.getSijoitus()).isEqualTo(1);
    assertThat(tallennettu.getUmat()).isEqualTo(15000);
  }

  @Test 
  public void searchTulosById() {
    Paikka paikka = paikkaRepository.save(
        new Paikka("Testipaikka", "Helsinki"));
    Peli peli = peliRepository.save(
        new Peli(LocalDateTime.now(), paikka));
    Pelaaja pelaaja = pelaajaRepository.save(
        new Pelaaja("Testipelaaja", "Club", 12345));
    Tulos tulos = tulosRepository.save(
        new Tulos(25000, 1, 15000, pelaaja, peli));

    assertThat(tulosRepository.findById(tulos.getTulosId())).isPresent();
    assertThat(tulosRepository.findById(tulos.getTulosId())
        .get().getPelaaja().getKutsumaNimi()).isEqualTo("Testipelaaja");
    assertThat(tulosRepository.findById(tulos.getTulosId())
        .get().getPeli().getPeliId()).isEqualTo(peli.getPeliId());
  }

  @Test 
  public void editTulos() {
    Paikka paikka = paikkaRepository.save(
        new Paikka("Testipaikka", "Helsinki"));
    Peli peli = peliRepository.save(
        new Peli(LocalDateTime.now(), paikka));
    Pelaaja pelaaja = pelaajaRepository.save(
        new Pelaaja("Testipelaaja", "Club", 12345));
    Pelaaja pelaaja2 = pelaajaRepository.save(
        new Pelaaja("Testipelaaja2", "Club", 12345));
    Tulos tulos = tulosRepository.save(
        new Tulos(25000, 1, 15000, pelaaja, peli));

    tulos.setPisteet(30000);
    tulos.setSijoitus(2);
    tulos.setUmat(5000);
    tulos.setPelaaja(pelaaja2);
    tulosRepository.save(tulos);
    
    assertThat(tulosRepository.findById(tulos.getTulosId())).isPresent();
    assertThat(tulosRepository.findById(tulos.getTulosId())
        .get().getPisteet()).isEqualTo(30000);
    assertThat(tulosRepository.findById(tulos.getTulosId())
        .get().getSijoitus()).isEqualTo(2);
    assertThat(tulosRepository.findById(tulos.getTulosId())
        .get().getUmat()).isEqualTo(5000);
    assertThat(tulosRepository.findById(tulos.getTulosId())
        .get().getPelaaja().getKutsumaNimi()).isEqualTo("Testipelaaja2");
  }

  @Test
  void deleteTulos() {
    Paikka paikka = paikkaRepository.save(
        new Paikka("Testipaikka", "Helsinki"));
    Peli peli = peliRepository.save(
        new Peli(LocalDateTime.now(), paikka));
    Pelaaja pelaaja = pelaajaRepository.save(
        new Pelaaja("Testipelaaja", "Club", 12345));
    Tulos tulos = tulosRepository.save(
        new Tulos(25000, 1, 15000, pelaaja, peli));

    tulosRepository.deleteById(tulos.getTulosId());

    assertThat(tulosRepository.findById(tulos.getTulosId())).isEmpty();
  }
}
