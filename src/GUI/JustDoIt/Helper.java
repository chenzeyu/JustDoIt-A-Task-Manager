package GUI.JustDoIt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/*A command input helper which gives user a example of how to enter various commands*/
class Helper {

	Shell helper;
	Text text;
	private static Helper singleton;

	public Helper(Shell shell, Display display, Image image, Color white,
			int fontSize) {
		helper = new Shell(shell, SWT.NO_TRIM);
		helper.setBackgroundImage(image);
		this.initialize();
		text = new Text(helper, SWT.READ_ONLY);
		helper.setSize(480, 33);
		helper.setText("Helper");
		text.setLocation(37, 7);
		text.setBackground(new Color(Display.getCurrent(), 0, 0, 0));
		text.setSize(480, 25);
		text.setFont(new Font(display, "Arial", fontSize, SWT.NONE));
		text.setForeground(white);

	}

	public static Helper getInstance(Shell shell, Display display, Image image,
			Color white, int fontSize) {
		if (singleton == null) {
			singleton = new Helper(shell, display, image, white, fontSize);
		}
		return singleton;
	}

	public void setLocation(int x, int y) {
		helper.setLocation(x, y);
	}

	public void initialize() {
		helper.open();
	}

	public void hide() {
		helper.setVisible(false);
	}

	public void show() {
		helper.setVisible(true);
	}

	public void setText(String input) {
		text.setText(input);
	}

	public Shell getHelperShell() {
		return helper;
	}
}