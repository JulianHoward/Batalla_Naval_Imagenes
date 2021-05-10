import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class FrameGeneral extends JFrame {

    private ImagenObjeto modelo;
    private final static Logger logger = (Logger) LogManager.getRootLogger();
    private ThreadEnviar send;
    private ThreadRecibir recive;
    private Panel_Imagen panel;

    public FrameGeneral() {
        setTitle("BattleField");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        init();
    }

    private void init() {
        modelo = new ImagenObjeto(700, 500);

        panel = new Panel_Imagen(modelo);

        modelo.addObserver(panel);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(panel, BorderLayout.CENTER);

        JMenuBar bar = new JMenuBar();

        JMenu menu1 = new JMenu("Start");
        JMenu menu2 = new JMenu("Help");

        JMenuItem item1 = new JMenuItem("Cargar imagen");
        item1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Servidor/Cliente carga la imagen");
                Chooser ch = new Chooser(modelo);
                try {
                    ch.hacer();
                    modelo.cambioOk();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        menu1.add(item1);

        JMenuItem item2 = new JMenuItem("Esperar conexion");
        item2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Servidor espera y establece conexión");
                paraEsperarConectar();
            }
        });
        menu1.add(item2);

        JMenuItem item3 = new JMenuItem("Conectarse");
        item3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Cliente establece conexión con el servidor");
                paraConectar();
            }
        });
        menu1.add(item3);

        JMenuItem item4 = new JMenuItem("About");
        item4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Servidor/Cliente lee las reglas del juego");
                JOptionPane.showMessageDialog(rootPane, "Reglas del juego:\n " +
                        " 1.- Presionar boton 'Start'.\n " +
                        " 2.- Presionar boton 'Esperar conexion'(si es servidor) o 'Conectarse'(si es cliente).\n " +
                        " 3.- Ingresar el número del puerto en el que esperará conexion.\n " +
                        " 4.- Presionar boton 'Ok'.");
            }
        });
        menu2.add(item4);

        JMenuItem item5 = new JMenuItem("Exit");
        item5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.debug("Servidor/Cliente termina el juego");
                int answer = JOptionPane.showConfirmDialog(rootPane, "¿Seguro que desea finalizar esta increible app?", "Cerra app", JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION)
                    System.exit(0);
            }
        });
        menu2.add(item5);

        bar.add(menu1);
        bar.add(menu2);
        this.setJMenuBar(bar);
        this.pack();
    }

    private void paraEsperarConectar() {
        logger.debug("Se pide un puerto para esperar la conexión");

        String puerto = JOptionPane.showInputDialog("Ingrese en qué puerto escuchará:");

        if (puerto == null)
            return;

        int port = readPuerto(puerto);
        if (port == 0) {
            JOptionPane.showMessageDialog(rootPane, "El puerto que ingrese debe ser un número mayor a 1024");
            logger.debug("¡Error! número de puerto no válido");
            return;
        }

        try {
            recive = new ThreadRecibir(port, modelo);
            panel.setRecive(recive);
        } catch (Exception e) {
            logger.debug("¡Error!");
            JOptionPane.showMessageDialog(rootPane, "¡Error!");
            return;
        }

        try {
            send = new ThreadEnviar(recive.getCliente());
            panel.setSend(send);
        } catch (IOException e) {
            logger.debug("¡Error!");
            JOptionPane.showMessageDialog(rootPane, "¡Error! Intente nuevamente");
            return;
        }

        Thread recibir = new Thread(recive);
        recibir.start();
        nuevoObserver();
    }

    private void nuevoObserver() {
        modelo.addObserver(send);
    }

    public int readPuerto(String puerto) {
        int auxiliar = -1;
        try {
            auxiliar = Integer.parseInt(puerto);
            if (auxiliar < 1024 || auxiliar > 65000)
                return 0;
        } catch (Exception e) {
            logger.debug("Solo debe ingresar números", e);
            return 0;
        }
        return auxiliar;
    }

    private void paraConectar() {

        String ipPuerto_uno = JOptionPane.showInputDialog("Debe ingresar la IP y el puerto de la siguiente manera: ip:puerto");

        if (ipPuerto_uno == null) {
            return;
        }
        String[] ipPuerto = ipPuerto_uno.split(":");

        if (ipPuerto.length != 2) {
            logger.debug("¡Error! IP y puerto mal ingresados");
            JOptionPane.showMessageDialog(rootPane, "Debe ingresar la IP y el puerto separado por dos puntos seguidos --> ':'");
            return;
        }

        String ip = leerIP(ipPuerto[0]);
        if (ip.equals("ERROR")) {
            JOptionPane.showMessageDialog(rootPane, "La IP ingresada debe tener 4 numeros enteros menores a 255 y separadps por un punto");
            return;
        }

        int puerto = leerPuerto(ipPuerto[1]);
        if (puerto == 0) {
            JOptionPane.showMessageDialog(rootPane, "El puerto ingresado debe ser un entero mayor que 1024");
            return;
        }

        try {
            send = new ThreadEnviar(ip, puerto);
            panel.setSend(send);
        }catch (Exception e){
            logger.debug("¡Error! No se puede conectar a la IP y puerto ingresado");
        }

        try{
        recive = new ThreadRecibir(send.getSocket(), modelo);
        panel.setRecive(recive);
        }catch (Exception e){
            logger.debug("¡Error!");
            return;
        }

        Thread recibir = new Thread(recive);
        recibir.start();
        nuevoObserver();
    }

    private int leerPuerto(String puerto) {
        int auxiliar = -1;
        try {
            auxiliar = Integer.parseInt(puerto);

            if (auxiliar < 1024 || auxiliar > 65000)
                return 0;
        } catch (Exception e) {
            logger.debug("Solo puede ingresar valores numéricos");
            return 0;
        }
        return auxiliar;
    }

    private String leerIP(String ip) {

        String[] separados = ip.split("\\.");
        StringBuilder salida = new StringBuilder();

        if (separados.length != 4) {
            logger.debug("IP mal ingresada");
            return "ERROR";
        }

        for (String uno : separados) {
            int auxiliar = -1;
            try {
                auxiliar = Integer.parseInt(uno);

                if (auxiliar < 0 || auxiliar > 255) {
                    logger.debug("¡Error! IP no válida");
                    return "ERROR";
                }
            } catch (Exception e) {
                return "ERROR";
            }
            salida.append("." + auxiliar);
        }
        return salida.substring(1);

    }


}
