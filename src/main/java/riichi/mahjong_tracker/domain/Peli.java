package riichi.mahjong_tracker.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity 
public class Peli {

  @Id 
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long peliId;
  private LocalDateTime pvm;

  @ManyToOne 
  @JsonIgnoreProperties("pelit")
  @JoinColumn(name = "paikkaId")
  private Paikka paikka;

  @OneToMany(mappedBy = "peli")
  @JsonIgnore 
  private List<Tulos> tulokset;
  
  public Peli() {

  }

  public Peli(LocalDateTime pvm, Paikka paikka) {
    this.pvm = pvm;
    this.paikka = paikka;
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

  public Paikka getPaikka() {
    return paikka;
  }

  public void setPaikka(Paikka paikka) {
    this.paikka = paikka;
  }

   public List<Tulos> getTulokset() {
    return tulokset;
  }

  public void setTulokset(List<Tulos> tulokset) {
    this.tulokset = tulokset;
  }

  @Override
  public String toString() {
    return "Peli [peliId=" + peliId + ", pvm=" + pvm + ", paikka=" + paikka + "]";
  }

}
