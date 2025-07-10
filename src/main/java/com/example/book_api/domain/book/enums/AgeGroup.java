package com.example.book_api.domain.book.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum AgeGroup {
    TEENS_EARLY("10대 초반", 10, 14),
    TEENS_LATE("10대 후반", 15, 19),
    TWENTIES_EARLY("20대 초반", 20, 24),
    TWENTIES_LATE("20대 후반", 25, 29),
    THIRTIES("30대", 30, 39),
    FORTIES("40대", 40, 49),
    FIFTIES("50대", 50, 59),
    SIXTIES_AND_ABOVE("60대 이상", 60, 120);

    private final String label;
    private final int minAge;
    private final int maxAge;

    AgeGroup(String label, int minAge, int maxAge) {
        this.label = label;
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public static Optional<AgeGroup> of(String input) {
        if (input == null || input.isBlank()) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(e -> e.name().equals(input))
                .findFirst();
    }
}
