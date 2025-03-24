import javax.swing.*;
import java.awt.*;

public class JFontChooser extends JDialog
{
    private JList<String> fontList;
    private JSpinner fontSizeSpinner;
    private JButton colorButton;
    private Color selectedColor;
    private Font selectedFont;
    private int result = CANCEL_OPTION;

    public static final int OK_OPTION = 1;
    public static final int CANCEL_OPTION = 0;

    public JFontChooser()
    {
        setTitle("Choose Font");
        setSize(400, 300);
        setLayout(new GridLayout(3, 1));

        // Font selection
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontList = new JList<>(fonts);
        add(new JScrollPane(fontList));

        // Font size selection
        fontSizeSpinner = new JSpinner(new SpinnerNumberModel(24, 8, 72, 1));
        add(fontSizeSpinner);

        // Color selection
        colorButton = new JButton("Select Color");
        colorButton.addActionListener(e -> {
            selectedColor = JColorChooser.showDialog(this, "Choose Text Color", Color.BLACK);
        });
        add(colorButton);

        // OK & Cancel buttons
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            selectedFont = new Font(fontList.getSelectedValue(), Font.PLAIN, (int) fontSizeSpinner.getValue());
            result = OK_OPTION;
            dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel);
    }

    public int showDialog(Component parent)
    {
        setLocationRelativeTo(parent);
        setVisible(true);
        return result;
    }

    public Font getSelectedFont()
    {
        return selectedFont;
    }

    public Color getSelectedColor()
    {
        return selectedColor;
    }
}
