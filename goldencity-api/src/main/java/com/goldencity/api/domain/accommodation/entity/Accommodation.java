package com.goldencity.api.domain.accommodation.entity;

import com.goldencity.api.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

// 숙박 정보 Entity
@Entity
@Table(
        name = "accommodations",
        indexes = {
                @Index(name = "idx_accommodation_name", columnList = "name"),
                @Index(name = "idx_accommodation_type", columnList = "accommodation_type")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Accommodation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 숙박업소 이름
    @Column(nullable = false, length = 200)
    private String name;

    // 주소
    @Column(length = 500)
    private String address;

    // 전화번호
    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    // 숙박 타입
    @Enumerated(EnumType.STRING)
    @Column(name = "accommodation_type", length = 30, nullable = false)
    private AccommodationType accommodationType;

    // 위도
    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    // 경도
    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    // 요약 정보 (객실수, 입실/퇴실시간, 편의시설 등)
    @Column(columnDefinition = "TEXT")
    private String summary;

    // 상세 설명
    @Column(columnDefinition = "TEXT")
    private String content;

    // 홈페이지 URL
    @Column(name = "homepage_url", length = 500)
    private String homepageUrl;

    // 이미지 URL
    @Column(name = "image_url", length = 500)
    private String imageUrl;


    // 키워드 (검색용)
    @Column(length = 500)
    private String keywords;
}