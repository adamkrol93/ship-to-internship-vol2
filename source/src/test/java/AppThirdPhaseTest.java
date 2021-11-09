import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class AppThirdPhaseTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void shouldAddBoxesIfIdemiomat1() {
        //given
        String input = "|S:O S:O S:O M:S@xyz M:O|\n" +
                       "|L:M@xyz L:O S:S@abc M:O M:O|\n" +
                       "\n" +
                       "BS@xyz;BS@xyz;B@xyz;BL@wasd;BM@abc\n" +
                       "BS@xyz;BS@xyz;B@xyz;BS@wasd;BM@abc\n";

        //when
        App.testableMethod(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|S:O S:O S:O M:SS@xyz M:X@abc|\n" +
                                                    "|L:SSM@xyz L:X@wasd S:X@abc M:O M:O|\n" +
                                                    "|S:X@xyz S:X@xyz S:X@xyz M:SS@xyz M:X@abc|\n" +
                                                    "|L:SSM@xyz L:X@wasd S:X@abc M:S@wasd M:X@abc|\n");
    }

    @Test
    public void shouldTakeBoxesIfIdemiomat1() {
        //given
        String input = "|S:O S:O S:O M:S@xyz M:O|\n" +
                       "|L:M@xyz L:O S:S@abc M:O M:O|\n" +
                       "\n" +
                       "BS@xyz;BS@xyz;B@xyz;BL@wasd;BM@abc\n" +
                       "Txyz;Twasd\n" +
                       "BS@xyz;BS@xyz;B@xyz;BS@wasd;BM@abc\n" +
                       "Txyz;Tabc\n";

        //when
        App.testableMethod(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|S:O S:O S:O M:SS@xyz M:X@abc|\n" +
                                                    "|L:SSM@xyz L:X@wasd S:X@abc M:O M:O|\n" +
                                                    "|S:O S:O S:O M:O M:X@abc|\n" +
                                                    "|L:O L:O S:X@abc M:O M:O|\n" +
                                                    "|S:X@xyz S:X@xyz S:X@xyz M:S@wasd M:X@abc|\n" +
                                                    "|L:O L:O S:X@abc M:X@abc M:O|\n" +
                                                    "|S:O S:O S:O M:S@wasd M:O|\n" +
                                                    "|L:O L:O S:O M:O M:O|\n");
    }
}