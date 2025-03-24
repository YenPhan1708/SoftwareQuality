import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

import static java.awt.SystemColor.text;

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

public abstract class SlideItem implements SlideItemInterface
{
	private int level = 0; // level of the slideitem

	public SlideItem(int lev)
	{
		level = lev;
	}

	public SlideItem()
	{
		this(0);
	}

	public void draw(Graphics g, Rectangle area)
	{
		Style style = Style.getStyle(level); // Get style based on level
		g.setFont(style.getFont(1.0f)); // Apply font from Style
		g.setColor(style.color);
		g.drawString(String.valueOf(text), area.x + style.indent, area.y + style.leading);
	}
// Give the level
	public int getLevel()
	{
		return level;
	}

}
