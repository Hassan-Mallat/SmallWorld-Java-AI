package vuecontroleur;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image imgBackground;
    private Image imgFront;

    // Background image
    public void setBackground(Image _imgBackground) {
        imgBackground = _imgBackground;
    }

    // Front image
    public void setFront(Image _imgFront) {
        imgFront = _imgFront;
    }
    @Override
    
    // Paint component (draw textures using Graphics)
    protected void paintComponent(Graphics graph) {
        super.paintComponent(graph);
        // frame
        graph.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 1, 1);

        if (imgBackground != null) {
            graph.drawImage(imgBackground, 2, 2, getWidth()-4, getHeight()-4, this);
        }

        if (imgFront != null) {
            int destW = (int) (getWidth() / 2);
            int destH = (int) (getHeight() / 2);
            int destX = (getWidth() - destW) / 2;
            int destY = (getHeight() - destH) / 2;
            graph.drawImage(imgFront, destX, destY, destW, destH, this);
        }


    }
}
