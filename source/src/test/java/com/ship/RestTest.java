package com.ship;

import com.ship.http.dto.CubicleDto;
import com.ship.http.dto.OpenDto;
import com.ship.http.dto.PackageDto;
import com.ship.model.Size;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;

import static com.ship.model.Size.L;
import static com.ship.model.Size.M;
import static com.ship.model.Size.S;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
public class RestTest {

    @Autowired
    private TestRestTemplate client;

    @Test
    public void shouldInitiateIdemiomat() {
        //given
        List<List<CubicleDto>> idemiomat = List.of(
                List.of(getCubicle(S), getCubicle(S, List.of(S), "owner")),
                List.of(getCubicle(S, List.of(S), "owner"), getCubicle(S)));
        ResponseEntity<String> response = client.postForEntity(
                "/idemiomat",
                idemiomat,
                String.class);

        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //and
        ResponseEntity<List<List<CubicleDto>>> result = client.exchange(
                "/idemiomat",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });

        //then
        assertThat(result.getBody()).isEqualTo(idemiomat);
    }

    @Test
    public void shouldTakeSortedPackages() {
        //given
        List<List<CubicleDto>> idemiomat = List.of(
                List.of(getCubicle(S), getCubicle(L, List.of(M, S, S), "owner")),
                List.of(getCubicle(S, List.of(S), "owner"), getCubicle(L, List.of(L), "owner")));
        ResponseEntity<String> response = client.postForEntity(
                "/idemiomat",
                idemiomat,
                String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        //then
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //and
        ResponseEntity<List<PackageDto>> result = client.exchange(
                "/box/{userId}",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                },
                "owner");

        //then
        assertThat(result.getBody())
                .extracting(PackageDto::getSize)
                .containsExactly(S, S, S, M, L);
    }

    @Test
    public void shouldTakeOpenSigned() {
        //given
        List<List<CubicleDto>> idemiomat = List.of(
                List.of(getCubicle(S), getCubicle(L, List.of(M, S, S), "owner")),
                List.of(getCubicle(S, List.of(S), "owner"), getCubicle(L, List.of(L), "owner")));
        ResponseEntity<String> response = client.postForEntity(
                "/idemiomat",
                idemiomat,
                String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        //when
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<OpenDto> result = client.exchange(
                "/idemiomat/size",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                OpenDto.class);

        //then
        OpenDto body = result.getBody();
        assertThat(body.getSize()).isEqualTo(1);
        assertThat(body.getSignature()).isEqualTo("816DCAE8B86877DD1AA2E47FF676F35298CC5C1E6769B13755B9AE542D610C41");
    }

    private CubicleDto getCubicle(Size size) {
        return getCubicle(size, new ArrayList<>(), null);
    }

    private CubicleDto getCubicle(Size size, List<Size> sizes, String owner) {
        CubicleDto cubicleDto = new CubicleDto();
        cubicleDto.setSize(size);
        cubicleDto.setOwner(owner);
        cubicleDto.setPackages(sizes);
        return cubicleDto;
    }
}
