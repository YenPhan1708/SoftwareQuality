package Slide.Factory;

import Slide.SlideItem;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

public class SlideItemFactory
{

    private static final Map<String, SlideItemLoaderStrategy> loaders = new HashMap<>();

    static
    {
        loaders.put("text", new TextItemLoader());
        loaders.put("image", new BitmapItemLoader());
        loaders.put("collection", new CollectionItemLoader());
    }

    public static SlideItem create(String type, Element element) {
        SlideItemLoaderStrategy loader = loaders.get(type.toLowerCase());
        if (loader != null)
        {
            return loader.load(element);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported item type: " + type);
        }
    }
}
