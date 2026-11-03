package riichi.mahjong_tracker.dto;

public class PeliPyynto {
  
  private Long paikkaId;

  public PeliPyynto(Long paikkaId) {
    this.paikkaId = paikkaId;
  }

  public PeliPyynto() {
    
  }

  public Long getPaikkaId() {
    return paikkaId;
  }

  public void setPaikkaId(Long paikkaId) {
    this.paikkaId = paikkaId;
  }

  @Override
  public String toString() {
    return "PeliPyynto [paikkaId=" + paikkaId + "]";
  }

}
