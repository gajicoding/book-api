package com.example.book_api.domain.book.enums;


public enum CategoryEnum {
    GENERAL("총류"),
    PHILOSOPHY("철학"),
    RELIGION("종교"),
    SOCIAL_SCIENCE("사회과학"),
    NATURAL_SCIENCE("순수과학"),
    TECHNOLOGY("기술과학"),
    ART("예술"),
    LANGUAGE("언어"),
    LITERATURE("문학"),
    HISTORY("역사");

    private final String label;

    CategoryEnum(String label) {
        this.label = label;
    }
}
