package riichi.mahjong_tracker;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import riichi.mahjong_tracker.domain.Pelaaja;
import riichi.mahjong_tracker.repository.PelaajaRepository;

@DataJpaTest 
public class PelaajaRepoTest {

  @Autowired 
  private PelaajaRepository pelaajaRepository;

  @Test 
  public void createPelaaja() {
    Pelaaja pelaaja = new Pelaaja("Testipelaaja", "Yama", 123000000);
    Pelaaja tallennettu = pelaajaRepository.save(pelaaja);

    assertThat(tallennettu.getPelaajaId()).isNotNull();
    assertThat(pelaajaRepository.findById(tallennettu.getPelaajaId())).isPresent();
    assertThat(tallennettu.getKutsumaNimi()).isEqualTo("Testipelaaja");
  }

  @Test 
  public void searchPelaajaById() {
    Pelaaja pelaaja = new Pelaaja("Testipelaaja", "Yama", 123000000);
    Pelaaja tallennettu = pelaajaRepository.save(pelaaja);
    Optional<Pelaaja> loydetty = 
            pelaajaRepository.findById(tallennettu.getPelaajaId());

    assertThat(loydetty).isPresent();
    assertThat(loydetty.get().getKutsumaNimi()).isEqualTo("Testipelaaja");
  }

  @Test 
  public void editPelaaja() {
    Pelaaja pelaaja = new Pelaaja("Testipelaaja", "Yama", 123000000);
    Pelaaja tallennettu = pelaajaRepository.save(pelaaja);

    tallennettu.setKutsumaNimi("Uusi nimi");
    tallennettu.setSeura("Aalto");
    tallennettu.setEmaNro(321000000);
    pelaajaRepository.save(tallennettu);

    Optional<Pelaaja> muokattu = 
            pelaajaRepository.findById(tallennettu.getPelaajaId());
    
    assertThat(muokattu).isPresent();
    assertThat(muokattu.get().getKutsumaNimi()).isEqualTo("Uusi nimi");
    assertThat(muokattu.get().getSeura()).isEqualTo("Aalto");
    assertThat(muokattu.get().getEmaNro()).isEqualTo(321000000);
  }

  @Test
  void deletePaikka() {
    Pelaaja pelaaja = new Pelaaja("Testipelaaja", "Yama", 123000000);
    Pelaaja tallennettu = pelaajaRepository.save(pelaaja);
    pelaajaRepository.deleteById(tallennettu.getPelaajaId());

    assertThat( pelaajaRepository.findById(tallennettu.getPelaajaId())).isEmpty();
  }
}
