package com.goldencity.api.domain.accommodation.dto.response;

import com.goldencity.api.domain.accommodation.entity.AccommodationType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// 숙박 상세 응답 DTO
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AccommodationDetailResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private AccommodationType accommodationType;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String summary;
    private String content;
    private String homepageUrl;
    private String imageUrl;
    private String keywords;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}