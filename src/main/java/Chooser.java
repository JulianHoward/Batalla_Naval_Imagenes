import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Chooser extends Transformar {

    private final static Logger logger = (Logger) LogManager.getRootLogger();


    public Chooser(ImagenObjeto img) {
        imagenBase = img;
    }


    @Override
    public void hacer() throws IOException {
        int color;
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int returnValue = jfc.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            BufferedImage foto = null;
            foto = ImageIO.read(selectedFile);
            if (foto.getWidth() == 700 && foto.getHeight() == 500) {

                for (int i = 0; i < foto.getHeight(); i++) {
                    for (int j = 0; j < foto.getWidth(); j++) {
                        color = foto.getRGB(j, i);
                        imagenBase.setPixel(color, j, i);
                    }
                }
            } else {
                logger.debug("¡Error! Tamaño de imagen no aceptado");
                JOptionPane.showMessageDialog(null, "¡Error! Tamaño de imagen no válido");
            }
        }
    }
}
