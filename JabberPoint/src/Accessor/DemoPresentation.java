package Accessor;

import Presentation.Presentation;
import Slide.*;

/** A built-in demo-presentation
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class DemoPresentation implements Accessor
{
	@Override
	public void loadFile(Presentation presentation, String filename)
	{
		presentation.setShowTitle("Demo Presentation.Presentation");
		// Clear existing slides before loading new ones
		presentation.clear();
		Slide slide;
		slide = new Slide();
		slide.setTitle("JabberPoint");
		slide.appendTextItem(1, "The Java Presentation.Presentation Tool");
		slide.appendTextItem(2, "Copyright (c) 1996-2000: Ian Darwin");
		slide.appendTextItem(2, "Copyright (c) 2000-now:");
		slide.appendTextItem(2, "Gert Florijn and Sylvia Stuurman");
		slide.appendTextItem(4, "Starting JabberPoint without a filename");
		slide.appendTextItem(4, "Shows this presentation");
		slide.appendTextItem(1, "Navigate:");
		slide.appendTextItem(3, "Next slide: PgDn or Enter");
		slide.appendTextItem(3, "Previous slide: PgUp or up-arrow");
		slide.appendTextItem(3, "Quit: q or Q");
		presentation.append(slide);

		slide = new Slide();
		slide.setTitle("Demonstration of levels and styles");
		slide.appendTextItem(1, "Level 1");
		slide.appendTextItem(2, "Level 2");
		slide.appendTextItem(1, "Again level 1");
		slide.appendTextItem(1, "Level 1 has style number 1");
		slide.appendTextItem(2, "Level 2 has style number  2");
		slide.appendTextItem(3, "This is how level 3 looks like");
		slide.appendTextItem(4, "And this is level 4");
		presentation.append(slide);

		slide = new Slide();
		slide.setTitle("The third slide");
		slide.appendTextItem(1, "To open a new presentation,");
		slide.appendTextItem(2, "Use File->Open from the menu.");
		slide.appendTextItem(1, " ");
		slide.appendTextItem(1, "This is the end of the presentation.");
		slide.appendSlideItem(new BitmapItem(1, "JabberPoint.jpg"));
		presentation.append(slide);
	}

	@Override
	public void saveFile(Presentation presentation, String unusedFilename)
	{
		throw new IllegalStateException("Save As->Demo! called");
	}
}
