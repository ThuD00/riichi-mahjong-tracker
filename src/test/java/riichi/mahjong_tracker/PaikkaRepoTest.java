package riichi.mahjong_tracker;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import riichi.mahjong_tracker.domain.Paikka;
import riichi.mahjong_tracker.repository.PaikkaRepository;

@DataJpaTest 
public class PaikkaRepoTest {

  @Autowired 
  private PaikkaRepository paikkaRepository;

  @Test 
  public void createPaikka() {
    Paikka paikka = new Paikka("Testipaikka", "Helsinki");
    Paikka tallennettu = paikkaRepository.save(paikka);

    assertThat(tallennettu.getPaikkaId()).isNotNull();
    assertThat(paikkaRepository.findById(tallennettu.getPaikkaId())).isPresent();
    assertThat(tallennettu.getNimi()).isEqualTo("Testipaikka");
  }

  @Test 
  public void searchPaikkaById() {
    Paikka paikka = new Paikka("Nagashi", "Turku");
    Paikka tallennettu = paikkaRepository.save(paikka);
    Optional<Paikka> loydetty = 
            paikkaRepository.findById(tallennettu.getPaikkaId());

    assertThat(loydetty).isPresent();
    assertThat(loydetty.get().getNimi()).isEqualTo("Nagashi");
  }

  @Test 
  public void editPaikka() {
    Paikka paikka = new Paikka("Vanha nimi", "Helsinki");
    Paikka tallennettu = paikkaRepository.save(paikka);

    tallennettu.setNimi("Uusi nimi");
    tallennettu.setKaupunki("Vantaa");
    paikkaRepository.save(tallennettu);

    Optional<Paikka> muokattu = 
            paikkaRepository.findById(tallennettu.getPaikkaId());
    
    assertThat(muokattu).isPresent();
    assertThat(muokattu.get().getNimi()).isEqualTo("Uusi nimi");
    assertThat(muokattu.get().getKaupunki()).isEqualTo("Vantaa");
  }

  @Test
  void deletePaikka() {
    Paikka paikka = new Paikka("Poistettava paikka", "Helsinki");
    Paikka tallennettu = paikkaRepository.save(paikka);
    paikkaRepository.deleteById(tallennettu.getPaikkaId());

    assertThat(paikkaRepository.findById(tallennettu.getPaikkaId())).isEmpty();
  }

}
