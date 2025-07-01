package Slide.Factory;

import Slide.SlideItem;
import org.w3c.dom.Element;

//OCP principle implemented
public interface SlideItemLoaderStrategy
{
    SlideItem load(Element element);
}
