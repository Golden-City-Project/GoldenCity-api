package com.goldencity.api.domain.tourspot.dto.response;

import com.goldencity.api.domain.tourspot.entity.AreaCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

// 클라이언트 응답용 DTO (상세 버전)
// 단건 조회 시 사용
// 자바 객체 직렬화 > 외부 자바 시스템에서도 사용할 수 있도록 바이트 데이터로 변환 > 레디스 서버에 저장할때
public class TourSpotDetailResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TourSpotDetailResponseDto() {
    }

    private TourSpotDetailResponseDto(Builder builder) {
        this.id = builder.id;
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
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static class Builder {
        private Long id;
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
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

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


        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public TourSpotDetailResponseDto build() {
            return new TourSpotDetailResponseDto(this);
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


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}