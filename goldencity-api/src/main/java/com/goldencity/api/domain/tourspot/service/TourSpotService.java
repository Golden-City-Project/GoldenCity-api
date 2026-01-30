package com.goldencity.api.domain.tourspot.service;

import com.goldencity.api.domain.tourspot.dto.response.TourSpotDetailResponseDto;
import com.goldencity.api.domain.tourspot.dto.response.TourSpotResponseDto;
import com.goldencity.api.domain.tourspot.entity.AreaCode;
import com.goldencity.api.domain.tourspot.entity.TourSpot;
import com.goldencity.api.domain.tourspot.repository.TourSpotRepository;
import com.goldencity.api.global.exception.DataNotFoundException;
import com.goldencity.api.global.exception.ErrorCode;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

// 관광지 비즈니스 로직
@Service
@Transactional(readOnly = true)
public class TourSpotService {

    private final TourSpotRepository tourSpotRepository;

    public TourSpotService(TourSpotRepository tourSpotRepository) {
        this.tourSpotRepository = tourSpotRepository;
    }

    // 전체 관광지 조회 (페이징 + 캐싱)
    @Cacheable(
            value = "tourspots",
            key = "'page_' + #page + '_size_' + #size",
            //
            unless = "#result == null || #result.isEmpty()" // [널 체크 수정] 결과가 null인 경우도 캐싱 방지
    )
    public Page<TourSpotResponseDto> getAllTourSpots(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<TourSpot> tourSpotPage = tourSpotRepository.findAll(pageable);
        // 널 처리 > 페이지 객체는 널이 아니지만 내부 컨텐츠가 널일 수 있음을 고려
        return tourSpotPage.map(this::convertToResponseDto);
    }

    // 권역별 조회 (캐싱)
    @Cacheable(
            value = "tourspots-by-area",
            key = "#areaCode + '_page_' + #page + '_size_' + #size",
            unless = "#result.isEmpty()"
    )
    public Page<TourSpotResponseDto> getTourSpotsByArea(AreaCode areaCode, int page, int size) {
        // [널 처리] areaCode가 null로 들어올 경우 예외 처리 혹은 기본 로직 결정 필요
        if (areaCode == null) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<TourSpot> tourSpotPage = tourSpotRepository.findByAreaCode(areaCode, pageable);
        return tourSpotPage.map(this::convertToResponseDto);
    }

    // 상세 조회 (캐싱)
    @Cacheable(value = "tourspot-detail", key = "#id")
    public TourSpotDetailResponseDto getTourSpotById(Long id) {
        // [널 처리] id가 null이면 조회가 불가능하므로 예외 처리
        if (id == null) {
            throw new DataNotFoundException(ErrorCode.TOUR_SPOT_NOT_FOUND);
        }

        TourSpot tourSpot = tourSpotRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.TOUR_SPOT_NOT_FOUND));
        return convertToDetailResponseDto(tourSpot);
    }

    // 이름 검색 (캐싱)
    @Cacheable(
            value = "tourspots-search",
            key = "#keyword + '_page_' + #page + '_size_' + #size",
            unless = "#result.isEmpty()"
    )
    public Page<TourSpotResponseDto> searchTourSpotsByName(String keyword, int page, int size) {
        // [널 처리] 검색어가 null이면 빈 결과 반환
        if (keyword == null || keyword.trim().isEmpty()) {
            return Page.empty();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<TourSpot> tourSpotPage = tourSpotRepository.findByNameContaining(keyword, pageable);
        return tourSpotPage.map(this::convertToResponseDto);
    }

    // 최신 업데이트 시간 조회
    public LocalDateTime getLatestUpdateTime() {
        return tourSpotRepository.findLatestUpdatedAt().orElse(null);
    }

    // Entity → ResponseDto 변환
    private TourSpotResponseDto convertToResponseDto(TourSpot tourSpot) {
        // [널 처리] 엔티티 자체가 null일 경우를 대비한 방어 로직
        if (tourSpot == null) return null;

        // 스트링 반환할때는 null이 아닌 빈문자열이 나가도록 핸들널스트링으로 감쌈(해당 메소드는 제일 아래에 있음)
        return TourSpotResponseDto.builder()
                .id(tourSpot.getId())
                .name(handleNullString(tourSpot.getName()))
                .address(handleNullString(tourSpot.getAddress()))
                .phoneNumber(handleNullString(tourSpot.getPhoneNumber()))
                .areaCode(tourSpot.getAreaCode())
                .latitude(tourSpot.getLatitude())
                .longitude(tourSpot.getLongitude())
                .imageUrl(handleNullString(tourSpot.getImageUrl()))
                .build();
    }

    // Entity → DetailResponseDto 변환
    private TourSpotDetailResponseDto convertToDetailResponseDto(TourSpot tourSpot) {
        if (tourSpot == null) return null;

        // 스트링 반환할때는 null이 아닌 빈문자열이 나가도록 핸들널스트링으로 감쌈(해당 메소드는 제일 아래에 있음)
        return TourSpotDetailResponseDto.builder()
                .id(tourSpot.getId())
                .name(handleNullString(tourSpot.getName()))
                .address(handleNullString(tourSpot.getAddress()))
                .phoneNumber(handleNullString(tourSpot.getPhoneNumber()))
                .areaCode(tourSpot.getAreaCode())
                .latitude(tourSpot.getLatitude())
                .longitude(tourSpot.getLongitude())
                .content(handleNullString(tourSpot.getContent()))
                .ticketStart(tourSpot.getTicketStart()) // LocalTime이라 제외
                .ticketEnd(tourSpot.getTicketEnd())
                .openTime(tourSpot.getOpenTime())
                .closeTime(tourSpot.getCloseTime())
                .imageUrl(handleNullString(tourSpot.getImageUrl()))
                .createdAt(tourSpot.getCreatedAt())
                .updatedAt(tourSpot.getUpdatedAt())
                .build();
    }

    // [널 처리 공통 메서드] 문자열이 null일 경우 빈 문자열로 반환하여 프론트엔드 에러 방지
    private String handleNullString(String value) {
        return value == null ? "" : value;
    }
}