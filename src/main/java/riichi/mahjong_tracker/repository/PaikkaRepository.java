package riichi.mahjong_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import riichi.mahjong_tracker.domain.Paikka;

public interface PaikkaRepository extends JpaRepository<Paikka, Long> {

}
