package com.ship.http;

import com.ship.http.dto.CubicleDto;
import com.ship.http.dto.OpenDto;
import com.ship.http.dto.PackageDto;
import com.ship.model.Idemiomat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class Api {

    public static final String BOX = "/box";
    private Idemiomat idemiomat = null;

    public static final String IDEMIOMAT = "/idemiomat";

    @PostMapping(IDEMIOMAT)
    public ResponseEntity init(@RequestBody List<List<CubicleDto>> dto) {
        if (idemiomat != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        idemiomat = new Idemiomat();
        dto.forEach(row -> idemiomat.addRow(row.stream().map(CubicleDto::toCubicle).collect(Collectors.toList())));
        return ResponseEntity.ok().build();
    }

    @GetMapping(IDEMIOMAT)
    public ResponseEntity getState() {
        if (idemiomat == null) {
            return ResponseEntity.notFound().build();
        }
        List<List<CubicleDto>> cubiclesDtos = this.idemiomat.get().stream()
                .map(row -> row.stream().map(CubicleDto::of).collect(Collectors.toList()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(cubiclesDtos);
    }

    @PutMapping(BOX)
    public ResponseEntity addPackage(@RequestBody CubicleDto dto) {
        if (idemiomat == null) {
            return ResponseEntity.notFound().build();
        }
        this.idemiomat.add(List.of(dto.toPackage()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/box/{userId}")
    public ResponseEntity takePackages(@PathVariable String userId) {
        if (idemiomat == null) {
            return ResponseEntity.notFound().build();
        }

        List<PackageDto> result = this.idemiomat.take(userId).stream().map(PackageDto::of).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/idemiomat/size")
    public ResponseEntity open()  {
        if (idemiomat == null) {
            return ResponseEntity.notFound().build();
        }

        long open = idemiomat.getOpenCount();
        return ResponseEntity.ok(new OpenDto(open));
    }
}
