package riichi.mahjong_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import riichi.mahjong_tracker.domain.Tulos;

public interface TulosRepository extends JpaRepository<Tulos, Long> {
  //tarkistaa mont tulosta pelis on
  long countByPeli_PeliId(Long peliId);
  //onko pelaaja jo lisätty kyseiseen peliin
  boolean existsByPeli_PeliIdAndPelaaja_PelaajaId(
    Long peliId, Long pelaajaId);
  //onko pelaaja jo lisätty kyseiseen 
  //peliin muun tuloksen kautta
  boolean existsByPeli_PeliIdAndPelaaja_PelaajaIdAndTulosIdNot(
    Long peliId, Long pelaajaId, Long tulosId);
}
