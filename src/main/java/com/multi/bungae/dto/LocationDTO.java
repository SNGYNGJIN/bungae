package com.multi.bungae.dto;

import com.multi.bungae.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationDTO {

    private String keyword;
    private String address;

    // 엔티티를 DTO로 변환하는 메서드
    public static LocationDTO fromEntity(Location location) {
        return new LocationDTO(location.getKeyword(), location.getAddress());
    }

    // DTO를 엔티티로 변환하는 메서드
    public static Location toEntity(LocationDTO locationDto) {
        return new Location(locationDto.getKeyword(), locationDto.getAddress());
    }
}
