package com.example.book_api.domain.book.validation;

import com.example.book_api.domain.book.enums.AgeGroup;
import com.example.book_api.domain.book.enums.CategoryEnum;
import com.example.book_api.domain.book.exception.InvalidSearchConditionException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class BookValidator {

    public CategoryEnum validateCategory(String category) {
        return CategoryEnum.of(category).orElseThrow(()
                -> new InvalidSearchConditionException(HttpStatus.BAD_REQUEST, "잘못된 카테고리 입니다.: " + category)
        );
    }

    public AgeGroup validateAgeGroup(String ageGroup) {
        return AgeGroup.of(ageGroup).orElseThrow(()
                -> new InvalidSearchConditionException(HttpStatus.BAD_REQUEST, "잘못된 나이대 입니다.: " + ageGroup)
        );
    }

}
