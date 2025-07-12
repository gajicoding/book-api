package com.example.book_api.domain.rating.service;

import com.example.book_api.domain.book.entity.Book;
import com.example.book_api.domain.book.service.BookService;
import com.example.book_api.domain.rating.dto.request.RatingRequestDto;
import com.example.book_api.domain.rating.dto.response.AverageRatingResponse;
import com.example.book_api.domain.rating.dto.response.MyRatingResponse;
import com.example.book_api.domain.rating.dto.response.RatingDistributionResponse;
import com.example.book_api.domain.rating.dto.response.TopRatedBookResponse;
import com.example.book_api.domain.rating.entity.Rating;
import com.example.book_api.domain.rating.exception.RatingErrorCode;
import com.example.book_api.domain.rating.exception.RatingException;
import com.example.book_api.domain.rating.repository.RatingRepository;
import com.example.book_api.domain.user.entity.User;
import com.example.book_api.domain.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final BookService bookService;
    private final UserService userService;

    @Transactional
    public void createRating(Long bookId, RatingRequestDto request, Long userId) {
        Book book = bookService.getBookById(bookId);
        User user = userService.findById(userId);

        if (ratingRepository.existsByBookAndUser(book, user)) {
            throw new RatingException(RatingErrorCode.DUPLICATE_RATING);
        }

        Rating rating = Rating.builder()
                .score(request.getScore())
                .book(book)
                .user(user)
                .build();

        ratingRepository.save(rating);
    }

    @Transactional
    public void updateRating(Long bookId, RatingRequestDto request, Long userId) {
        Book book = bookService.getBookById(bookId);
        User user = userService.findById(userId);

        Rating rating = ratingRepository.findByBookAndUser(book, user)
                .orElseThrow(() -> new RatingException(RatingErrorCode.RATING_NOT_FOUND));

        rating.updateScore(request.getScore());
    }

    @Transactional
    public void deleteRating(Long bookId, Long userId) {
        Book book = bookService.getBookById(bookId);
        User user = userService.findById(userId);

        Rating rating = ratingRepository.findByBookAndUser(book, user)
                .orElseThrow(() -> new RatingException(RatingErrorCode.RATING_NOT_FOUND));

        ratingRepository.delete(rating);
    }

    //내가 남긴 평점 조회
    public MyRatingResponse getMyRating(Long bookId, Long userId) {
        Book book = bookService.getBookById(bookId);
        User user = userService.findById(userId);

        Rating rating = ratingRepository.findByBookAndUser(book, user)
                .orElseThrow(() -> new RatingException(RatingErrorCode.RATING_NOT_FOUND));

        return new MyRatingResponse(book.getId(), rating.getScore());
    }

    //책 평점 평균 조회
    public AverageRatingResponse getAverageRating(Long bookId) {
        Book book = bookService.getBookById(bookId);

        Double average = ratingRepository.findAverageByBook(book);
        Long count = ratingRepository.countByBook(book);

        return new AverageRatingResponse(book.getId(), average != null ? average : 0.0, count);
    }

    //책 평점 분포 조회
    public List<RatingDistributionResponse> getRatingDistribution(Long bookId) {
        Book book = bookService.getBookById(bookId);
        return ratingRepository.countGroupByScore(book);
    }

    //평점 높은 순 top 10
    public List<TopRatedBookResponse> getTop10RatedBooks() {
        return ratingRepository.findTop10BooksByAverageScore(PageRequest.of(0, 10));
    }

}


