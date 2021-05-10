import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Panel_Imagen extends JPanel implements PropertyChangeListener, MouseListener {

    private final static Logger logger = (Logger) LogManager.getRootLogger();
    private ImagenObjeto imagen;
    private int x;
    private int y;
    private ThreadEnviar send;
    private ThreadRecibir recive;

    public Panel_Imagen(ImagenObjeto img) {
        addMouseListener(this);
        imagen = img;
    }

    public ThreadEnviar getSend() {
        return send;
    }

    public void setSend(ThreadEnviar send) {
        this.send = send;
    }

    public ThreadRecibir getRecive() {
        return recive;
    }

    public void setRecive(ThreadRecibir recive) {
        this.recive = recive;
    }

    @Override
    public Dimension getPreferredSize() {

        return new Dimension(700, 500);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (imagen != null) {
            imagen.dibujar(g);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        x = e.getX();
        y = e.getY();
        if (send != null) {
            send.setPosicionX(x);
            send.setPosicionY(y);
            imagen.cambioOk();
            logger.debug("DISPARO: " + x + " , " + y);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


}
