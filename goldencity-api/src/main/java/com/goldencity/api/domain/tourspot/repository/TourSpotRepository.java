package com.goldencity.api.domain.tourspot.repository;

import com.goldencity.api.domain.tourspot.entity.AreaCode;
import com.goldencity.api.domain.tourspot.entity.TourSpot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

// TourSpot Entity Repository
public interface TourSpotRepository extends JpaRepository<TourSpot, Long> {

    // 권역별 조회 (페이징)
    Page<TourSpot> findByAreaCode(AreaCode areaCode, Pageable pageable);

    // 이름으로 검색 (부분 일치, 페이징)
    Page<TourSpot> findByNameContaining(String keyword, Pageable pageable);

    // 최신 데이터 업데이트 시간 조회 (배치 상태 확인용)
    @Query("SELECT MAX(t.updatedAt) FROM TourSpot t")
    Optional<java.time.LocalDateTime> findLatestUpdatedAt();

    // 전체 데이터 개수
    @Query("SELECT COUNT(t) FROM TourSpot t")
    long countAll();
}