package com.ship.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ship.model.Cubicle;
import com.ship.model.Package;
import com.ship.model.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CubicleDto {

    @JsonProperty("rozmiar")
    private Size size;

    @JsonProperty("paczki")
    private List<Size> packages;

    @JsonProperty("użytkownik")
    private String owner;

    public Cubicle toCubicle() {
        if (owner != null) {
            return new Cubicle(
                    size,
                    packages.stream()
                            .map(packageSize -> new Package(packageSize, owner))
                            .collect(Collectors.toList()),
                    owner);
        } else {
            return new Cubicle(size);
        }
    }

    public static CubicleDto of(Cubicle cubicle) {
        CubicleDto dto = new CubicleDto();
        cubicle.getOwner().ifPresent(dto::setOwner);
        dto.setPackages(cubicle.getPackages().stream().map(Package::getSize).collect(Collectors.toList()));
        dto.setSize(cubicle.getSize());
        return dto;
    }

    public Package toPackage() {
        return new Package(size, owner);
    }
}
