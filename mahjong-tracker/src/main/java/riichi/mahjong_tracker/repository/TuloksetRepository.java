package riichi.mahjong_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import riichi.mahjong_tracker.domain.Tulokset;

public interface TuloksetRepository extends JpaRepository<Tulokset, Long> {

}
