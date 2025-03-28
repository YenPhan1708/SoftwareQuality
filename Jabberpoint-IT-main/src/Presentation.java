import javax.swing.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


/**
 * <p>Presentation maintains the slides in the presentation.</p>
 * <p>There is only instance of this class.</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */

public class Presentation extends Observable
{
	private String showTitle; // title of the presentation
	private ArrayList<Slide> showList = null; // an ArrayList with Slides
	private int currentSlideNumber = 0; // the slidenummer of the current Slide
	private SlideViewerComponent slideViewComponent = null; // the viewcomponent of the Slides
	private Observer[] observers;

	public Presentation()
	{
		slideViewComponent = null;
		clear();
	}

	public Presentation(SlideViewerComponent slideViewerComponent)
	{
		this.slideViewComponent = slideViewerComponent;
		addObserver(slideViewerComponent);  // Make sure it's observing
		clear();
	}

	public ArrayList<Slide> getShowList()
	{
		return this.showList;
	}

	public void setShowList(ArrayList<Slide> showList)
	{
		this.showList = showList;
	}

	public int getSize()
	{
		return showList.size();
	}

	public String getTitle()
	{
		return showTitle;
	}

	public void setTitle(String title)
	{
		showTitle = title;
		setChanged();
		notifyObservers();
	}

	public void setShowView(SlideViewerComponent slideViewerComponent)
	{
		this.slideViewComponent = slideViewerComponent;
	}

	// give the number of the current slide
	public int getSlideNumber()
	{
		return currentSlideNumber;
	}

	// change the current slide number and signal it to the window
	public void setSlideNumber(int number)
	{
		currentSlideNumber = number;
		setChanged();  // Mark that there is a change
		notifyObservers(getCurrentSlide());  // Notify observer
	}

	// go to the previous slide unless your at the beginning of the presentation
	public void prevSlide()
	{
		if (currentSlideNumber > 0)
		{
			currentSlideNumber--;
			setChanged();
			notifyObservers(getCurrentSlide());
		}
	}

	// go to the next slide unless your at the end of the presentation.
	public void nextSlide()
	{
		if (currentSlideNumber < showList.size() - 1)
		{
			currentSlideNumber++;
			setChanged();
			notifyObservers(getCurrentSlide());
		}
	}

	// Delete the presentation to be ready for the next one.
	public void clear()
	{
		showList = new ArrayList<>();
		currentSlideNumber = -1;
		setChanged();
		notifyObservers();
	}

	// Add a slide to the presentation
	public void append(Slide slide)
	{
		showList.add(slide);
		if (currentSlideNumber == -1)
		{
			currentSlideNumber = 0;
		}
		setChanged();
		notifyObservers(slide);
	}

	// Get a slide with a certain slidenumber
	public Slide getSlide(int number)
	{
		if (number < 0 || number >= getSize())
		{
			return null;
	    }
			return (Slide)showList.get(number);
	}

	// Give the current slide
	public Slide getCurrentSlide()
	{
		System.out.println("🔹 Current Slide Index: " + currentSlideNumber);
		return (currentSlideNumber >= 0 && currentSlideNumber < showList.size()) ? showList.get(currentSlideNumber) : null;
	}

	public void exit(int n)
	{
		System.exit(n);
	}

	public void updateView()
	{
		System.out.println("🔹 updateView() called!");

		setChanged();  // Mark the observable as changed
		notifyObservers(getCurrentSlide());  // Notify observers with the current slide

		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this.slideViewComponent);
		if (frame != null) {
			frame.invalidate();
			frame.validate();
			frame.repaint();
		}
	}

}
