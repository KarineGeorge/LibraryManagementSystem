package com.Maids.LibraryManagementSystem;


import com.Maids.LibraryManagementSystem.Controllers.BorrowingRecordController;
import com.Maids.LibraryManagementSystem.Exceptions.BookNotFoundException;
import com.Maids.LibraryManagementSystem.Exceptions.BorrowingRecordNotAvailable;
import com.Maids.LibraryManagementSystem.Models.BorrowingRecord;
import com.Maids.LibraryManagementSystem.Models.Librarian;
import com.Maids.LibraryManagementSystem.Services.BorrowingRecordService;
import com.Maids.LibraryManagementSystem.Services.JwtService;
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
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = BorrowingRecordController.class)
@Import({JwtService.class, SecurityConfiguration.class})
public class BorrowingRecordControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingRecordService borrowingRecordService;

    @MockBean
    private Librarian librarian;

    @MockBean
    private JwtService jwtService;

    private BorrowingRecord borrowingRecord;
    private String token;

    @BeforeEach
    public void setup() {

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("Admin@gmail.com");
        when(authentication.isAuthenticated()).thenReturn(true);

        librarian = new Librarian(1l,"Admin","Admin@gmail.com","12358Kg/");
        token = jwtService.generateToken(librarian);

    }

    @Test
    public void borrowBookSuccess() throws Exception{
        BorrowingRecord record = new BorrowingRecord();
        given(borrowingRecordService.borrowBook(anyLong(), anyLong())).willReturn(record);

        mockMvc.perform(post("/api/borrow/{bookId}/patron/{patronId}", 1, 1))
                .andExpect(status().isOk());
    }

    @Test
    public void borrowBookNotFoundBook() throws Exception {
        given(borrowingRecordService.borrowBook(anyLong(), anyLong())).willThrow(new BookNotFoundException("Book not found"));

        mockMvc.perform(post("/api/borrow/{bookId}/patron/{patronId}", 1, 1))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Book not found"));
    }

    @Test
    public void borrowBookBorrowingNotAvailable() throws Exception {
        given(borrowingRecordService.borrowBook(anyLong(), anyLong())).willThrow(new BorrowingRecordNotAvailable("Book is already borrowed"));

        mockMvc.perform(post("/api/borrow/{bookId}/patron/{patronId}", 1, 1))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Book is already borrowed"));
    }

    @Test
    public void returnBookSuccess() throws Exception {
        BorrowingRecord record = new BorrowingRecord(); // Setup return record
        given(borrowingRecordService.returnBook(anyLong(), anyLong())).willReturn(record);

        mockMvc.perform(put("/api/return/{bookId}/patron/{patronId}", 1, 1))
                .andExpect(status().isOk());
    }

    @Test
    public void returnBookFail() throws Exception {
        given(borrowingRecordService.returnBook(anyLong(), anyLong())).willThrow(new BorrowingRecordNotAvailable("This book was not borrowed by this patron or has already been returned"));

        mockMvc.perform(put("/api/return/{bookId}/patron/{patronId}", 1, 1))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("This book was not borrowed by this patron or has already been returned"));
    }
}



