package Slide.Factory;

import Accessor.TextItem;
import Slide.SlideItem;
import org.w3c.dom.Element;

public class TextItemLoader implements SlideItemLoaderStrategy
{
    @Override
    public SlideItem load(Element element)
    {
        int level = Integer.parseInt(element.getAttribute("level"));
        String text = element.getTextContent();
        return new TextItem(level, text);
    }
}
