package com.ship.http;

import com.ship.http.dto.CubicleDto;
import com.ship.http.dto.OpenDto;
import com.ship.http.dto.PackageDto;
import com.ship.model.Idemiomat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import static javax.xml.crypto.dsig.SignatureMethod.HMAC_SHA512;

@RestController
@Slf4j
public class Api {

    private static final String KEY = "0123456789ABCDEF";

    public static final String BOX = "/box";
    public static final String IDEMIOMAT = "/idemiomat";

    private Idemiomat idemiomat = null;

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
    public ResponseEntity open() throws NoSuchAlgorithmException, InvalidKeyException {
        if (idemiomat == null) {
            return ResponseEntity.notFound().build();
        }

        long open = idemiomat.getOpenCount();
        log.info("Print: {}", longToBytes(open));
        log.info("Print: {}", longToBytes(open));
        SecretKeySpec sk = new SecretKeySpec(hexStringToByteArray(KEY), HMAC_SHA512);
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(sk);
        byte[] result = mac.doFinal(longToBytes(open));

        return ResponseEntity.ok(new OpenDto(open, bytesToHex(result)));
    }

    private byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                  + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);

    public static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }
}
