import Controller.AboutBox;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AboutBoxTest {

    @Test
    public void testShowAboutBoxDoesNotThrowException()
    {
        Frame frame = new Frame();

        assertDoesNotThrow(() -> {
            AboutBox.show(frame);
        });

        frame.dispose();
    }

    @Test
    public void testShowWithNullParent() {
        assertDoesNotThrow(() -> AboutBox.show(null), "Should not throw when parent is null");
    }

    @Test
    public void testShowWithMinimalFrame()
    {
        Frame frame = new Frame();
        assertDoesNotThrow(() -> AboutBox.show(frame), "Should show AboutBox with empty frame");
    }

    @Test
    public void testShowWithConfiguredFrame()
    {
        Frame frame = new Frame("Main Frame");
        frame.setSize(400, 300);
        assertDoesNotThrow(() -> AboutBox.show(frame), "Should show AboutBox with sized frame");
    }

    @Test
    public void testShowWhenCalledMultipleTimes()
    {
        Frame frame = new Frame();
        assertDoesNotThrow(() -> AboutBox.show(frame));
        assertDoesNotThrow(() -> AboutBox.show(frame));
    }

    @Test
    public void testShowFromAlreadyVisibleFrame() {
        Frame frame = new Frame("Visible Frame");
        frame.setVisible(true);
        assertDoesNotThrow(() -> AboutBox.show(frame));
    }
}
