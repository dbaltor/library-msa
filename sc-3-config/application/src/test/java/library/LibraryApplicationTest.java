package library;

import library.usecase.port.BookService;
import library.adapter.controller.port.BookController;
import library.usecase.port.ReaderService;
import library.adapter.controller.port.ReaderController;
import library.dto.Book;
import library.dto.Reader;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.Optional;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;

import com.github.javafaker.Faker;
import lombok.val;

import static org.hamcrest.Matchers.containsString;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class LibraryApplicationTest{

	@Autowired private MockMvc mockMvc;
	@MockBean private BookService bookService;
	@MockBean private ReaderService readerService;

	private final Faker faker = new Faker();

	private static final int NUM_TEST_READERS = 5;
	private static final int NUM_TEST_BOOKS = 20;
	

	@Test
	public void contextLoads() {
	}

	@Test
	public void shouldLoadHomePage() throws Exception {
		mockMvc.perform(get("/"))
			.andDo(print())
			.andExpect(view().name("Index"));
	}
	@Test
	public void shouldLoadBookDatabase() throws Exception {
		//Given
		val books = Stream.iterate(0, e -> e + 1)
		.limit(NUM_TEST_BOOKS)
		.map(e -> Book.builder()
			.name(faker.book().title())
			.author(faker.book().author())
			.genre(faker.book().genre())
			.publisher(faker.book().publisher())
			.build())
		.collect(toList());

		when(bookService.loadDatabase(Optional.of(NUM_TEST_BOOKS)))
			.thenReturn(books);
		//When
		mockMvc.perform(post("/loadbooks?count=" + NUM_TEST_BOOKS))
		//Then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(String.format("Book database loaded with %d records", NUM_TEST_BOOKS))));
	}

	@Test
	public void shouldLoadReaderDatabase() throws Exception {
		//Given
		val readers = Stream.iterate(0, e -> e + 1)
			.limit(NUM_TEST_READERS)
			.map(e -> Reader.builder()
				.firstName(faker.name().firstName())
				.lastName(faker.name().lastName())
				.dob(faker.date().birthday())
				.address(faker.address().streetAddress())
				.phone(faker.phoneNumber().phoneNumber())
				.build())
		.collect(toList());

		when(readerService.loadDatabase(Optional.of(NUM_TEST_READERS)))
			.thenReturn(readers);
		//When
		mockMvc.perform(post("/loadreaders?count=" + NUM_TEST_READERS))
		//Then
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString(String.format("Reader database loaded with %d records", NUM_TEST_READERS))));
	}

	@Test
	public void shouldCleanUpDatabases() throws Exception {
		mockMvc.perform(post("/cleanup"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("All data have been removed.")));
	}

	@Test
	public void shouldRetrieveBooks() throws Exception {
		mockMvc.perform(get("/listbooks"))
		.andDo(print())
		.andExpect(view().name(BookController.BOOKS_TEMPLATE));
	}

	@Test
	public void shouldRetrieveReaders() throws Exception {
		mockMvc.perform(get("/listreaders"))
		.andDo(print())
		.andExpect(view().name(ReaderController.READERS_TEMPLATE));
	}
}
