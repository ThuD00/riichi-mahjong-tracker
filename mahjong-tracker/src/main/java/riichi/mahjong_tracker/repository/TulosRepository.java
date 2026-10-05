package riichi.mahjong_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import riichi.mahjong_tracker.domain.Tulos;

public interface TulosRepository extends JpaRepository<Tulos, Long> {

}
