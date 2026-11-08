package riichi.mahjong_tracker.web;

import java.time.LocalDateTime;
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
import riichi.mahjong_tracker.domain.Peli;
import riichi.mahjong_tracker.repository.PaikkaRepository;
import riichi.mahjong_tracker.repository.PeliRepository;

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

@WebMvcTest(PeliRestController.class)
@Import(WebSecurityConfig.class)
@ActiveProfiles("test")
public class PeliRestControllerTest {

  private static final String BASE_URL = "/api/peli";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PeliRepository peliRepository;

  @MockitoBean
  private PaikkaRepository paikkaRepository;

  @Test
  void findAllPelit() throws Exception {
    //määrittää: mitä mockattu repository palauttaa
    when(peliRepository.findAll()).thenReturn(Collections.emptyList());
    //lähettää GET-pyynnön kontrollerille
    mockMvc.perform(get(BASE_URL))
      .andExpect(status().isOk()) 
      //tarkistaa et vastaus on JSON-muodossa
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      //tarkistaa et palautettu JSON on taulukko eli lista
      .andExpect(jsonPath("$").isArray());
  }
  
  @Test
  void searchPeliById() throws Exception {
    Paikka paikka = new Paikka("Testipaikka", "Helsinki");
    Peli peli = new Peli();
    peli.setPvm(LocalDateTime.now());
    peli.setPaikka(paikka);

    when(peliRepository.findById(1L)).thenReturn(Optional.of(peli));

    mockMvc.perform(get(BASE_URL + "/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.paikka.nimi").value("Testipaikka"));
  }

  @Test
  void searchPeliByIdNotFound() throws Exception {
    when(peliRepository.findById(99L))
      .thenReturn(Optional.empty());

    mockMvc.perform(get(BASE_URL + "/99"))
      .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  void createPeli() throws Exception {
    Paikka paikka = new Paikka("Testipaikka", "Helsinki");

    when(paikkaRepository.findById(1L))
      .thenReturn(Optional.of(paikka));

    when(peliRepository.save(any(Peli.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));

    mockMvc.perform(post(BASE_URL)
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "paikkaId": 1,
            "pvm": "2026-11-08T20:00:00"
          }
          """))
      .andExpect(status().isCreated())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.paikka.nimi").value("Testipaikka"));
  }

  @Test
  @WithMockUser
  void createPeliBadRequest() throws Exception {
    mockMvc.perform(post(BASE_URL)
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "paikkaId":
          }
          """))
      .andExpect(status().isBadRequest());
      //varmistaa virheellistä ei tallenneta TK:n
      verify(peliRepository, never()).save(any(Peli.class));
  }

  @Test
  @WithMockUser
  void createPeliNotFound() throws Exception {
  when(paikkaRepository.findById(99L))
    .thenReturn(Optional.empty());

  mockMvc.perform(post(BASE_URL)
    .contentType(MediaType.APPLICATION_JSON)
    .content("""
        {
          "paikkaId": 99,
          "pvm": "2026-11-08T20:00:00"
        }
        """))
    .andExpect(status().isNotFound());

    verify(peliRepository, never()).save(any(Peli.class));
  }

  @Test
  @WithMockUser
  void updatePeli() throws Exception {
    Paikka vanhaPaikka = new Paikka("Vanha paikka", "Helsinki");
    Paikka uusiPaikka = new Paikka("Uusi paikka", "Turku");
    Peli peli = new Peli();
    peli.setPvm(LocalDateTime.now());
    peli.setPaikka(vanhaPaikka);

    when(peliRepository.findById(1L))
      .thenReturn(Optional.of(peli));

    when(paikkaRepository.findById(2L))
      .thenReturn(Optional.of(uusiPaikka));

    when(peliRepository.save(any(Peli.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));

    mockMvc.perform(put(BASE_URL + "/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "paikkaId": 2,
            "pvm": "2026-11-08T20:00:00"
          }
        """))
      .andExpect(status().isOk())
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.paikka.nimi").value("Uusi paikka"))
      .andExpect(jsonPath("$.paikka.kaupunki").value("Turku"));
  }

  @Test
  @WithMockUser
  void updatePeliBadRequest() throws Exception {
    mockMvc.perform(put(BASE_URL + "/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "paikkaId":
          }
          """))
      .andExpect(status().isBadRequest());
      verify(peliRepository, never()).save(any(Peli.class));
  }

  @Test
  @WithMockUser
  void updatePeliNotFound() throws Exception {
    when(peliRepository.existsById(99L))
      .thenReturn(false);

    mockMvc.perform(put(BASE_URL + "/99")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "paikkaId": 1,
            "pvm": "2026-11-08T20:00:00"
          }
          """))
      .andExpect(status().isNotFound());
      verify(peliRepository, never()).save(any(Peli.class));
  }

  @Test
  @WithMockUser
  void deletePeli() throws Exception {
    when(peliRepository.existsById(1L))
      .thenReturn(true);

    mockMvc.perform(delete(BASE_URL + "/1"))
      .andExpect(status().isNoContent());

    verify(peliRepository).deleteById(1L);
  }

  //Paikkaa ei löydy -> 404 NotFound
  @Test
  @WithMockUser
  void deletePeliNotFound() throws Exception {
    when(peliRepository.existsById(99L))
      .thenReturn(false);

    mockMvc.perform(delete(BASE_URL + "/99"))
      .andExpect(status().isNotFound());
    //-> poistamista ei yritetä suorittaa
    verify(peliRepository, never()).deleteById(anyLong());
  }
}
