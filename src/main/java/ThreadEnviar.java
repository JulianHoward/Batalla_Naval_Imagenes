import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadEnviar implements PropertyChangeListener {

    private final static Logger logger = (Logger) LogManager.getRootLogger();
    private Socket socket;
    private PrintWriter salida;
    private int posicionX;
    private int posicionY;
    private boolean banderaX;
    private boolean banderaY;

    public ThreadEnviar(String ip, int puerto) throws IOException {
        try {
            socket = new Socket(ip, puerto);
        } catch (IOException e) {
            logger.debug("¡Error! conexion mal hecha", e);
            throw e;
        }

        logger.debug("Conectado exitosamente a " + ip + ":" + puerto);

        try {
            salida = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            logger.debug("¡Error!");
            throw e;
        }
    }

    public ThreadEnviar(Socket socket) throws IOException {
        this.socket = socket;
        try {
            salida = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            logger.debug("¡Error!");
            throw e;
        }
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (banderaX && banderaY) {
            salida.println(posicionX + "," + posicionY);
            salida.flush();
            banderaX = false;
            banderaY = false;
        }
    }

    public void conexionClose() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Socket getSocket() {

        return socket;
    }

    public void setSocket(Socket socket) {

        this.socket = socket;
    }

    public PrintWriter getSalida() {

        return salida;
    }

    public void setSalida(PrintWriter salida) {

        this.salida = salida;
    }

    public void setPosicionX(int posicionX) {
        this.posicionX = posicionX;
        banderaX = true;
    }

    public void setPosicionY(int posicionY) {
        this.posicionY = posicionY;
        banderaY = true;
    }
}
