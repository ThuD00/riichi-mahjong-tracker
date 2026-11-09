package riichi.mahjong_tracker.web;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import riichi.mahjong_tracker.WebSecurityConfig;
import riichi.mahjong_tracker.domain.Pelaaja;
import riichi.mahjong_tracker.repository.PelaajaRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PelaajaRestController.class)
@Import(WebSecurityConfig.class)
@ActiveProfiles("test")
public class PelaajaRestControllerTest {

  private static final String BASE_URL = "/api/pelaaja";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PelaajaRepository pelaajaRepository;

  @Test
  void findAllPelaajat() throws Exception {
    //määrittää: mitä mockattu repository palauttaa
    when(pelaajaRepository.findAll()).thenReturn(Collections.emptyList());
    //lähettää GET-pyynnön kontrollerille
    mockMvc.perform(get(BASE_URL))
      .andExpect(status().isOk()) 
      //tarkistaa et vastaus on JSON-muodossa
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      //tarkistaa et palautettu JSON on taulukko eli lista
      .andExpect(jsonPath("$").isArray());
}
  @Test
  void searchPelaajaById() throws Exception {
    Pelaaja pelaaja = new Pelaaja("Testipelaaja", "Testi", 1242494245);

    when(pelaajaRepository.findById(1L)).thenReturn(Optional.of(pelaaja));

    mockMvc.perform(get(BASE_URL + "/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.kutsumaNimi").value("Testipelaaja"))
      .andExpect(jsonPath("$.seura").value("Testi"))
      .andExpect(jsonPath("$.emaNro").value(1242494245));
  }

  @Test
  void searchPelaajaByIdNotFound() throws Exception {
    when(pelaajaRepository.findById(99L))
      .thenReturn(Optional.empty());

    mockMvc.perform(get(BASE_URL + "/99"))
      .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  void createPelaaja() throws Exception {
    Pelaaja pelaaja = new Pelaaja("Testipelaaja", "Testi", 1242494245);

    when(pelaajaRepository.save(any(Pelaaja.class)))
      .thenReturn(pelaaja);

    mockMvc.perform(post(BASE_URL)
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "kutsumaNimi": "Testipelaaja",
            "seura": "Testi",
            "emaNro": 1242494245
          }
          """))
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.kutsumaNimi").value("Testipelaaja"))
      .andExpect(jsonPath("$.seura").value("Testi"))
      .andExpect(jsonPath("$.emaNro").value(1242494245));
  }

  @Test
  @WithMockUser
  void createPelaajaBadRequest() throws Exception {
    mockMvc.perform(post(BASE_URL)
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "kutsumaNimi": "TamaOnLiianPitkaKutsumaNimi",
            "seura": "Testi",
            "emaNro": 1242494245
          }
          """))
      .andExpect(status().isBadRequest());
      //varmistaa virheellistä ei tallenneta TK:n
      verify(pelaajaRepository, never()).save(any(Pelaaja.class));
  }

  @Test
  @WithMockUser
  void updatePelaaja() throws Exception {
    Pelaaja pelaaja = new Pelaaja("Testipelaaja", "Testi", 1242494245);
    //löytyykö tietokannasta -> palauttaa true, jotta päivitys voi jatkua
    when(pelaajaRepository.existsById(1L))
      .thenReturn(true);
    when(pelaajaRepository.findById(1L))
      .thenReturn(Optional.of(pelaaja));
    when(pelaajaRepository.save(any(Pelaaja.class)))
      //sisältää tiedot siitä, mitä mockatulle metodille annettiin
      //palauttaa 1. parametrin eli tallennettavan Pelaaja-olion
      .thenAnswer(invocation -> invocation.getArgument(0));

    mockMvc.perform(put(BASE_URL + "/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "kutsumaNimi": "Uusi pelaaja",
            "seura": "Testi",
            "emaNro": 1242494245
          }
        """))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.kutsumaNimi").value("Uusi pelaaja"))
      .andExpect(jsonPath("$.seura").value("Testi"))
      .andExpect(jsonPath("$.emaNro").value(1242494245));
  }

  @Test
  @WithMockUser
  void updatePelaajaBadRequest() throws Exception {
    mockMvc.perform(put(BASE_URL + "/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "kutsumaNimi": "",
            "seura": "Testi",
            "emaNro": 1242494245
          }
          """))
      .andExpect(status().isBadRequest());
      verify(pelaajaRepository, never()).save(any(Pelaaja.class));
  }

  @Test
  @WithMockUser
  void updatePelaajaNotFound() throws Exception {
    when(pelaajaRepository.findById(99L))
      .thenReturn(Optional.empty());

    mockMvc.perform(put(BASE_URL + "/99")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "kutsumaNimi": "Uusi nimi",
            "seura": "Testi",
            "emaNro": 1242494245
          }
          """))
      .andExpect(status().isNotFound());
      verify(pelaajaRepository, never()).save(any(Pelaaja.class));
  }

  @Test
  @WithMockUser
  void deletePelaaja() throws Exception {
    when(pelaajaRepository.existsById(1L))
      .thenReturn(true);

    mockMvc.perform(delete(BASE_URL + "/1"))
      .andExpect(status().isNoContent());

    verify(pelaajaRepository).deleteById(1L);
  }

  //Paikkaa ei löydy -> 404 NotFound
  @Test
  @WithMockUser
  void deletePelaajaNotFound() throws Exception {
    when(pelaajaRepository.existsById(99L))
      .thenReturn(false);

    mockMvc.perform(delete(BASE_URL + "/99"))
      .andExpect(status().isNotFound());
    //-> poistamista ei yritetä suorittaa
    verify(pelaajaRepository, never()).deleteById(anyLong());
  }
}
