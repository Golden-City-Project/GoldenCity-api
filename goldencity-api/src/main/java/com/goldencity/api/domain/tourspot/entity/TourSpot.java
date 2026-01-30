package com.goldencity.api.domain.tourspot.entity;

import com.goldencity.api.domain.common.entity.BaseEntity;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalTime;

// 관광지 정보 Entity
// Redis 캐싱을 위해 Serializable 구현 (BaseEntity에서 상속)
@Entity
@Table(
        name = "tour_spots",
        indexes = {
                @Index(name = "idx_name", columnList = "name"),
                @Index(name = "idx_area_code", columnList = "area_code")
        }
)
public class TourSpot extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 관광지 이름
    @Column(nullable = false, length = 200)
    private String name;

    // 주소
    @Column(length = 500)
    private String address;

    // 전화번호
    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    // 권역 코드
    @Enumerated(EnumType.STRING)
    @Column(name = "area_code", length = 20)
    private AreaCode areaCode;

    // 위도
    @Column(precision = 10, scale = 7)
    private BigDecimal latitude;

    // 경도
    @Column(precision = 10, scale = 7)
    private BigDecimal longitude;

    // 상세 설명
    @Column(columnDefinition = "TEXT")
    private String content;

    // 입장권 판매 시작 시간
    @Column(name = "ticket_start")
    private LocalTime ticketStart;

    // 입장권 판매 종료 시간
    @Column(name = "ticket_end")
    private LocalTime ticketEnd;

    // 관광지 개장 시간
    @Column(name = "open_time")
    private LocalTime openTime;

    // 관광지 폐장 시간
    @Column(name = "close_time")
    private LocalTime closeTime;

    // 이미지 URL
    @Column(name = "image_url", length = 500)
    private String imageUrl;


    protected TourSpot() {
    }

    private TourSpot(Builder builder) {
        this.name = builder.name;
        this.address = builder.address;
        this.phoneNumber = builder.phoneNumber;
        this.areaCode = builder.areaCode;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.content = builder.content;
        this.ticketStart = builder.ticketStart;
        this.ticketEnd = builder.ticketEnd;
        this.openTime = builder.openTime;
        this.closeTime = builder.closeTime;
        this.imageUrl = builder.imageUrl;
    }

    public static class Builder {
        private String name;
        private String address;
        private String phoneNumber;
        private AreaCode areaCode;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String content;
        private LocalTime ticketStart;
        private LocalTime ticketEnd;
        private LocalTime openTime;
        private LocalTime closeTime;
        private String imageUrl;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder areaCode(AreaCode areaCode) {
            this.areaCode = areaCode;
            return this;
        }

        public Builder latitude(BigDecimal latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(BigDecimal longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder ticketStart(LocalTime ticketStart) {
            this.ticketStart = ticketStart;
            return this;
        }

        public Builder ticketEnd(LocalTime ticketEnd) {
            this.ticketEnd = ticketEnd;
            return this;
        }

        public Builder openTime(LocalTime openTime) {
            this.openTime = openTime;
            return this;
        }

        public Builder closeTime(LocalTime closeTime) {
            this.closeTime = closeTime;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }


        public TourSpot build() {
            return new TourSpot(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public AreaCode getAreaCode() {
        return areaCode;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public String getContent() {
        return content;
    }

    public LocalTime getTicketStart() {
        return ticketStart;
    }

    public LocalTime getTicketEnd() {
        return ticketEnd;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public LocalTime getCloseTime() {
        return closeTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}