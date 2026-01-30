package com.goldencity.api.domain.accommodation.dto.response;

import com.goldencity.api.domain.accommodation.entity.AccommodationType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

// 숙박 응답 DTO (목록 조회용)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AccommodationResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private AccommodationType accommodationType;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String imageUrl;
}