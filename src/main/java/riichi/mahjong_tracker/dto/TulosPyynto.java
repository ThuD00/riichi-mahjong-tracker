package riichi.mahjong_tracker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TulosPyynto {

  @NotNull 
  private Long pelaajaId;
  
  @NotNull 
  private Long peliId;

  @NotNull 
  private Integer pisteet;

  @NotNull 
  @Min(1)
  @Max(4)
  private Integer sijoitus;

  @NotNull 
  private Integer umat;

  public TulosPyynto(Long pelaajaId, Long peliId, Integer pisteet, 
    Integer sijoitus, Integer umat) {
    this.pelaajaId = pelaajaId;
    this.peliId = peliId;
    this.pisteet = pisteet;
    this.sijoitus = sijoitus;
    this.umat = umat;
  }

  public TulosPyynto() {
    
  }

  public Long getPelaajaId() {
    return pelaajaId;
  }

  public void setPelaajaId(Long pelaajaId) {
    this.pelaajaId = pelaajaId;
  }

  public Long getPeliId() {
    return peliId;
  }

  public void setPeliId(Long peliId) {
    this.peliId = peliId;
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

  public void setUmas(Integer umat) {
    this.umat = umat;
  }

  @Override
  public String toString() {
    return "TulosPyynto [pelaajaId=" + pelaajaId + ", peliId=" + peliId + ", pisteet=" + pisteet + ", sijoitus="
        + sijoitus + ", umat=" + umat + "]";
  }
  
}
