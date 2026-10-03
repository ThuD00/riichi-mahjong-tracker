package riichi.mahjong_tracker.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class Tulokset {

  @Id 
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long tulosId;

  private Integer pisteet;

  private Integer sijoitus;

  private Integer umat;

  public Tulokset() {

  }

  public Tulokset(Integer pisteet, Integer sijoitus, Integer umat) {
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
    return "Tulokset [tulosId=" + tulosId + ", pisteet=" + pisteet + ", sijoitus=" + sijoitus + ", umat=" + umat + "]";
  }
  
}
