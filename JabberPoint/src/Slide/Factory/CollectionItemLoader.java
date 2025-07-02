package Slide.Factory;

import Accessor.CollectionItem;
import Accessor.BitmapItem;
import Accessor.TextItem;
import Slide.SlideItem;
import Style.Style;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class CollectionItemLoader implements SlideItemLoaderStrategy
{
    @Override
    public SlideItem load(Element element)
    {
        int level = Integer.parseInt(element.getAttribute("level"));
        CollectionItem group = new CollectionItem(level);

        NodeList children = element.getElementsByTagName("item");

        for (int i = 0; i < children.getLength(); i++)
        {
            Element child = (Element) children.item(i);
            String kind = child.getAttribute("kind");
            int childLevel = Integer.parseInt(child.getAttribute("level"));
            String content = child.getTextContent();
            Style style = Style.getStyle(childLevel);

            switch (kind)
            {
                case "text" -> group.add(new TextItem(childLevel, content), style);
                case "image" -> group.add(new BitmapItem(childLevel, content), style);
            }
        }

        return group;
    }
}
