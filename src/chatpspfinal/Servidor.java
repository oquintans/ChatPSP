package chatpspfinal;

import chatpspfinal.HiloServidor;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

public class Servidor {

    SSLSocket newSocket;
    SSLServerSocketFactory serverSocketFactory;
    ServerSocket serverSocket;
    private final int puerto = 5556;
    //Creamos una lista de sockets, donde guardaremos los sockets que se vayan conectando
    private LinkedList<Socket> usuarios = new LinkedList<Socket>();

    public void escuchar() {
        try {
            System.setProperty("javax.net.ssl.keyStore", "src/chatpspfinal/serverKey.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "servpass");
            System.setProperty("javax.net.ssl.trustStore", "src/chatpspfinal/serverTrustedCerts.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "servpass");

            System.out.println("Obteniendo sockets servidor");
            serverSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            System.out.println("Creando socket servidor");
            serverSocket = serverSocketFactory.createServerSocket();
            System.out.println("Realizando el bind");
            InetSocketAddress addr = new InetSocketAddress("localhost", puerto);
            serverSocket.bind(addr);
            
            //Ciclo infinito para estar escuchando por nuevos clientes           
            while (true) {
                //Cuando un cliente se conecte guardamos el socket en nuestra lista
                SSLSocket cliente = (SSLSocket) serverSocket.accept();
                usuarios.add(cliente);
                //Instanciamos un hilo que estara atendiendo al cliente y lo ponemos a escuchar
                Runnable run = new HiloServidor(cliente, usuarios);
                Thread hilo = new Thread(run);
                hilo.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
