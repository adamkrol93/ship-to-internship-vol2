import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class AppSecondPhaseTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void shouldAddBoxesIfIdemiomat1() {
        //given
        String input = "|S:O S:O S:O M:O M:O|\n" +
                       "|L:O L:O S:X M:X M:O|\n" +
                       "BM;BS;B;BL;BS;BM;BS;BS\n";

        //when
        App.testableMethod(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|S:X S:X S:X M:X M:X|\n" +
                                                    "|L:X L:S S:X M:X M:S|\n");
    }

    @Test
    public void shouldAddBoxesIfIdemiomat2() {
        //given
        String input = "|S:O S:O S:O M:O M:O|\n" +
                       "|L:O L:O S:X M:X M:O|\n" +
                       "|L:O L:O S:X M:X M:O|\n" +
                       "B;B;B;B;B;B;B;B;B;B;B\n";

        //when
        App.testableMethod(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|S:X S:X S:X M:S M:S|\n" +
                                                    "|L:S L:S S:X M:X M:S|\n" +
                                                    "|L:S L:S S:X M:X M:S|\n");
    }

    @Test
    public void shouldAddBoxesIfIdemiomat3() {
        //given
        String input = "|S:O S:O S:O M:O M:O|\n" +
                       "|L:O L:O S:X M:X M:O|\n" +
                       "|L:O L:O S:X M:X M:O|\n" +
                       "BM;BM;BM;BM;BM;BM;BM;BM;BM;BM;BM\n";

        //when
        App.testableMethod(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|S:O S:O S:O M:X M:X|\n" +
                                                    "|L:M L:M S:X M:X M:X|\n" +
                                                    "|L:M L:M S:X M:X M:X|\n");
    }

    @Test
    public void shouldAddBoxesIfIdemiomat4() {
        //given
        String input = "|S:O S:O S:O M:O M:O|\n" +
                       "|L:O L:O S:X M:X M:O|\n" +
                       "|L:O L:O S:X M:X M:O|\n" +
                       "BL;BL;BL;BL;BL;BL;BL;BL;BL;BL;BL\n";

        //when
        App.testableMethod(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|S:O S:O S:O M:O M:O|\n" +
                                                    "|L:X L:X S:X M:X M:O|\n" +
                                                    "|L:X L:X S:X M:X M:O|\n");
    }

    @Test
    public void shouldAddBoxesIfIdemiomat5() {
        //given
        String input = "|S:O S:O S:O M:O M:O|\n" +
                       "|L:O L:O S:X M:X M:O|\n" +
                       "|L:O L:O S:X M:X M:O|\n" +
                       "BL;BL\n" +
                       "BL;BL\n";

        //when
        App.testableMethod(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|S:O S:O S:O M:O M:O|\n" +
                                                    "|L:X L:X S:X M:X M:O|\n" +
                                                    "|L:O L:O S:X M:X M:O|\n" +
                                                    "|S:O S:O S:O M:O M:O|\n" +
                                                    "|L:X L:X S:X M:X M:O|\n" +
                                                    "|L:X L:X S:X M:X M:O|\n");
    }

    @Test
    public void shouldAddBoxesIfIdemiomat6() {
        //given
        String input = "|S:X M:X L:X|\n" +
                "|L:O S:X S:X|\n" +
                "|M:S M:O S:X|\n" +
                "|L:X M:X S:X|\n" +
                "BM;BS\n";

        //when
        App.testableMethod(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|S:X M:X L:X|\n" +
                "|L:S S:X S:X|\n" +
                "|M:S M:X S:X|\n" +
                "|L:X M:X S:X|\n");
    }
}