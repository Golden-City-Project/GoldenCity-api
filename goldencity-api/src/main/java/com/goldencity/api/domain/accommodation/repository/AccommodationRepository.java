package com.goldencity.api.domain.accommodation.repository;

import com.goldencity.api.domain.accommodation.entity.Accommodation;
import com.goldencity.api.domain.accommodation.entity.AccommodationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// Accommodation Entity Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {

    // 타입별 조회
    Page<Accommodation> findByAccommodationType(AccommodationType accommodationType, Pageable pageable);

    // 이름으로 검색
    Page<Accommodation> findByNameContaining(String keyword, Pageable pageable);

    // 키워드로 검색
    @Query("SELECT a FROM Accommodation a WHERE a.name LIKE %:keyword% OR a.keywords LIKE %:keyword%")
    Page<Accommodation> searchByKeyword(String keyword, Pageable pageable);

    // 최신 업데이트 시간 조회
    @Query("SELECT MAX(a.updatedAt) FROM Accommodation a")
    Optional<java.time.LocalDateTime> findLatestUpdatedAt();

    // 전체 개수 조회
    @Query("SELECT COUNT(a) FROM Accommodation a")
    long countAll();
}