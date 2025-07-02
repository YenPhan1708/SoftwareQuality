package Slide.Factory;

import Accessor.BitmapItem;
import Slide.SlideItem;
import org.w3c.dom.Element;

public class BitmapItemLoader implements SlideItemLoaderStrategy
{
    @Override
    public SlideItem load(Element element)
    {
        int level = Integer.parseInt(element.getAttribute("level"));
        String name = element.getTextContent();
        return new BitmapItem(level, name);
    }
}
