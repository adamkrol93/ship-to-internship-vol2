import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void shouldAddBoxesIfIdemiomat1() {
        //given
        String input = "|S:O S:O|\n" +
                       "B;B\n" +
                       "S\n";

        //when
        App.testableMethod(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|S:X S:X|\n" +
                                                    "|S:X S:X|\n");
    }

    @Test
    public void shouldAddBoxesIfIdemiomat2() {
        //given
        String input = "|S:X S:O|\n" +
                       "|S:O S:X|\n" +
                       "B;B\n";

        //when
        App.testableMethod(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|S:X S:X|\n" +
                                                    "|S:X S:X|\n");
    }

    @Test
    public void shouldAddBoxesIfIdemiomat3() {
        //given
        String input = "|S:X S:O S:O|\n" +
                       "|S:O S:X S:O|\n" +
                       "|S:O S:X S:O|\n" +
                       "|S:O S:X S:O|\n" +
                       "B;B;B;B;B\n";

        //when
        App.testableMethod(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)), new PrintStream(outContent));

        //then
        assertThat(outContent.toString()).isEqualTo("|S:X S:X S:X|\n" +
                                                    "|S:X S:X S:X|\n" +
                                                    "|S:X S:X S:O|\n" +
                                                    "|S:O S:X S:O|\n");
    }
}