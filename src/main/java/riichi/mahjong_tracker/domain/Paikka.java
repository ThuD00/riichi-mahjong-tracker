package riichi.mahjong_tracker.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@JsonPropertyOrder({"paikkaId", "nimi", "kaupunki"})
@Entity 
public class Paikka {

  @Id 
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long paikkaId;

  @NotBlank
  private String nimi;
  
  @NotBlank
  private String kaupunki;

  @OneToMany(mappedBy = "paikka")
  @JsonIgnore 
  private List<Peli> pelit;

  public Paikka() {

  }

  public Paikka(String nimi, String kaupunki) {
    this.nimi = nimi;
    this.kaupunki = kaupunki;
  }

  public long getPaikkaId() {
    return paikkaId;
  }

  public void setPaikkaId(long paikkaId) {
    this.paikkaId = paikkaId;
  }

  public String getNimi() {
    return nimi;
  }

  public void setNimi(String nimi) {
    this.nimi = nimi;
  }

  public String getKaupunki() {
    return kaupunki;
  }

  public void setKaupunki(String kaupunki) {
    this.kaupunki = kaupunki;
  }

  @Override
  public String toString() {
    return "Paikka [paikkaId=" + paikkaId + ", nimi=" + nimi + ", kaupunki=" + kaupunki + "]";
  }
  
}
