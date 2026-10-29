package riichi.mahjong_tracker.dto;

public class PelaajaTilasto {

  private String kutsumaNimi;
  private long pelienMaara;
  private long voittojenMaara;
  private double keskiarvoSijoitus;
  private double keskiarvoPisteet;
  private double keskiarvoUmat;
  private Integer suurinPistemäärä;
  private long pisteetYht;

  public PelaajaTilasto () {
    
  }
  
  public PelaajaTilasto(
          String kutsumaNimi, 
          long pelienMaara, 
          long voittojenMaara, 
          double keskiarvoSijoitus,
          double keskiarvoPisteet, 
          double keskiarvoUmat, 
          Integer suurinPistemäärä, 
          long pisteetYht) {
            
    this.kutsumaNimi = kutsumaNimi;
    this.pelienMaara = pelienMaara;
    this.voittojenMaara = voittojenMaara;
    this.keskiarvoSijoitus = keskiarvoSijoitus;
    this.keskiarvoPisteet = keskiarvoPisteet;
    this.keskiarvoUmat = keskiarvoUmat;
    this.suurinPistemäärä = suurinPistemäärä;
    this.pisteetYht = pisteetYht;
  }

  public String getKutsumaNimi() {
    return kutsumaNimi;
  }

  public void setKutsumaNimi(String kutsumaNimi) {
    this.kutsumaNimi = kutsumaNimi;
  }

  public long getPelienMaara() {
    return pelienMaara;
  }

  public void setPelienMaara(long pelienMaara) {
    this.pelienMaara = pelienMaara;
  }

  public long getVoittojenMaara() {
    return voittojenMaara;
  }

  public void setVoittojenMaara(long voittojenMaara) {
    this.voittojenMaara = voittojenMaara;
  }

  public double getKeskiarvoSijoitus() {
    return keskiarvoSijoitus;
  }

  public void setKeskiarvoSijoitus(double keskiarvoSijoitus) {
    this.keskiarvoSijoitus = keskiarvoSijoitus;
  }

  public double getKeskiarvoPisteet() {
    return keskiarvoPisteet;
  }

  public void setKeskiarvoPisteet(double keskiarvoPisteet) {
    this.keskiarvoPisteet = keskiarvoPisteet;
  }

  public double getKeskiarvoUmat() {
    return keskiarvoUmat;
  }

  public void setKeskiarvoUmat(double keskiarvoUmat) {
    this.keskiarvoUmat = keskiarvoUmat;
  }

  public Integer getSuurinPistemäärä() {
    return suurinPistemäärä;
  }

  public void setSuurinPistemäärä(Integer suurinPistemäärä) {
    this.suurinPistemäärä = suurinPistemäärä;
  }

  public long getPisteetYht() {
    return pisteetYht;
  }

  public void setPisteetYht(long pisteetYht) {
    this.pisteetYht = pisteetYht;
  }

  @Override
  public String toString() {
    return "PelaajaTilasto [kutsumaNimi=" + kutsumaNimi + ", pelienMaara=" + pelienMaara + ", voittojenMaara="
        + voittojenMaara + ", keskiarvoSijoitus=" + keskiarvoSijoitus + ", keskiarvoPisteet=" + keskiarvoPisteet
        + ", keskiarvoUmat=" + keskiarvoUmat + ", suurinPistemäärä=" + suurinPistemäärä + ", pisteetYht=" + pisteetYht
        + "]";
  }

  

}
