import java.awt.*;
import javax.swing.JComponent;
import javax.swing.JFrame;


/** <p>SlideViewerComponent is a graphical component that can show slides.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class SlideViewerComponent extends JComponent implements SlideObserver
{
		
	private Slide slide; // current slide
	private Font labelFont = null; // font for labels
	private Presentation presentation = null; // the presentation
	private JFrame frame = null;
	
	private static final long serialVersionUID = 227L;
	
	private static final Color BGCOLOR =  Color.white;
	private static final Color COLOR = Color.black;
	private static final String FONTNAME = "Dialog";
	private static final int FONTSTYLE = Font.BOLD;
	private static final int FONTHEIGHT = 10;
	private static final int XPOS = 1100;
	private static final int YPOS = 20;

	public SlideViewerComponent(Presentation pres, JFrame frame)
	{
		setBackground(BGCOLOR); 
		presentation = pres;
		labelFont = new Font(FONTNAME, FONTSTYLE, FONTHEIGHT);
		this.frame = frame;
	}

	public void observeSlide(Slide slide1)
	{
		if (slide != null)
		{
			slide.removeObserver(this);
		}
		slide = slide1;
		slide.addObserver(this);
	}

	public Dimension getPreferredSize()
	{
		return new Dimension(Slide.WIDTH, Slide.HEIGHT);
	}

	public void update(Presentation presentation, Slide data)
	{
		if (data == null)
		{
			repaint();
			return;
		}
		this.presentation = presentation;
		this.slide = data;
		repaint();
		frame.setTitle(presentation.getTitle());
	}

// draw the slide
	public void paintComponent(Graphics g)
	{
		Color pastelPink = new Color(237, 176, 200); // Pastel Pink for background
		Color whiteColor = Color.WHITE; // White for main content
		Color borderColor = Color.LIGHT_GRAY; // Slightly darker pink for border

		// Fill Background
		g.setColor(pastelPink);
		g.fillRect(0, 0, getWidth(), getHeight());

		// Define Main Content Area
		int margin = 35;  // Space around the main content
		int borderRadius = 7; // Curve radius

		int contentX = margin;
		int contentY = margin;
		int contentWidth = getWidth() - 2 * margin;
		int contentHeight = getHeight() - 2 * margin;

		// Draw Main Content Background
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(whiteColor);
		g2.fillRoundRect(contentX, contentY, contentWidth, contentHeight, borderRadius * 2, borderRadius * 2);

		// Draw Border
		g2.setColor(borderColor);
		g2.setStroke(new BasicStroke(2)); // Border thickness
		g2.drawRoundRect(contentX, contentY, contentWidth, contentHeight, borderRadius * 2, borderRadius * 2);

		// Draw Slide Content Inside (Existing Code)
		if (presentation.getSlideNumber() >= 0 && slide != null) {
			g2.setFont(labelFont);
			g2.setColor(COLOR);

			// Move slide number to bottom-right of the main content
			String slideText = "Slide " + (1 + presentation.getSlideNumber()) + " of " + presentation.getSize();
			FontMetrics fm = g2.getFontMetrics();
			int textWidth = fm.stringWidth(slideText);
			int textHeight = fm.getHeight();

			int textX = contentX + contentWidth - textWidth - 30; // 10px padding from right
			int textY = contentY + contentHeight - textHeight + fm.getAscent() - 30; // 10px padding from bottom

			g2.drawString(slideText, textX, textY);

			// Draw Slide Content
			Rectangle area = new Rectangle(contentX + 10, contentY + 30, contentWidth - 20, contentHeight - 40);
			slide.draw(g2, area, this);
		}
	}

	@Override
	public void update(Slide slide)
	{
		System.out.println("SlideViewerComponent updated: Displaying new slide");

	}
}
