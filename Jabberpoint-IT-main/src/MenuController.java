import java.awt.MenuBar;
import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.*;

/** <p>The controller for the menu</p>
 * @author Ian F. Darwin, ian@darwinsys.com, Gert Florijn, Sylvia Stuurman
 * @version 1.1 2002/12/17 Gert Florijn
 * @version 1.2 2003/11/19 Sylvia Stuurman
 * @version 1.3 2004/08/17 Sylvia Stuurman
 * @version 1.4 2007/07/16 Sylvia Stuurman
 * @version 1.5 2010/03/03 Sylvia Stuurman
 * @version 1.6 2014/05/16 Sylvia Stuurman
 */
public class MenuController extends MenuBar {

	private Frame parent; // the frame, only used as parent for the Dialogs
	private Presentation presentation; // Commands are given to the presentation

	private static final long serialVersionUID = 227L;

	protected static final String ABOUT = "About";
	protected static final String FILE = "File";
	protected static final String EXIT = "Exit";
	protected static final String GOTO = "Go to";
	protected static final String HELP = "Help";
	protected static final String NEW = "New";
	protected static final String NEXT = "Next";
	protected static final String OPEN = "Open";
	protected static final String PAGENR = "Page number?";
	protected static final String PREV = "Prev";
	protected static final String SAVE = "Save";
	protected static final String VIEW = "View";
	protected static final String ADD_TEXT = "Add Text";
	protected static final String ADD_IMAGE = "Add Image";


	protected static final String TESTFILE = "test.xml";
	protected static final String SAVEFILE = "dump.xml";

	protected static final String IOEX = "IO Exception: ";
	protected static final String LOADERR = "Load Error";
	protected static final String SAVEERR = "Save Error";

	public MenuController(Frame frame, Presentation pres)
	{
		parent = frame;
		presentation = pres;
		MenuItem menuItem;
		Menu fileMenu = new Menu(FILE);
		fileMenu.add(menuItem = mkMenuItem(OPEN));
		menuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent actionEvent)
			{
				presentation.clear();
				Accessor xmlAccessor = new XMLAccessor();
				try
				{
					xmlAccessor.loadFile(presentation, TESTFILE);
					presentation.setSlideNumber(0);
				}
				catch (IOException exc)
				{
					JOptionPane.showMessageDialog(parent, IOEX + exc,
							LOADERR, JOptionPane.ERROR_MESSAGE);
				}
				parent.repaint();
			}
		} );
		fileMenu.add(menuItem = mkMenuItem(NEW));
		menuItem.addActionListener(e -> {
			int response = JOptionPane.showConfirmDialog(
					parent,
					"Create a new presentation? Unsaved changes will be lost.",
					"New Presentation",
					JOptionPane.YES_NO_OPTION
			);

			if (response == JOptionPane.YES_OPTION)
			{
				presentation.clear();
				presentation.setTitle("New Presentation");

				// Ensure at least one blank slide is created
				Slide newSlide = new Slide();
				newSlide.setTitle("Untitled Slide"); // Default title
				presentation.append(newSlide);

				parent.repaint();
			}
		});

		fileMenu.add(menuItem = mkMenuItem(SAVE));
		menuItem.addActionListener(e -> {
			try
			{
				new XMLAccessor().saveFile(presentation, SAVEFILE);
			}
			catch (IOException exc)
			{
				JOptionPane.showMessageDialog(parent, IOEX + exc, SAVEERR, JOptionPane.ERROR_MESSAGE);
			}
		});
		fileMenu.addSeparator();
		fileMenu.add(menuItem = mkMenuItem(EXIT));
		menuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent actionEvent)
			{
				presentation.exit(0);
			}
		});
		add(fileMenu);

		Menu editMenu = new Menu("Edit");

		MenuItem addSlideMenuItem = mkMenuItem("New Slide");
		addSlideMenuItem.addActionListener(e -> addNewSlide());
		editMenu.add(addSlideMenuItem);

		editMenu.add(menuItem = mkMenuItem(ADD_TEXT));
		menuItem.addActionListener(e -> addText());

		editMenu.add(menuItem = mkMenuItem(ADD_IMAGE));
		menuItem.addActionListener(e -> addImage());

		add(editMenu);
		Menu viewMenu = new Menu(VIEW);
		viewMenu.add(menuItem = mkMenuItem(NEXT));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				presentation.nextSlide();
			}
		});
		viewMenu.add(menuItem = mkMenuItem(PREV));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				presentation.prevSlide();
			}
		});
		viewMenu.add(menuItem = mkMenuItem(GOTO));
		menuItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent actionEvent)
			{
				String pageNumberStr = JOptionPane.showInputDialog((Object)PAGENR);
				int pageNumber = Integer.parseInt(pageNumberStr);
				presentation.setSlideNumber(pageNumber - 1);
			}
		});
		add(viewMenu);
		Menu helpMenu = new Menu(HELP);
		helpMenu.add(menuItem = mkMenuItem(ABOUT));
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				AboutBox.show(parent);
			}
		});
		setHelpMenu(helpMenu);		// needed for portability (Motif, etc.).
	}

	// create a menu item
	public MenuItem mkMenuItem(String name)
	{
		return new MenuItem(name, new MenuShortcut(name.charAt(0)));
	}

	private void addText()
	{
		String text = JOptionPane.showInputDialog(parent, "Enter text:");
		if (text != null && !text.isEmpty())
		{
			Slide currentSlide = presentation.getCurrentSlide();
			if (currentSlide == null)
			{
				currentSlide = new Slide();
				presentation.append(currentSlide);
			}
			currentSlide.append(new TextItem(2, text));
			parent.repaint();
		}
	}

	private void addImage()
	{
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(parent);
		if (result == JFileChooser.APPROVE_OPTION)
		{
			String imagePath = fileChooser.getSelectedFile().getAbsolutePath();
			Slide currentSlide = presentation.getCurrentSlide();
			if (currentSlide == null)
			{
				currentSlide = new Slide();
				presentation.append(currentSlide);
			}
			currentSlide.append(new BitmapItem(2, imagePath));
			parent.repaint();
		}
	}

	private void addNewSlide()
	{
		Slide newSlide = new Slide();
		newSlide.setTitle("New Slide");

		presentation.append(newSlide);
		presentation.setSlideNumber(presentation.getSize() - 1); // Move to the new slide
		parent.repaint();
	}

}