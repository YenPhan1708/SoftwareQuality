import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AboutBoxTest {

    @Test
    public void testShowAboutBoxDoesNotThrowException() {
        Frame frame = new Frame();

        assertDoesNotThrow(() -> {
            AboutBox.show(frame);
        });

        frame.dispose();
    }
}
