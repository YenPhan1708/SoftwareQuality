import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.util.Observer;
import java.util.Observable;


/** <p>SlideViewerComponent is a graphical component that can show slides.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class SlideViewerComponent extends JComponent implements Observer
{
		
	private Slide slide; // current slide
	private Font labelFont = null; // font for labels
	private Presentation presentation = null; // the presentation
	private JFrame frame = null;
	
	private static final long serialVersionUID = 227L;
	
	private static final Color BGCOLOR = Color.white;
	private static final Color COLOR = Color.black;
	private static final String FONTNAME = "Dialog";
	private static final int FONTSTYLE = Font.BOLD;
	private static final int FONTHEIGHT = 10;
	private static final int XPOS = 1100;
	private static final int YPOS = 20;

	public SlideViewerComponent(Presentation pres, JFrame frame)
	{
		System.out.println("🔹 SlideViewerComponent initialized!");
		setBackground(BGCOLOR); 
		presentation = pres;
		labelFont = new Font(FONTNAME, FONTSTYLE, FONTHEIGHT);
		this.frame = frame;
		presentation.addObserver(this);
	}

	public Dimension getPreferredSize()
	{
		return new Dimension(Slide.WIDTH, Slide.HEIGHT);
	}


// draw the slide
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (presentation == null || presentation.getCurrentSlide() == null) {
			System.out.println("DEBUG: No presentation or slide to render.");
			return;
		}
		int x = 50;  // Example X position for text
		int y = 100; // Start drawing items at this Y position
		float scale = 1.0f;

		System.out.println("DEBUG: Rendering slide " + presentation.getCurrentSlide() + " with " +
				presentation.getCurrentSlide().getSlideItems().size() + " items.");

		for (SlideItemInterface item : presentation.getCurrentSlide().getSlideItems())
		{
			Style style = Style.getStyle(item.getLevel());
			System.out.println("DEBUG: Drawing item at level " + item.getLevel() + " with text: " + item.toString());
			item.draw(x, y, scale, g, style, this);
			y += 50;  // Move down for next item		}
		}
	}

	@Override
	public void update(Observable observable, Object arg)
	{
		System.out.println("DEBUG: Updating SlideViewerComponent - Slide #" + presentation.getSlideNumber());

		if (arg instanceof Slide)
		{
			this.slide = (Slide) arg;
			repaint();
			frame.setTitle(presentation.getTitle());
		}
	}
}
