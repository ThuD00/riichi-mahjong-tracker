package riichi.mahjong_tracker.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
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

@WebMvcTest({TulosController.class, PeliController.class})
@Import(WebSecurityConfig.class)
@ActiveProfiles("test")
public class TulosControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private TulosRepository tulosRepository;

  @MockitoBean
  private PelaajaRepository pelaajaRepository;

  @MockitoBean
  private PeliRepository peliRepository;

  @MockitoBean
  private PaikkaRepository paikkaRepository;

  @Test
  void tulosLista() throws Exception {
    when(tulosRepository.findAll())
      .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/tulos/lista"))
      .andExpect(status().isOk())
      .andExpect(view().name("tulos/lista"))
      .andExpect(model().attributeExists("tulokset"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void addTulos() throws Exception {
    Peli peli = mock(Peli.class);
    when(peliRepository.findById(1L))
      .thenReturn(Optional.of(peli));

    when(pelaajaRepository.findAll())
      .thenReturn(Collections.emptyList());

    mockMvc.perform(get("/tulos/add").param("peliId", "1"))
      .andExpect(status().isOk())
      .andExpect(view().name("tulos/add"))
      .andExpect(model().attributeExists("tulos"))
      .andExpect(model().attributeExists("pelaajat"))
      .andExpect(model().attributeExists("peli"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void saveTulos() throws Exception {
    Peli peli = mock(Peli.class);
    Pelaaja pelaajat = new Pelaaja();

    when(peliRepository.findById(1L))
      .thenReturn(Optional.of(peli));

    when(peli.getTulokset())
      .thenReturn(Collections.emptyList());

    when(pelaajaRepository.findById(1L))
      .thenReturn(Optional.of(pelaajat)); 

    mockMvc.perform(post("/tulos/save")
          .param("peliId", "1")
          .param("pelaajaId", "1")
          .param("pisteet", "30000")
          .param("sijoitus", "1")
          .param("umat", "15"))
      .andExpect(status().is3xxRedirection())
      .andExpect(redirectedUrl("/peli/1"));

    verify(tulosRepository).save(any(Tulos.class));
  }

  //viidennen tuloksen lisääminen estetään
  @Test
  @WithMockUser(roles = "ADMIN")
  void saveTulosMax4() throws Exception {
    Peli peli = mock(Peli.class);
    Pelaaja pelaaja = new Pelaaja();

    List<Tulos> tulokset = Arrays.asList(
      new Tulos(), new Tulos(), new Tulos(), new Tulos()
    );

    when(peliRepository.findById(1L))
      .thenReturn(Optional.of(peli));

    when(peli.getTulokset()).thenReturn(tulokset);

    when(pelaajaRepository.findById(5L))
      .thenReturn(Optional.of(pelaaja));

    mockMvc.perform(post("/tulos/save")
          .param("peliId", "1")
          .param("pelaajaId", "5")
          .param("pisteet", "25000")
          .param("sijoitus", "1")
          .param("umat", "15"))
      .andExpect(status().is3xxRedirection())
      .andExpect(redirectedUrl("/peli/1?virhe=maara"));

    verify(tulosRepository, never()).save(any(Tulos.class));
  }

  //samaa pelaajaa ei voi lisätä kahdesti
  @Test
  @WithMockUser(roles = "ADMIN")
  void saveTulosSamePlayer() throws Exception {
    Peli peli = mock(Peli.class);
    
    Pelaaja pelaaja = new Pelaaja();
    pelaaja.setPelaajaId(1L);

    Tulos olemassaOlevaTulos = new Tulos();
    olemassaOlevaTulos.setPelaaja(pelaaja);

    assertNotNull(olemassaOlevaTulos.getPelaaja());
    assertEquals(1L, olemassaOlevaTulos.getPelaaja().getPelaajaId());

    when(pelaajaRepository.findById(1L))
        .thenReturn(Optional.of(pelaaja));

    when(peliRepository.findById(1L))
        .thenReturn(Optional.of(peli));

    when(peli.getTulokset())
        .thenReturn(Collections.singletonList(olemassaOlevaTulos));
    
    mockMvc.perform(post("/tulos/save")
          .param("peliId", "1")
          .param("pelaajaId", "1")
          .param("pisteet", "30000")
          .param("sijoitus", "1")
          .param("umat", "15"))
      .andExpect(status().is3xxRedirection())
      .andExpect(redirectedUrl("/peli/1?virhe=pelaaja"));

    verify(tulosRepository, never()).save(any(Tulos.class));
  }

  //pelin tietosivu ja tulosten näyttäminen
  @Test
  @WithMockUser(roles = "ADMIN")
  void naytaPelinTiedot() throws Exception {
    Peli peli = mock(Peli.class);
    Paikka paikka = mock(Paikka.class);

    when(peliRepository.findById(1L))
      .thenReturn(Optional.of(peli));

    when(peli.getPvm())
      .thenReturn(LocalDateTime.of(2026, 11, 9, 14, 0));

    when(peli.getPaikka()).thenReturn(paikka);
    when(paikka.getNimi()).thenReturn("Testipaikka");
    when(peli.getTulokset()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/peli/1"))
      .andExpect(status().isOk())
      .andExpect(view().name("peli/tiedot"))
      .andExpect(model().attributeExists("peli"))
      .andExpect(content().string(containsString("Pelin tiedot")))
      .andExpect(content().string(containsString("Testipaikka")));
  }

  //viidenneksi lisättävän tuloksen virheilmoitus
  @Test
  @WithMockUser(roles = "ADMIN")
  void naytaMaaraVirheilmoitus() throws Exception {
    Peli peli = mock(Peli.class);
    Paikka paikka = mock(Paikka.class);

    when(peliRepository.findById(1L))
      .thenReturn(Optional.of(peli));

    when(peli.getPvm())
      .thenReturn(LocalDateTime.of(2026, 11, 9, 14, 0));

    when(peli.getPaikka()).thenReturn(paikka);
    when(paikka.getNimi()).thenReturn("Testipaikka");
    when(peli.getTulokset()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/peli/1").param("virhe", "maara"))
      .andExpect(status().isOk())
      .andExpect(content().string(
          containsString("Pelissä voi olla enintään 4 tulosta!")));
  }

  //saman pelaajan virheilmoitus
  @Test
  @WithMockUser(roles = "ADMIN")
  void naytaPelaajaVirheilmoitus() throws Exception {
    Peli peli = mock(Peli.class);
    Paikka paikka = mock(Paikka.class);

    when(peliRepository.findById(1L))
      .thenReturn(Optional.of(peli));

    when(peli.getPvm())
      .thenReturn(LocalDateTime.of(2026, 11, 9, 14, 0));

    when(peli.getPaikka()).thenReturn(paikka);
    when(paikka.getNimi()).thenReturn("Testipaikka");
    when(peli.getTulokset()).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/peli/1").param("virhe", "pelaaja"))
      .andExpect(status().isOk())
      .andExpect(content().string(
          containsString("Tämä pelaaja on jo lisätty tähän peliin!")));
  }
}
