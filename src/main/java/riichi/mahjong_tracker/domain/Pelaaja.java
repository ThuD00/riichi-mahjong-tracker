package riichi.mahjong_tracker.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity 
public class Pelaaja {

  @Id 
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long pelaajaId; 
  private String kutsumaNimi;
  private String seura;
  private Integer emaNro;

  @OneToMany(mappedBy = "pelaaja")
  @JsonIgnore
  private List<Tulos> tulokset;
  
  public Pelaaja() {

  }

  public Pelaaja(String kutsumaNimi, String seura, Integer emaNro) {
    this.kutsumaNimi = kutsumaNimi;
    this.seura = seura;
    this.emaNro = emaNro;
  }

  public long getPelaajaId() {
    return pelaajaId;
  }

  public void setPelaajaId(long pelaajaId) {
    this.pelaajaId = pelaajaId;
  }

  public String getKutsumaNimi() {
    return kutsumaNimi;
  }

  public void setKutsumaNimi(String kutsumaNimi) {
    this.kutsumaNimi = kutsumaNimi;
  }

  public String getSeura() {
    return seura;
  }

  public void setSeura(String seura) {
    this.seura = seura;
  }

  public Integer getEmaNro() {
    return emaNro;
  }

  public void setEmaNro(Integer emaNro) {
    this.emaNro = emaNro;
  }

  @Override
  public String toString() {
    return "Pelaaja [pelaajaId=" + pelaajaId + ", kutsumaNimi=" + kutsumaNimi + ", seura=" + seura + ", emaNro="
        + emaNro + "]";
  }

}
