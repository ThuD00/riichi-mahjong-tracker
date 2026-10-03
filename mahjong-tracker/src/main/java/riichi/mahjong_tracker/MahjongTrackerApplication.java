package riichi.mahjong_tracker;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import riichi.mahjong_tracker.domain.Paikka;
import riichi.mahjong_tracker.domain.Pelaaja;
import riichi.mahjong_tracker.domain.Peli;
import riichi.mahjong_tracker.domain.Tulokset;
import riichi.mahjong_tracker.repository.PaikkaRepository;
import riichi.mahjong_tracker.repository.PelaajaRepository;
import riichi.mahjong_tracker.repository.PeliRepository;
import riichi.mahjong_tracker.repository.TuloksetRepository;

@SpringBootApplication
public class MahjongTrackerApplication {
  private static final Logger log = LoggerFactory.getLogger(MahjongTrackerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MahjongTrackerApplication.class, args);
	}

  @Bean 
  public CommandLineRunner demo(
    PaikkaRepository paikkaRepository,
    PelaajaRepository pelaajaRepository,
    PeliRepository peliRepository,
    TuloksetRepository tuloksetRepository
  ) {
    return (args) -> {
      log.info("Paikat");
      Paikka paikka1 = new Paikka("Tenpai", "Tampere");
      Paikka paikka2 = new Paikka("Yama", "Helsinki");
      Paikka paikka3 = new Paikka("Aalto Daigaku Maajanbu ry", "Espoo");
      paikkaRepository.save(paikka1);
      paikkaRepository.save(paikka2);
      paikkaRepository.save(paikka3);

      log.info("Pelaajat");
      Pelaaja pelaaja1 = new Pelaaja("Ray", "Aalto Daigaku Maajanbu ry", 12345678);
      Pelaaja pelaaja2 = new Pelaaja("Sami", "Tenpai", 103253852);
      Pelaaja pelaaja3 = new Pelaaja("Matti", "Yama", 123325353);
      pelaajaRepository.save(pelaaja1);
      pelaajaRepository.save(pelaaja2);
      pelaajaRepository.save(pelaaja3);

      log.info("Pelit");
      Peli peli1 = new Peli(LocalDateTime.of(2026, 10, 5, 14, 0));
      Peli peli2 = new Peli(LocalDateTime.of(2026, 10, 5, 17, 0));
      Peli peli3 = new Peli(LocalDateTime.of(2026, 10, 5, 19, 30));
      peliRepository.save(peli1);
      peliRepository.save(peli2);
      peliRepository.save(peli3);
      
      log.info("Tulokset");
      Tulokset tulos1 = new Tulokset(46100, 1, 15000);
      Tulokset tulos2 = new Tulokset(34600, 2, 5000);
      Tulokset tulos3 = new Tulokset(11300, 3, -5000);
      Tulokset tulos4 = new Tulokset(8000, 4, -15000);

      tuloksetRepository.save(tulos1);
      tuloksetRepository.save(tulos2);
      tuloksetRepository.save(tulos3);
      tuloksetRepository.save(tulos4);

      log.info("fetch paikat");
        for (Paikka paikka : paikkaRepository.findAll()) {
          log.info(paikka.toString());
        }
      log.info("fetch pelit");
        for (Peli peli : peliRepository.findAll()) {
          log.info(peli.toString());
        }
      log.info("fetch pelaajat");
        for (Pelaaja pelaaja : pelaajaRepository.findAll()) {
          log.info(pelaaja.toString());
        }
      log.info("fetch tulokset");
        for (Tulokset tulokset : tuloksetRepository.findAll()) {
          log.info(tulokset.toString());
        }
    };
  }

}
