import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

public class LineGrinder {
    public static void main(String[] args) {
        int size = 19;
        if (args.length > 0)
            size = Integer.parseInt(args[0]);

        JFrame frame = new JFrame();
        final int FRAME_WIDTH = 600;
        final int FRAME_HEIGHT = 650;
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setTitle("LineGrinder");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        LineGrinderPanel panel = new LineGrinderPanel(size);
        frame.add(panel);

        frame.setVisible(true);
    }
}
