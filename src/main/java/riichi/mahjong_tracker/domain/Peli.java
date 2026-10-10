package riichi.mahjong_tracker.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class Peli {

  @Id 
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long peliId;

  private LocalDateTime pvm;
  
  public Peli() {

  }

  public Peli(LocalDateTime pvm) {
    this.pvm = pvm;
  }

  public long getPeliId() {
    return peliId;
  }

  public void setPeliId(long peliId) {
    this.peliId = peliId;
  }

  public LocalDateTime getPvm() {
    return pvm;
  }

  public void setPvm(LocalDateTime pvm) {
    this.pvm = pvm;
  }

  @Override
  public String toString() {
    return "Peli [peliId=" + peliId + ", pvm=" + pvm + "]";
  }

}
