package com.Maids.LibraryManagementSystem;

import com.Maids.LibraryManagementSystem.Controllers.BookController;
import com.Maids.LibraryManagementSystem.Controllers.PatronController;
import com.Maids.LibraryManagementSystem.Exceptions.PatronNotFoundException;
import com.Maids.LibraryManagementSystem.Models.Librarian;
import com.Maids.LibraryManagementSystem.Models.Patron;
import com.Maids.LibraryManagementSystem.Services.BookService;
import com.Maids.LibraryManagementSystem.Services.JwtService;
import com.Maids.LibraryManagementSystem.Services.PatronService;
import com.Maids.LibraryManagementSystem.configs.SecurityConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PatronController.class)
@Import({JwtService.class, SecurityConfiguration.class})
public class PatronControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatronService patronService;

    @MockBean
    private Librarian librarian;

    @MockBean
    private JwtService jwtService;

    private Patron patron;
    private String token;

    @BeforeEach
    public void setup() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("Admin@gmail.com");
        when(authentication.isAuthenticated()).thenReturn(true);

        patron = new Patron(1L, "John Doe", "John@gmail.com", "01288530697", "/Password123");
        librarian = new Librarian(1l,"Admin","Admin@gmail.com","12358Kg/");
        token = jwtService.generateToken(librarian);

    }

    @Test
    public void getAllPatrons() throws Exception {
        given(patronService.getAllPatrons()).willReturn(Arrays.asList(patron));

        mockMvc.perform(get("/api/patrons")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    public void getPatronByIdSuccess() throws Exception {
        given(patronService.getPatronById(1L)).willReturn(patron);

        mockMvc.perform(get("/api/patrons/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void getPatronByIdNotFound() throws Exception {
        given(patronService.getPatronById(anyLong())).willThrow(new PatronNotFoundException("Not found"));

        mockMvc.perform(get("/api/patrons/{id}", 1)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not found"));
    }

    @Test
    public void deletePatronSuccess() throws Exception {
        willDoNothing().given(patronService).deletePatronById(1L);

        mockMvc.perform(delete("/api/patrons/{id}", 1)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePatronNotFound() throws Exception {
        willThrow(new PatronNotFoundException("Not found")).given(patronService).deletePatronById(1L);

        mockMvc.perform(delete("/api/patrons/{id}", 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}