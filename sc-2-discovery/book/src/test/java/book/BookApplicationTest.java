package book;

import book.usecase.port.BookService;
import book.adapter.gateway.ReaderEntityGateway;
import book.dto.Book;
import book.usecase.port.BookRepository;
import book.usecase.exception.BorrowingException;
import book.usecase.exception.ReturningException;

import org.junit.After;
//import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.mock.mockito.MockBean;
//import static org.mockito.Mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.test.web.servlet.MockMvc;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

//import io.restassured.module.mockmvc.RestAssuredMockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;

import com.github.javafaker.Faker;
import lombok.val;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(classes = BookApplication.class)
@AutoConfigureMockMvc
public class BookApplicationTest{

	//@Autowired private MockMvc mockMvc;
	@Autowired private BookService bookService;
	@Autowired private BookRepository bookRepository;
	//@Autowired private WebApplicationContext webApplicationContext;
	//@Autowired private BookController bookController;

	@MockBean private ReaderEntityGateway readerEntityGateway;

	private final Faker faker = new Faker();

	private static final int NUM_TEST_BOOKS = 20;

	/*@Before
	public void setUp() {
		//RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
		//RestAssuredMockMvc.standaloneSetup(bookController);

		val book = Book.builder()
			.name("Name")
			.author("Author")
			.genre("Genre")
			.publisher("Publisher")
			.build();
		book.setId(1L);
		val reader = Reader.builder()
			.firstName("John")
			.lastName("Doe")
			.dob(new Date())
			.address("")
			.phone("")
			.build();

		//when(readerEntityGateway.bookBorrowingValidator(1L, List.of(book)))
		//.thenReturn(Set.of());
	}*/
	
    @After
    public void teardown() {
		// Delete all books
		bookService.cleanUpDatabase();
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void shouldFindById() {
			//Given
			var books = List.of(
					Book.builder().name("Java").author("").genre("").publisher("").build(),
					Book.builder().name("Go").author("").genre("").publisher("").build(),
					Book.builder().name("Node").author("").genre("").publisher("").build());
			books = bookRepository.saveAll(books);
			//When
			val book = bookService.retrieveBook(books.get(0).getId());
			//Then
			assertTrue(book.isPresent());
	}

	@Test
	public void shouldFindByIds() {
			//Given
			val ids = Stream.iterate(0, e -> e + 1)
							.limit(NUM_TEST_BOOKS)
							.map(e -> Book.builder()
											.name(faker.book().title())
											.author(faker.book().author())
											.genre(faker.book().genre())
											.publisher(faker.book().publisher())
											.build())
							.map(book -> bookRepository.save(book))
							.map(book -> book.getId())
							.collect(toList());
			//When
			val books = bookService.findBooksByIds(ids);
			//Then
			assertThat(books.size(), is(NUM_TEST_BOOKS));
	}

	@Test
	public void shouldFindByReaderId() throws BorrowingException {
		//Given
		var books = List.of(
			Book.builder().name("Java").author("").genre("").publisher("").build(),
			Book.builder().name("Go").author("").genre("").publisher("").build(),
			Book.builder().name("Node").author("").genre("").publisher("").build());
		books = bookRepository.saveAll(books);
		bookService.borrowBooks(books, 1L);
		//When
		val borrowedBooks = bookService.findBooksByReaderId(1L);
		//Then
		assertThat(borrowedBooks.size(), is(3));
	}	

	@Test
	public void shouldRetrieveAllBooks() {
			//Given
			bookService.loadDatabase(Optional.of(NUM_TEST_BOOKS), List.of());
			//When
			val books = bookService.retrieveBooks(Optional.empty(), Optional.empty());
			//Then
			assertThat(books.size(), is(NUM_TEST_BOOKS));
	}

	@Test
	public void shouldBorrowBooks() throws BorrowingException{
			// Given
			var books = List.of(
					Book.builder().name("Java").author("").genre("").publisher("").build(),
					Book.builder().name("Go").author("").genre("").publisher("").build(),
					Book.builder().name("Node").author("").genre("").publisher("").build());
			books = bookRepository.saveAll(books);
			// When
			bookService.borrowBooks(books, 1L);
			// Then
			val borrowedBooks = bookService.findBooksByReaderId(1L);
			assertThat(borrowedBooks.size(), is(books.size()));
	}

	@Test
	public void shouldReturnBooks() throws BorrowingException, ReturningException{
			// Given
			var books = List.of(
					Book.builder().name("Java").author("").genre("").publisher("").build(),
					Book.builder().name("Go").author("").genre("").publisher("").build(),
					Book.builder().name("Node").author("").genre("").publisher("").build());
			books = bookRepository.saveAll(books);
			bookService.borrowBooks(books, 1L);
			// When
			bookService.returnBooks(books, 1L);
			// Then
			val borrowedBooks = bookService.findBooksByReaderId(1L);
			assertThat(borrowedBooks.size(), is(0));
	}
}
