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
import riichi.mahjong_tracker.domain.Paikka;
import riichi.mahjong_tracker.repository.PaikkaRepository;

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

@WebMvcTest(PaikkaRestController.class)
@Import(WebSecurityConfig.class)
@ActiveProfiles("test")
public class PaikkaRestControllerTest {

  private static final String BASE_URL = "/api/paikka";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PaikkaRepository paikkaRepository;

  @Test
  void findAllPaikat() throws Exception {
    //määrittää: mitä mockattu repository palauttaa
    when(paikkaRepository.findAll()).thenReturn(Collections.emptyList());
    //lähettää GET-pyynnön kontrollerille
    mockMvc.perform(get(BASE_URL))
      .andExpect(status().isOk()) 
      //tarkistaa et vastaus on JSON-muodossa
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      //tarkistaa et palautettu JSON on taulukko eli lista
      .andExpect(jsonPath("$").isArray());
}
  @Test
  void searchPaikkaById() throws Exception {
    Paikka paikka = new Paikka("Testipaikka", "Helsinki");

    when(paikkaRepository.findById(1L)).thenReturn(Optional.of(paikka));

    mockMvc.perform(get(BASE_URL + "/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.nimi").value("Testipaikka"))
      .andExpect(jsonPath("$.kaupunki").value("Helsinki"));
  }

  @Test
  void searchPaikkaByIdNotFound() throws Exception {
    when(paikkaRepository.findById(99L))
      .thenReturn(Optional.empty());

    mockMvc.perform(get(BASE_URL + "/99"))
      .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  void createPaikka() throws Exception {
    Paikka paikka = new Paikka("Testipaikka", "Helsinki");

    when(paikkaRepository.save(any(Paikka.class)))
      .thenReturn(paikka);

    mockMvc.perform(post(BASE_URL)
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "nimi": "Testipaikka",
            "kaupunki": "Helsinki"
          }
          """))
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.nimi").value("Testipaikka"))
      .andExpect(jsonPath("$.kaupunki").value("Helsinki"));
  }

  @Test
  @WithMockUser
  void createPaikkaBadRequest() throws Exception {
    mockMvc.perform(post(BASE_URL)
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "nimi": "",
            "kaupunki": "Helsinki"
          }
          """))
      .andExpect(status().isBadRequest());
      //varmistaa virheellistä ei tallenneta TK:n
      verify(paikkaRepository, never()).save(any(Paikka.class));
  }

  @Test
  @WithMockUser
  void updatePaikka() throws Exception {
    Paikka paikka = new Paikka("Vanha nimi", "Helsinki");
    //löytyykö tietokannasta -> palauttaa true, jotta päivitys voi jatkua
    when(paikkaRepository.existsById(1L))
      .thenReturn(true);
    when(paikkaRepository.findById(1L))
      .thenReturn(Optional.of(paikka));
    when(paikkaRepository.save(any(Paikka.class)))
      //sisältää tiedot siitä, mitä mockatulle metodille annettiin
      //palauttaa 1. parametrin eli tallennettavan Paikka-olion
      .thenAnswer(invocation -> invocation.getArgument(0));

    mockMvc.perform(put(BASE_URL + "/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "nimi": "Uusi nimi",
            "kaupunki": "Tampere"
          }
        """))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.nimi").value("Uusi nimi"))
      .andExpect(jsonPath("$.kaupunki").value("Tampere"));
  }

  @Test
  @WithMockUser
  void updatePaikkaBadRequest() throws Exception {
    mockMvc.perform(put(BASE_URL + "/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "nimi": "Testi",
            "kaupunki": ""
          }
          """))
      .andExpect(status().isBadRequest());
      verify(paikkaRepository, never()).save(any(Paikka.class));
  }

  @Test
  @WithMockUser
  void updatePaikkaNotFound() throws Exception {
    when(paikkaRepository.existsById(99L))
      .thenReturn(false);

    mockMvc.perform(put(BASE_URL + "/99")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "nimi": "Uusi paikka",
            "kaupunki": "Helsinki"
          }
          """))
      .andExpect(status().isNotFound());
      verify(paikkaRepository, never()).save(any(Paikka.class));
  }

  @Test
  @WithMockUser
  void deletePaikka() throws Exception {
    when(paikkaRepository.existsById(1L))
      .thenReturn(true);

    mockMvc.perform(delete(BASE_URL + "/1"))
      .andExpect(status().isNoContent());

    verify(paikkaRepository).deleteById(1L);
  }

  //Paikkaa ei löydy -> 404 NotFound
  @Test
  @WithMockUser
  void deletePaikkaNotFound() throws Exception {
    when(paikkaRepository.existsById(99L))
      .thenReturn(false);

    mockMvc.perform(delete(BASE_URL + "/99"))
      .andExpect(status().isNotFound());
    //-> poistamista ei yritetä suorittaa
    verify(paikkaRepository, never()).deleteById(anyLong());
  }
}
