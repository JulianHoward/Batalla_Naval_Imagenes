import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

public class WinMainDos {

    private final static Logger logger = (Logger) LogManager.getRootLogger();

    public static void main(String[] args) {
        FrameGeneral win = new FrameGeneral();
        win.setVisible(true);
        logger.debug("Servidor/Cliente corre frame");
    }
}
