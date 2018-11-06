import java.util.*;
import java.awt.*;
import javax.swing.*;

public class GUI {
  private JFrame frame;
  private Container contentPane; // everything to go in GUI is placed in here

  public GUI()
  {
    frame = new JFrame("Toguz Korgool");
    frame.setResizable(true);
    frame.setPreferredSize(new Dimension(400, 400));
    frame.setMinimumSize(new Dimension(300, 300));
    contentPane = frame.getContentPane();
    frame.pack();
    frame.setVisible(true);
  }
}
