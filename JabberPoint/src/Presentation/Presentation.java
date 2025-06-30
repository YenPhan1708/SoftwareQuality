package Presentation;
import Slide.*;

import java.util.ArrayList;


/**
 * <p>Presentation.Presentation maintains the slides in the presentation.</p>
 * <p>There is only instance of this class.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class Presentation implements SlideObserver
{
	private String showTitle; // Title of the presentation
	private ArrayList<Slide> showList = null; // An ArrayList with Slides
	private int currentSlideNumber = 0; // The slide nummer of the current Slide.Slide
	private SlideViewerComponent slideViewComponent = null; // The viewcomponent of the Slides

	public Presentation()
	{
		slideViewComponent = null;
		clear();
	}

	public Presentation(SlideViewerComponent slideViewerComponent)
	{
		this.slideViewComponent = slideViewerComponent;
		clear();
	}

	public String getShowTitle()
	{
		return this.showTitle;
	}

	public void setShowTitle(String titleName)
	{
		this.showTitle = titleName;
	}

	public ArrayList<Slide> getShowList()
	{
		return this.showList;
	}

	public void setShowList(ArrayList<Slide> showList)
	{
		this.showList = showList;
	}

	// Give the number of the current slide
	public int getCurrentSlideNumber()
	{
		return this.currentSlideNumber;
	}

	public void setCurrentSlideNumber(int currentSlideNumber)
	{
		this.currentSlideNumber = currentSlideNumber;
	}

	public SlideViewerComponent getSlideViewComponent()
	{
		return this.slideViewComponent;
	}

	public void setShowView(SlideViewerComponent slideViewerComponent)
	{
		this.slideViewComponent = slideViewerComponent;
	}

	// Give the current slide
	public Slide getCurrentSlide()
	{
		return getSlide(currentSlideNumber);
	}

	// Change the current slide number and signal it to the window
	public void setSlideNumber(int number)
	{
		currentSlideNumber = number;
		if (slideViewComponent != null)
		{
			slideViewComponent.update(this, getCurrentSlide());
		}
	}

	public int getSize()
	{
		return showList.size();
	}

	// Go to the previous slide unless your at the beginning of the presentation
	public void prevSlide()
	{
		if (currentSlideNumber > 0)
		{
			setSlideNumber(currentSlideNumber - 1);
			notifySlideChange();
	    }
	}

	// Go to the next slide unless your at the end of the presentation.
	public void nextSlide()
	{
		if (currentSlideNumber < (showList.size()-1))
		{
			setSlideNumber(currentSlideNumber + 1);
			notifySlideChange();
		}
	}

	private void notifySlideChange()
	{
		getCurrentSlide().notifyObservers();
	}

	// Delete the presentation to be ready for the next one.
	public void clear()
	{
		showList = new ArrayList<Slide>();
		setSlideNumber(-1);
	}

	// Add a slide to the presentation
	public void addSlide(Slide slide)
	{
		showList.add(slide);
		// Observer 1: Presentation (business logic)
		slide.addObserver(this);

		// Observer 2: Logger (non-UI behavior)
		slide.addObserver(new LoggerObserver());

		// SlideViewerComponent is still handled via setShowView()
	}

	// Get a slide with a certain slidenumber
	public Slide getSlide(int number)
	{
		if (number < 0 || number >= getSize()){
			return null;
	    }
			return (Slide)showList.get(number);
	}

	public void exit(int n)
	{
		System.exit(n);
	}

	@Override
	public void update(Slide slide)
	{
		System.out.println("Presentation.Presentation updated");
	}
}
