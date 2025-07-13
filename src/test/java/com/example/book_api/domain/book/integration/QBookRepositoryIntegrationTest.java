package com.example.book_api.domain.book.integration;

import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.entity.BookView;
import com.example.book_api.domain.book.enums.AgeGroup;
import com.example.book_api.domain.book.enums.CategoryEnum;
import com.example.book_api.domain.book.repository.QBookRepository;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.domain.user.enums.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class QBookRepositoryIntegrationTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private QBookRepository qBookRepository;

    @BeforeEach
    void setUp() {

        User user = new User("email", "password", "name", LocalDate.parse("2000-01-01"), Role.USER);
        em.persist(user);

        Book book1 = new Book("title1", "author1", "publisher1", Year.parse("2010"), "11111111", CategoryEnum.GENERAL);
        em.persist(book1);
        em.persist(new BookView(book1, user));
        em.persist(new BookView(book1, user));
        em.persist(new BookView(book1, user));

        Book book2 = new Book("title2", "author1", "publisher1", Year.parse("2010"), "11111111", CategoryEnum.GENERAL);
        em.persist(book2);
        em.persist(new BookView(book2, user));
        em.persist(new BookView(book2, user));

        Book book3 = new Book("title3", "author1", "publisher1", Year.parse("2010"), "11111111", CategoryEnum.GENERAL);
        em.persist(book3);
        em.persist(new BookView(book3, user));
    }

    @Test
    void 전체_Top_10_조회_테스트() {
        List<Book> topBooks = qBookRepository.findTop10Books();

        assertThat(topBooks).hasSize(3);
        assertThat(topBooks.get(0).getTitle()).isEqualTo("title1");
        assertThat(topBooks.get(1).getTitle()).isEqualTo("title2");
        assertThat(topBooks.get(2).getTitle()).isEqualTo("title3");
    }

    @Test
    void 카테고리별_Top_10_조회_테스트() {
        List<Book> topBooks = qBookRepository.findTop10BooksByCategory(CategoryEnum.GENERAL);

        assertThat(topBooks).hasSize(3);
        assertThat(topBooks.get(0).getTitle()).isEqualTo("title1");
        assertThat(topBooks.get(1).getTitle()).isEqualTo("title2");
        assertThat(topBooks.get(2).getTitle()).isEqualTo("title3");
    }

    @Test
    void 나이대별_Top_10_조회_테스트() {
        List<Book> topBooks = qBookRepository.findTop10BooksByAgeGroup(AgeGroup.TWENTIES_LATE);

        assertThat(topBooks).hasSize(3);
        assertThat(topBooks.get(0).getTitle()).isEqualTo("title1");
        assertThat(topBooks.get(1).getTitle()).isEqualTo("title2");
        assertThat(topBooks.get(2).getTitle()).isEqualTo("title3");
    }

}
