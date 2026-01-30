package com.goldencity.api.domain.tourspot.dto.response;

import com.goldencity.api.domain.tourspot.entity.AreaCode;
import java.io.Serializable;
import java.math.BigDecimal;

public class TourSpotResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private AreaCode areaCode;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String imageUrl;

    public TourSpotResponseDto() {
    }

    private TourSpotResponseDto(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.address = builder.address;
        this.phoneNumber = builder.phoneNumber;
        this.areaCode = builder.areaCode;
        this.latitude = builder.latitude;
        this.longitude = builder.longitude;
        this.imageUrl = builder.imageUrl;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String address;
        private String phoneNumber;
        private AreaCode areaCode;
        private BigDecimal latitude;
        private BigDecimal longitude;
        private String imageUrl;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder address(String address) { this.address = address; return this; }
        public Builder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public Builder areaCode(AreaCode areaCode) { this.areaCode = areaCode; return this; }
        public Builder latitude(BigDecimal latitude) { this.latitude = latitude; return this; }
        public Builder longitude(BigDecimal longitude) { this.longitude = longitude; return this; }
        public Builder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }

        public TourSpotResponseDto build() {
            return new TourSpotResponseDto(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getPhoneNumber() { return phoneNumber; }
    public AreaCode getAreaCode() { return areaCode; }
    public BigDecimal getLatitude() { return latitude; }
    public BigDecimal getLongitude() { return longitude; }
    public String getImageUrl() { return imageUrl; }
}