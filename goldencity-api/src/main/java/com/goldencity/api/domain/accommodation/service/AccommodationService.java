package com.goldencity.api.domain.accommodation.service;

import com.goldencity.api.domain.accommodation.dto.response.AccommodationDetailResponseDto;
import com.goldencity.api.domain.accommodation.dto.response.AccommodationResponseDto;
import com.goldencity.api.domain.accommodation.entity.Accommodation;
import com.goldencity.api.domain.accommodation.entity.AccommodationType;
import com.goldencity.api.domain.accommodation.repository.AccommodationRepository;
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

// 숙박 비즈니스 로직
@Service
@Transactional(readOnly = true)
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    public AccommodationService(AccommodationRepository accommodationRepository) {
        this.accommodationRepository = accommodationRepository;
    }

    // 전체 숙박업소 조회
    @Cacheable(
            value = "accommodations",
            key = "'page_' + #page + '_size_' + #size",
            // 널체크추가
            unless = "#result == null || #result.isEmpty()"
    )
    public Page<AccommodationResponseDto> getAllAccommodations(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Accommodation> accommodations = accommodationRepository.findAll(pageable);
        return accommodations.map(this::convertToResponseDto);
    }

    // 타입별 조회
    @Cacheable(
            value = "accommodations-type",
            key = "#type + '_page_' + #page + '_size_' + #size",
            // 널체크추가
            unless = "#result == null || #result.isEmpty()"
    )
    public Page<AccommodationResponseDto> getAccommodationsByType(AccommodationType type, int page, int size) {
        // [널 처리 추가] 타입이 없으면 빈 페이지를 반환합니다.
        if (type == null) return Page.empty();

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Accommodation> accommodations = accommodationRepository.findByAccommodationType(type, pageable);
        return accommodations.map(this::convertToResponseDto);
    }

    // 상세 조회
    @Cacheable(value = "accommodation-detail", key = "#id")
    public AccommodationDetailResponseDto getAccommodationById(Long id) {
        // [널 처리 추가] ID가 없으면 예외를 발생시킵니다.
        if (id == null) throw new DataNotFoundException(ErrorCode.ACCOMMODATION_NOT_FOUND);

        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.ACCOMMODATION_NOT_FOUND));
        return convertToDetailResponseDto(accommodation);
    }

    // 검색
    @Cacheable(
            value = "accommodations-search",
            key = "#keyword + '_page_' + #page + '_size_' + #size",
            //널 처리 추가
            unless = "#result == null || #result.isEmpty()"
    )
    public Page<AccommodationResponseDto> searchAccommodations(String keyword, int page, int size) {
        // [널 처리 추가] 검색어가 없으면 빈 페이지를 반환합니다.
        if (keyword == null || keyword.trim().isEmpty()) return Page.empty();

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Accommodation> accommodations = accommodationRepository.searchByKeyword(keyword, pageable);
        return accommodations.map(this::convertToResponseDto);
    }

    // 최신 업데이트 시간 조회
    public LocalDateTime getLatestUpdateTime() {
        return accommodationRepository.findLatestUpdatedAt().orElse(null);
    }

    // Entity → ResponseDto 변환
    private AccommodationResponseDto convertToResponseDto(Accommodation accommodation) {
        if (accommodation == null) return null;

        return AccommodationResponseDto.builder()
                .id(accommodation.getId())
                .name(handleNullString(accommodation.getName()))
                .address(handleNullString(accommodation.getAddress()))
                .phoneNumber(handleNullString(accommodation.getPhoneNumber()))
                .accommodationType(accommodation.getAccommodationType())
                .latitude(accommodation.getLatitude())
                .longitude(accommodation.getLongitude())
                .imageUrl(handleNullString(accommodation.getImageUrl()))
                .build();
    }

    // Entity → DetailResponseDto 변환
    private AccommodationDetailResponseDto convertToDetailResponseDto(Accommodation accommodation) {
        if (accommodation == null) return null;

        return AccommodationDetailResponseDto.builder()
                .id(accommodation.getId())
                .name(handleNullString(accommodation.getName()))
                .address(handleNullString(accommodation.getAddress()))
                .phoneNumber(handleNullString(accommodation.getPhoneNumber()))
                .accommodationType(accommodation.getAccommodationType())
                .latitude(accommodation.getLatitude())
                .longitude(accommodation.getLongitude())
                .summary(handleNullString(accommodation.getSummary()))
                .content(handleNullString(accommodation.getContent()))
                .homepageUrl(handleNullString(accommodation.getHomepageUrl()))
                .imageUrl(handleNullString(accommodation.getImageUrl()))
                .keywords(handleNullString(accommodation.getKeywords()))
                .createdAt(accommodation.getCreatedAt())
                .updatedAt(accommodation.getUpdatedAt())
                .build();
    }

    // [널 처리 추가] 문자열이 null인 경우 빈 문자열을 반환하여 프론트엔드 노출을 방지합니다.
    private String handleNullString(String value) {
        return (value == null || value.trim().isEmpty()) ? "" : value;
    }
}