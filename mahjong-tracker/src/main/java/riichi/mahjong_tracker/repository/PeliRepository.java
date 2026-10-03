package riichi.mahjong_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import riichi.mahjong_tracker.domain.Peli;

public interface PeliRepository extends JpaRepository<Peli, Long> {

}
