import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.Vector;

/** <p>A slide. This class has a drawing functionality.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class Slide
{
	public final static int WIDTH = 1200;
	public final static int HEIGHT = 800;
	protected String title; // title is saved separately
	protected Vector<SlideItemInterface> items; // slide items are saved in a Vector

	public Slide()
	{
		items = new Vector<>();
	}

	// Add a slide item
	public void append(SlideItemInterface anItem)
	{
		items.addElement(anItem);
	}

	// give the title of the slide
	public String getTitle()
	{
		return title;
	}

	// change the title of the slide
	public void setTitle(String newTitle) {
		title = newTitle;
	}

	// Create TextItem of String, and add the TextItem 
	public void append(int level, String message)
	{
		System.out.println("🔹 Added SlideItem: " + items.toString());
		append(new TextItem(level, message));
	}

	// Get slide item at index
	public SlideItemInterface getSlideItem(int number) {
		return items.elementAt(number);
	}


	// give all SlideItems in a Vector
	public Vector<SlideItemInterface> getSlideItems()
	{
		System.out.println("🔹 getSlideItems() called, items count: " + items.size());
		return items;
	}

	// give the size of the Slide
	public int getSize() {
		return items.size();
	}

	// draw the slide
	public void draw(Graphics g, Rectangle area, ImageObserver view)
	{
		float scale = getScale(area);
		int y = area.y;

		// Draw title first
		SlideItemInterface titleItem = new TextItem(0, getTitle());
		Style style = Style.getStyle(titleItem.getLevel());
		titleItem.draw(area.x, y, scale, g, style, view);
		y += titleItem.getBoundingBox(g, view, scale, style).height;

		// Draw all other items
		for (SlideItemInterface slideItem : getSlideItems())
		{
			style = Style.getStyle(slideItem.getLevel());
			slideItem.draw(area.x, y, scale, g, style, view);
			y += slideItem.getBoundingBox(g, view, scale, style).height;
		}
	}

	// Give the scale for drawing
	private float getScale(Rectangle area)
	{
		return Math.min(((float)area.width) / ((float)WIDTH), ((float)area.height) / ((float)HEIGHT));
	}
}
