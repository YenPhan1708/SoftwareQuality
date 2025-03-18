import java.awt.*;
import java.awt.image.ImageObserver;

public interface SlideItemInterface
{
    int getLevel();
    Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style style);
    void draw(int x, int y, float scale, Graphics g, Style style, ImageObserver observer);
}
