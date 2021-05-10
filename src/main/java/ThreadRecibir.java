import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ThreadRecibir implements Runnable {


    private final static Logger logger = (Logger) LogManager.getRootLogger();
    private ImagenObjeto imagen;
    private Socket cliente;
    private BufferedReader entrada;

    public ThreadRecibir(int port, ImagenObjeto imagen) throws IOException {
        ServerSocket serverSocket = null;
        cliente = null;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            logger.debug("¡Error! No se pudo hacer la conexion satisfactoriamente", e);
            throw e;
        }

        logger.debug("Esperando conexion puerto: " + port);

        try {
            cliente = serverSocket.accept();
        } catch (Exception e) {
            logger.debug("¡Error! Tiempo de espera de conexión", e);
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                logger.debug("¡Error! Cerrando el servidor");
            }
        }

        logger.debug("Conexion satisfactoria a servidor ");

        entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        this.imagen = imagen;
    }

    public ThreadRecibir(Socket socket, ImagenObjeto imagen) throws IOException {
        cliente = socket;
        entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        this.imagen = imagen;
    }


    @Override
    public void run() {
        try {
            while (!cliente.isClosed()) {
                String enXY = entrada.readLine();
                String[] pos = enXY.split(",");
                int x = Integer.parseInt(pos[0]);
                int y = Integer.parseInt(pos[1]);
                logger.debug("Coordenadas: " + x + " , " + y);
                RevisarXY pintar = new RevisarXY(imagen, x, y);
                pintar.hacer();
            }
        } catch (IOException e) {
            logger.debug("¡Error!");
        }
    }


    public ImagenObjeto getImagen() {

        return imagen;
    }

    public void setImagen(ImagenObjeto imagen) {

        this.imagen = imagen;
    }

    public Socket getCliente() {

        return cliente;
    }

    public void setCliente(Socket cliente) {

        this.cliente = cliente;
    }

    public BufferedReader getEntrada() {

        return entrada;
    }

    public void setEntrada(BufferedReader entrada) {

        this.entrada = entrada;
    }
}
