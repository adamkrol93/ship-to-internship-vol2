package com.ship.http.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenDto {
    private long size;

    public OpenDto() {
    }

    public OpenDto(long size) {
        this.size = size;
    }
}
