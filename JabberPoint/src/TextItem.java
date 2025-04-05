import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.font.TextAttribute;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.text.AttributedString;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/** <p>A tekst item.</p>
 * <p>A TextItem has drawingfunctionality.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class TextItem implements SlideItem
{
	private String text;
	private Font font;
	private Color color;
	private int level;
	private Style style;

	// A textitem of level, with the text string
	public TextItem(int level, String text)
	{
		this.level = level;
		this.text = text;
	}

	// Give the text
	public String getText()
	{
		return text == null ? "" : text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Font getFont()
	{
		return this.font;
	}

	public void setFont(Font font)
	{
		this.font = font;
	}

	public Color getColor()
	{
		return this.color;
	}

	public void setColor(Color color)
	{
		this.color = color;
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

	public Style getStyle()
	{
		return this.style;
	}

	@Override
	public void setStyle(Style style)
	{
		this.style = style;

	}

	// Give the AttributedString for the item
	public AttributedString getAttributedString(Style style, float scale)
	{
		AttributedString attrStr = new AttributedString(getText());
		attrStr.addAttribute(TextAttribute.FONT, style.getFont(scale), 0, text.length());
		return attrStr;
	}

	@Override
// give the bounding box of the item
	public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style myStyle)
	{
		List<TextLayout> layouts = getLayouts(g, myStyle, scale);
		int xsize = 0, ysize = (int) (myStyle.leading * scale);
		Iterator<TextLayout> iterator = layouts.iterator();
		while (iterator.hasNext())
		{
			TextLayout layout = iterator.next();
			Rectangle2D bounds = layout.getBounds();
			if (bounds.getWidth() > xsize)
			{
				xsize = (int) bounds.getWidth();
			}
			if (bounds.getHeight() > 0)
			{
				ysize += bounds.getHeight();
			}
			ysize += layout.getLeading() + layout.getDescent();
		}
		return new Rectangle((int) (myStyle.indent * scale), 0, xsize, ysize);
	}

	@Override
	// Draw the item
	public void draw(int x, int y, float scale, Graphics g, Style myStyle, ImageObserver o)
	{
		if (text == null || text.length() == 0)
		{
			return;
		}

		Style usedStyle = (this.style != null) ? this.style : myStyle; // Ensure we use stored style

		List<TextLayout> layouts = getLayouts(g, usedStyle, scale);
		Point pen = new Point(x + (int) (usedStyle.indent * scale),
				y + (int) (usedStyle.leading * scale));
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(usedStyle.color);  // Use the stored style color

		for (TextLayout layout : layouts)
		{
			pen.y += layout.getAscent();
			layout.draw(g2d, pen.x, pen.y);
			pen.y += layout.getDescent();
		}
	  }

	private List<TextLayout> getLayouts(Graphics g, Style s, float scale)
	{
		List<TextLayout> layouts = new ArrayList<TextLayout>();
		AttributedString attrStr = getAttributedString(s, scale);
		Graphics2D g2d = (Graphics2D) g;
		FontRenderContext frc = g2d.getFontRenderContext();
		LineBreakMeasurer measurer = new LineBreakMeasurer(attrStr.getIterator(), frc);
		float wrappingWidth = (Slide.WIDTH - s.indent) * scale;
		while (measurer.getPosition() < getText().length())
		{
			TextLayout layout = measurer.nextLayout(wrappingWidth);
			layouts.add(layout);
		}
		return layouts;
	}

	public String toString()
	{
		return "TextItem[" + getLevel()+","+getText()+"]";
	}
}
