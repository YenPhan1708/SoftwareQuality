import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

/** <p>The abstract class for an item on a slide<p>
 * <p>All SlideItems have drawingfunctionality.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
*/

public class SlideItem implements SlideItemInterface
{
	private List<SlideItemInterface> children = new ArrayList<>();
	private int level = 0; // level of the slideitem

	public SlideItem(int lev)
	{
		this.level = lev;
	}

	public SlideItem()
	{
		this(0);
	}

	public List<SlideItemInterface> getChildren()
	{
		return this.children;
	}

	public void setChildren(List<SlideItemInterface> children)
	{
		this.children = children;
	}

	@Override
	public int getLevel()
	{
		return this.level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	public void add(SlideItemInterface item)
	{
		children.add(item);
	}

	public void remove(SlideItemInterface item)
	{
		children.remove(item);
	}

	@Override
	public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style style) {
		int width = 0, height = 0;
		for (SlideItemInterface item : children)
		{
			Rectangle itemBox = item.getBoundingBox(g, observer, scale, style);
			width = Math.max(width, itemBox.width);
			height += itemBox.height;
		}
		return new Rectangle(0, 0, width, height);
	}

	@Override
	public void draw(int x, int y, float scale, Graphics g, Style style, ImageObserver observer)
	{
		int currentY = y;
		for (SlideItemInterface item : children)
		{
			Style itemStyle = Style.getStyle(item.getLevel()); // Ensure each item gets its own style
			item.draw(x, currentY, scale, g, itemStyle, observer);
			currentY += item.getBoundingBox(g, observer, scale, style).height;
		}
	}
}
