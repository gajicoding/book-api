DROP DATABASE IF EXISTS book_api;
CREATE DATABASE IF NOT EXISTS book_api;
USE book_api;

-- 사용자(users) 테이블 생성
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 ID (PK)',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT '이메일',
    password VARCHAR(255) NOT NULL COMMENT '비밀번호',
    name VARCHAR(50) NOT NULL COMMENT '비밀번호',
    birth DATETIME COMMENT '생일',
    role VARCHAR(20) COMMENT '권한',

    created_at DATETIME COMMENT '생성일',
    modified_at DATETIME COMMENT '수정일',
    deleted_at DATETIME COMMENT '삭제일 (soft delete)'
) COMMENT = '사용자 Table';

-- 책(books) 테이블 생성
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '책 ID (PK)',
    title VARCHAR(255) NOT NULL COMMENT '제목',
    author VARCHAR(100) NOT NULL COMMENT '저자',
    publisher VARCHAR(100) NOT NULL COMMENT '발행처',
    publication_year YEAR NOT NULL COMMENT '발행년도',
    isbn VARCHAR(20) NOT NULL COMMENT 'ISBN',
    category_code VARCHAR(50) NOT NULL COMMENT '분류기호',

    user_id BIGINT NOT NULL COMMENT '유저 ID (FK)',

    created_at DATETIME COMMENT '생성일',
    modified_at DATETIME COMMENT '수정일',
    deleted_at DATETIME COMMENT '삭제일 (soft delete)',

    FOREIGN KEY (user_id) REFERENCES users(id)

) COMMENT = '책 Table';

-- 책 조회수(book_views) 테이블 생성
CREATE TABLE book_views (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '책 조회수 ID (PK)',

    book_id BIGINT NOT NULL COMMENT '책 ID (FK)',
    user_id BIGINT NOT NULL COMMENT '유저 ID (FK)',

    created_at DATETIME COMMENT '생성일',

    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (user_id) REFERENCES users(id)

) COMMENT = '책 조회수 Table';

-- 책 평점(book_rating) 테이블 생성
CREATE TABLE book_views (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '책 평점 ID (PK)',
    score INT NOT NULL COMMENT '평점',

    book_id BIGINT NOT NULL COMMENT '책 ID (FK)',
    user_id BIGINT NOT NULL COMMENT '유저 ID (FK)',

    created_at DATETIME COMMENT '생성일',
    modified_at DATETIME COMMENT '수정일',
    deleted_at DATETIME COMMENT '삭제일 (soft delete)',

    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (user_id) REFERENCES users(id)

) COMMENT = '책 평점 Table';


-- 책 댓글(book_comments) 테이블 생성
CREATE TABLE book_comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '책 댓글 ID (PK)',
    content VARCHAR(255) NOT NULL COMMENT '댓글 내용',

    book_id BIGINT NOT NULL COMMENT '책 ID (FK)',
    user_id BIGINT NOT NULL COMMENT '유저 ID (FK)',

    created_at DATETIME COMMENT '생성일',
    modified_at DATETIME COMMENT '수정일',
    deleted_at DATETIME COMMENT '삭제일 (soft delete)',

    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (user_id) REFERENCES users(id)

) COMMENT = '책 댓글 Table';

-- 로그(logs) 테이블 생성
CREATE TABLE logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '로그 ID (PK)',
    target_type VARCHAR(255) COMMENT '대상 타입',
    target_id BIGINT NOT NULL COMMENT '대상 id',
    request_method VARCHAR(255) COMMENT '요청 http 메서드',
    request_uri VARCHAR(255) COMMENT '요청 api 엔드포인트',
    message VARCHAR(255) COMMENT '메세지',
    is_success BOOLEAN COMMENT '성공 여부',

    user_id BIGINT NOT NULL COMMENT '유저 ID (FK)',

    created_at DATETIME COMMENT '생성일',

    FOREIGN KEY (user_id) REFERENCES users(id)
) COMMENT = '로그 Table';