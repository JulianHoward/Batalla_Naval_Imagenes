import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class RevisarXY extends Transformar {


    private int x;
    private int y;


    private final static Logger logger = (Logger) LogManager.getRootLogger();


    public RevisarXY(ImagenObjeto img, int x, int y) {
        imagenBase = img;
        this.x = x;
        this.y = y;
    }

    @Override
    public void hacer() {
        try {
            pintarPuntoXY(x, y, -1);
        } catch (StackOverflowError e) {
            logger.debug("Termina por StackOverflow");
        }
        imagenBase.cambioOk();
    }

    public void pintarPuntoXY(int m, int n, int colorBase) {

        int base = colorBase;
        int c = imagenBase.getPixel(m, n);


        if (base < 0){
            base = c;
            imagenBase.setPixel(0x00ffffff, m, n);
        }
        if ((m + 1) < imagenBase.getAncho() && imagenBase.getPixel(m + 1, n) == base)
            pintarPuntoXY(m + 1, n, base);
        if ((m - 1) >= 0 && imagenBase.getPixel(m - 1, n) == base)
            pintarPuntoXY(m - 1, n, base);

        if ((n + 1) < imagenBase.getAlto() && imagenBase.getPixel(m, n + 1) == base)
            pintarPuntoXY(m, n + 1, base);
        if ((n - 1) >= 0 && imagenBase.getPixel(m, n - 1) == base)
            pintarPuntoXY(m, n - 1, base);
    }
}




