package riichi.mahjong_tracker.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity 
public class Tulos {

  @Id 
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long tulosId;
  private Integer pisteet;
  private Integer sijoitus;
  private Integer umat;

  @ManyToOne
  @JsonIgnoreProperties("tulokset")
  @JoinColumn(name = "peliId")
  private Peli peli;

  @ManyToOne
  @JsonIgnoreProperties("tulokset")
  @JoinColumn(name = "pelaajaId")
  private Pelaaja pelaaja;

  public Tulos() {

  }

  public Tulos(Integer pisteet, Integer sijoitus, Integer umat) {
    this.pisteet = pisteet;
    this.sijoitus = sijoitus;
    this.umat = umat;
  }

  public long getTulosId() {
    return tulosId;
  }

  public void setTulosId(long tulosId) {
    this.tulosId = tulosId;
  }

  public Integer getPisteet() {
    return pisteet;
  }

  public void setPisteet(Integer pisteet) {
    this.pisteet = pisteet;
  }

  public Integer getSijoitus() {
    return sijoitus;
  }

  public void setSijoitus(Integer sijoitus) {
    this.sijoitus = sijoitus;
  }

  public Integer getUmat() {
    return umat;
  }

  public void setUmat(Integer umat) {
    this.umat = umat;
  }

  @Override
  public String toString() {
    return "Tulos [tulosId=" + tulosId + ", pisteet=" + pisteet + ", sijoitus=" + sijoitus + ", umat=" + umat + "]";
  }
  
}
