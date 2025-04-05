package Accessor;
import Style.*;
import Slide.SlideItem;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public class CollectionItem implements SlideItem
{
    private int level;
    private List<SlideItem> children = new ArrayList<>();
    private List<Style> itemStyles = new ArrayList<>();  // Store styles for each item

    public CollectionItem(int level)
    {
        this.level = level;
    }

    public void add(SlideItem item, Style style)
    {

        // Get the correct style based on the item's level
        children.add(item);
        itemStyles.add(style);  // Associate a specific style with the item
    }

    public void remove(SlideItem item)
    {
        int index = children.indexOf(item);
        if (index != -1)
        {
            children.remove(index);
            itemStyles.remove(index);  // Remove the style associated with the item
        }
    }

    @Override
    public int getLevel()
    {
        return level; // Keeps the program behavior unchanged
    }

    @Override
    public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style style)
    {
        int width = 0;
        int height = 0;

        for (int i = 0; i < children.size(); i++)
        {
            SlideItem item = children.get(i);
            Style itemStyle = itemStyles.get(i);  // Get the specific style for the item
            Rectangle itemBounds = item.getBoundingBox(g, observer, scale, itemStyle);
            if (itemBounds != null)
            {
                width = Math.max(width, itemBounds.width);  // Get the max width
                height += itemBounds.height;  // Stack items vertically
            }
        }

        return new Rectangle(0, 0, width, height);
    }

    @Override
    public void draw(int x, int y, float scale, Graphics g, Style style, ImageObserver observer)
    {

        int yOffset = y;  // Track the Y position for stacking items

        for (int i = 0; i < children.size(); i++)
        {
            SlideItem item = children.get(i);
            Style itemStyle = itemStyles.get(i);  // Get stored style for this item

            Rectangle bounds = item.getBoundingBox(g, observer, scale, itemStyle);
            item.draw(x, yOffset, scale, g, itemStyle, observer);  // Pass correct stored style
            yOffset += bounds.height;  // Move down for the next item
        }
    }

    @Override
    public void setStyle(Style style)
    {
        for (int i = 0; i < children.size(); i++)
        {
            SlideItem item = children.get(i);
            Style itemStyle = itemStyles.get(i);  // Ensure we apply the correct style per item

            if (item instanceof TextItem)
            {
                item.setStyle(itemStyle);  // Apply the specific style stored for this item
            }
            else if (item instanceof CollectionItem) {
                item.setStyle(style);  // For nested composites, propagate normally
            }
        }
    }
}
