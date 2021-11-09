package com.ship.http.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenDto {
    private long size;
    private String signature;

    public OpenDto() {
    }

    public OpenDto(long size, String signature) {
        this.size = size;
        this.signature = signature;
    }
}
