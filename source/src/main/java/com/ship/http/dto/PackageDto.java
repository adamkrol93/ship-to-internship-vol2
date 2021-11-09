package com.ship.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import com.ship.model.Package;
import com.ship.model.Size;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PackageDto {
    @JsonProperty("rozmiar")
    private Size size;

    public static PackageDto of(Package box) {
        PackageDto dto = new PackageDto();
        dto.setSize(box.getSize());
        return dto;
    }
}
