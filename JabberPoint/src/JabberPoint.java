import Accessor.Accessor;
import Accessor.XMLAccessor;
import Presentation.Presentation;
import Slide.SlideViewerFrame;
import Style.Style;

import javax.swing.JOptionPane;
import java.io.IOException;

public class JabberPoint {

	protected static final String IOERR = "IO Error: ";
	protected static final String JABERR = "Jabberpoint Error ";
	protected static final String JABVERSION = "Jabberpoint 1.6 - OU version";

	public static void main(String argv[]) {
		Style.createStyles();
		Presentation presentation = new Presentation();
		new SlideViewerFrame(JABVERSION, presentation);

		try {
			Accessor accessor;
			if (argv.length == 0 || argv == null) {
				accessor = Accessor.getDemoAccessor(); // use Demo
			} else {
				accessor = new XMLAccessor(); // or load from XML
			}

			accessor.loadFile(presentation, argv.length > 0 ? argv[0] : "");
			presentation.setSlideNumber(0);
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, IOERR + ex, JABERR, JOptionPane.ERROR_MESSAGE);
		}
	}
}
