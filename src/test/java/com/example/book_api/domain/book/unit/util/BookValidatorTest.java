package com.example.book_api.domain.book.unit.util;

import com.example.book_api.domain.book.enums.AgeGroup;
import com.example.book_api.domain.book.enums.CategoryEnum;
import com.example.book_api.domain.book.exception.InvalidSearchConditionException;
import com.example.book_api.domain.book.validation.BookValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookValidatorTest {

    @InjectMocks
    BookValidator bookValidator;

    @Test
    void 카테고리_검사_성공() {

        CategoryEnum category = bookValidator.validateCategory(CategoryEnum.NATURAL_SCIENCE.name());

        assertNotNull(category);
    }

    @Test
    void 카테고리_검사_실패() {

        String invalidCategory = "aaa";

        InvalidSearchConditionException ex = assertThrows(
                InvalidSearchConditionException.class,
                () -> bookValidator.validateCategory(invalidCategory)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertTrue(ex.getMessage().contains(invalidCategory));
    }

    @Test
    void 나이대_검사_성공() {

        AgeGroup age = bookValidator.validateAgeGroup(AgeGroup.TWENTIES_LATE.name());

        assertNotNull(age);
    }

    @Test
    void 나이대_검사_실패() {

        String invalidAge = "aaa";

        InvalidSearchConditionException ex = assertThrows(
                InvalidSearchConditionException.class,
                () -> bookValidator.validateAgeGroup(invalidAge)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatus());
        assertTrue(ex.getMessage().contains(invalidAge));
    }
}
