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
import riichi.mahjong_tracker.domain.Pelaaja;
import riichi.mahjong_tracker.domain.Peli;
import riichi.mahjong_tracker.domain.Tulos;
import riichi.mahjong_tracker.repository.PaikkaRepository;
import riichi.mahjong_tracker.repository.PelaajaRepository;
import riichi.mahjong_tracker.repository.PeliRepository;
import riichi.mahjong_tracker.repository.TulosRepository;

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

@WebMvcTest(TulosRestController.class)
@Import(WebSecurityConfig.class)
@ActiveProfiles("test")
public class TulosRestControllerTest {

  private static final String BASE_URL = "/api/tulos";

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private PeliRepository peliRepository;

  @MockitoBean
  private PelaajaRepository pelaajaRepository;

  @MockitoBean
  private TulosRepository tulosRepository;

  @Test
  void findAllTulokset() throws Exception {
    when(tulosRepository.findAll()).thenReturn(Collections.emptyList());
    
    mockMvc.perform(get(BASE_URL))
      .andExpect(status().isOk()) 
      .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$").isArray());
  }
  
  @Test
  void searchTulosById() throws Exception {
    Tulos tulos = new Tulos();

    when(tulosRepository.findById(1L)).thenReturn(Optional.of(tulos));

    mockMvc.perform(get(BASE_URL + "/1"))
      .andExpect(status().isOk());
  }

  @Test
  void searchTulosByIdNotFound() throws Exception {
    when(tulosRepository.findById(99L))
      .thenReturn(Optional.empty());

    mockMvc.perform(get(BASE_URL + "/99"))
      .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  void createTulos() throws Exception {
    Peli peli = new Peli();
    Pelaaja pelaaja = new Pelaaja();
    Tulos tulos = new Tulos();

    when(peliRepository.findById(1L))
      .thenReturn(Optional.of(peli));

    when(pelaajaRepository.findById(1L))
      .thenReturn(Optional.of(pelaaja));

    when(tulosRepository.save(any(Tulos.class)))
      .thenReturn(tulos);

    mockMvc.perform(post(BASE_URL)
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "peliId": 1,
            "pelaajaId": 1,
            "pisteet": 30000,
            "sijoitus": 1,
            "umat": 15
          }
          """))
      .andExpect(status().isCreated());

      verify(tulosRepository).save(any(Tulos.class));
  }

  @Test
  @WithMockUser
  void createTulosBadRequest() throws Exception {
    mockMvc.perform(post(BASE_URL)
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "peliId": 1,
            "pelaajaId": 1,
            "pisteet": null,
            "sijoitus": 1,
            "umat": 15
          }
          """))
      .andExpect(status().isBadRequest());
      verify(tulosRepository, never()).save(any(Tulos.class));
  }

  @Test
  @WithMockUser
  void createTulosPeliNotFound() throws Exception {
  when(peliRepository.findById(99L))
    .thenReturn(Optional.empty());

  mockMvc.perform(post(BASE_URL)
    .contentType(MediaType.APPLICATION_JSON)
    .content("""
        {
          "peliId": 99,
          "pelaajaId": 1,
          "pisteet": 30000,
          "sijoitus": 1,
          "umat": 15
        }
        """))
    .andExpect(status().isNotFound());

    verify(tulosRepository, never()).save(any(Tulos.class));
  }

  @Test
  @WithMockUser
  void updateTulos() throws Exception {
    Peli peli = new Peli();
    Pelaaja pelaaja = new Pelaaja();
    Tulos tulos = new Tulos();

    when(tulosRepository.findById(1L))
      .thenReturn(Optional.of(tulos));

    when(peliRepository.findById(1L))
      .thenReturn(Optional.of(peli));

    when(pelaajaRepository.findById(1L))
      .thenReturn(Optional.of(pelaaja));

    when(tulosRepository.save(any(Tulos.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));

    mockMvc.perform(put(BASE_URL + "/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "peliId": 1,
            "pelaajaId": 1,
            "pisteet": 30000,
            "sijoitus": 1,
            "umat": 15
          }
        """))
      .andExpect(status().isOk());
  }

  @Test
  @WithMockUser
  void updateTulosBadRequest() throws Exception {
    mockMvc.perform(put(BASE_URL + "/1")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "peliId": 1,
            "pelaajaId": 1,
            "pisteet": 30000,
            "sijoitus": 0,
            "umat": 15
          }
          """))
      .andExpect(status().isBadRequest());
      verify(tulosRepository, never()).save(any(Tulos.class));
  }

  @Test
  @WithMockUser
  void updateTulosNotFound() throws Exception {
    when(peliRepository.existsById(99L))
      .thenReturn(false);

    mockMvc.perform(put(BASE_URL + "/99")
      .contentType(MediaType.APPLICATION_JSON)
      .content("""
          {
            "peliId": 1,
            "pelaajaId": 1,
            "pisteet": 30000,
            "sijoitus": 1,
            "umat": 15
          }
          """))
      .andExpect(status().isNotFound());
      verify(tulosRepository, never()).save(any(Tulos.class));
  }

  @Test
  @WithMockUser
  void deleteTulos() throws Exception {
    when(tulosRepository.existsById(1L))
      .thenReturn(true);

    mockMvc.perform(delete(BASE_URL + "/1"))
      .andExpect(status().isNoContent());

    verify(tulosRepository).deleteById(1L);
  }

  //Paikkaa ei löydy -> 404 NotFound
  @Test
  @WithMockUser
  void deleteTulosNotFound() throws Exception {
    when(tulosRepository.existsById(99L))
      .thenReturn(false);

    mockMvc.perform(delete(BASE_URL + "/99"))
      .andExpect(status().isNotFound());
    //-> poistamista ei yritetä suorittaa
    verify(tulosRepository, never()).deleteById(anyLong());
  }
}
