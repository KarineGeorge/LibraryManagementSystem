package com.Maids.LibraryManagementSystem;

import com.Maids.LibraryManagementSystem.Controllers.BookController;
import com.Maids.LibraryManagementSystem.Exceptions.BookNotFoundException;
import com.Maids.LibraryManagementSystem.Models.Book;
import com.Maids.LibraryManagementSystem.Models.Librarian;
import com.Maids.LibraryManagementSystem.Repositories.LibrarianRepository;
import com.Maids.LibraryManagementSystem.Services.BookService;
import com.Maids.LibraryManagementSystem.Services.JwtService;
import com.Maids.LibraryManagementSystem.configs.SecurityConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BookController.class)
@Import({JwtService.class,SecurityConfiguration.class})
public class BookControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private Book book;
    private String token;

    @MockBean
    private Librarian librarian;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private LibrarianRepository librarianRepository;


    @BeforeEach
    public void setup() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("Admin@gmail.com");
        when(authentication.isAuthenticated()).thenReturn(true);

        book = new Book(1L, "bookTitle1", "Karine", 2018, "1234567890");
        librarian = new Librarian(1l,"Admin","Admin@gmail.com","12358Kg/");
        token = jwtService.generateToken(librarian);
    }

    @Test
    public void getAllBooks() throws Exception {
        given(bookService.getAllBooks()).willReturn(Arrays.asList(book));
        mockMvc.perform(get("/api/books")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getBookByIdSuccess() throws Exception {
        given(bookService.getBookById(1L)).willReturn(book);

        mockMvc.perform(get("/api/books/{id}", 1)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

    }

    @Test
    public void getBookByIdNotFound() throws Exception {
        given(bookService.getBookById(anyLong())).willThrow(new BookNotFoundException("Not found"));

        mockMvc.perform(get("/api/books/{id}", 1)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());

    }

    @Test
    public void addBook() throws Exception {
        given(bookService.addBook(book)).willReturn(book);

        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(book))
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated());
    }

    @Test
    public void updateBookSuccess() throws Exception {
        given(bookService.updateBook(eq(1L), ArgumentMatchers.any(Book.class))).willReturn(book);
        mockMvc.perform(put("/api/books/{id}", 1)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteBookSuccess() throws Exception {
        willDoNothing().given(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/{id}", 1)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteBookNotFound() throws Exception {
        willThrow(new BookNotFoundException("Not found")).given(bookService).deleteBook(1L);

        mockMvc.perform(delete("/api/books/{id}", 1)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());

    }
}