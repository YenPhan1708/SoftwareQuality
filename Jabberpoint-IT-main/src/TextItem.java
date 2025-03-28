import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.font.TextAttribute;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import java.text.AttributedString;
import java.text.AttributedCharacterIterator;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

public class TextItem implements SlideItemInterface
{
	private String text;
	private static final String EMPTYTEXT = "No Text Given";
	private Font font;
	private Color color;

	public TextItem(int level, String string)
	{
		text = string;
		this.font = new Font("Arial", Font.PLAIN, 24); // Default font
		this.color = Color.BLACK; // Default color
	}

	public TextItem()
	{
		this(0, EMPTYTEXT);
	}

	public String getText()
	{
		return text == null ? "" : text;
	}

	public Font getFont()
	{
		return this.font;
	}

	public void setFont(Font font)
	{
		this.font = font;
		System.out.println("🔹 TextItem font updated to: " + font.getFontName() + " " + font.getSize());
	}

	public Color getColor()
	{
		return this.color;
	}

	public void setColor(Color color)
	{
		this.color = color;
		System.out.println("🔹 TextItem color updated to: " + color);
	}

	public AttributedString getAttributedString(Style style, float scale)
	{
		if (text == null || text.isEmpty())
		{
			return new AttributedString(EMPTYTEXT);
		}
		AttributedString attributedString = new AttributedString(text);
		attributedString.addAttribute(TextAttribute.FONT, style.getFont(scale));
		attributedString.addAttribute(TextAttribute.SIZE, style.getFont(scale).getSize2D());
		attributedString.addAttribute(TextAttribute.FOREGROUND, style.color);
		return attributedString;
	}

	@Override
	public int getLevel()
	{
		return 0;
	}

	@Override
	public Rectangle getBoundingBox(Graphics g, ImageObserver observer, float scale, Style myStyle)
	{
		int width = 0, height = 0;
		List<TextLayout> layouts = getLayouts(g, myStyle, scale);
		for (TextLayout layout : layouts)
		{
			width = Math.max(width, (int) layout.getBounds().getWidth());
			height += layout.getAscent() + layout.getDescent() + layout.getLeading();
		}
		return new Rectangle(0, 0, width, height);
	}

	@Override
	public void draw(int x, int y, float scale, Graphics g, Style myStyle, ImageObserver observer)
	{
		if (!(this instanceof TextItem))
		{
			return; // Only apply styles to text items
		}

		Graphics2D g2d = (Graphics2D) g;
		TextItem textItem = (TextItem) this;

		// Retrieve the correct style for the text level
		Style itemStyle = Style.getStyle(getLevel());

		// Apply the correct font, size, and color
		g2d.setFont(itemStyle.getFont(scale));
		g2d.setColor(itemStyle.getColor());

		// Draw the text
		g2d.drawString(getText(), x, y);
	}

	private List<TextLayout> getLayouts(Graphics g, Style s, float scale)
	{
		List<TextLayout> layouts = new ArrayList<>();
		String textContent = getText();
		if (textContent.isEmpty())
		{
			textContent = " ";
		}

		AttributedString attrStr = new AttributedString(textContent);
		attrStr.addAttribute(TextAttribute.FONT, s.getFont(scale));
		attrStr.addAttribute(TextAttribute.SIZE, s.getFont(scale).getSize2D());
		attrStr.addAttribute(TextAttribute.FOREGROUND, s.color);

		Graphics2D g2d = (Graphics2D) g;
		FontRenderContext frc = g2d.getFontRenderContext();
		LineBreakMeasurer measurer = new LineBreakMeasurer(attrStr.getIterator(), frc);
		float wrappingWidth = (Slide.WIDTH - s.indent) * scale;

		while (measurer.getPosition() < textContent.length()) {
			layouts.add(measurer.nextLayout(wrappingWidth));
		}
		return layouts;
	}

	public String toString()
	{
		return "TextItem[" + getLevel() + "," + getText() + "]";
	}
}
