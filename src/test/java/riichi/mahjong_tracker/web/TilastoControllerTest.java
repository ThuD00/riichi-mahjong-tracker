package riichi.mahjong_tracker.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import riichi.mahjong_tracker.WebSecurityConfig;
import riichi.mahjong_tracker.service.TilastoService;

@WebMvcTest(TilastoController.class)
@Import(WebSecurityConfig.class)
@ActiveProfiles("test")
public class TilastoControllerTest {
  
  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private TilastoService tilastoService;

  @Test
  @WithMockUser
  void naytaPelaajaTilasto() throws Exception {
    when(tilastoService.haePelaajaTilastot())
      .thenReturn(Collections.emptyList());
    
    //palauttaa arvona 10
    when(tilastoService.haePelienMaara()).thenReturn(10L);
    when(tilastoService.haePelaajienMaara()).thenReturn(5L);
    when(tilastoService.haePelipaikkojenMaara()).thenReturn(3L);
    when(tilastoService.haeTulostenMaara()).thenReturn(40L);

    mockMvc.perform(get("/tilasto"))
      .andExpect(status().isOk())
      //näkymän tarkistus
      .andExpect(view().name("tilasto/tilastot"))
      //model-attribuuttien tarkistus
      .andExpect(model().attributeExists(
        "pelaajaTilastot",
        "pelienMaara",
        "pelaajienMaara",
        "pelipaikkojenMaara",
        "tulostenMaara"
    ));
  }
}
