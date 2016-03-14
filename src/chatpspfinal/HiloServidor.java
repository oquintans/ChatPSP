package chatpspfinal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import javax.net.ssl.SSLSocket;
import javax.swing.JOptionPane;

/**
 *
 * @author Oscar
 */
public class HiloServidor implements Runnable {

    //Declaramos las variables que utiliza el hilo para estar recibiendo y mandando mensajes
    SSLSocket socket;
    DataInputStream in;
    DataOutputStream out;

    private LinkedList<Socket> usuarios = new LinkedList<Socket>();

    //Constructor que recibe el socket que atendera el hilo y la lista de usuarios conectados
    public HiloServidor(Socket soc, LinkedList users) {
        socket = (SSLSocket) soc;
        usuarios = users;
    }

    @Override
    public void run() {
        try {
            //Inicializamos los canales de comunicacion y mandamos un mensaje de bienvenida
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(" ");
            //Ciclo infinito para escuchar por mensajes del cliente
            while (true) {
                String recibido = in.readUTF();
                
                //Cuando se recibe un mensaje se envia a todos los usuarios conectados 
                for (int i = 0; i < usuarios.size(); i++) {
                    out = new DataOutputStream(usuarios.get(i).getOutputStream());
                    out.writeUTF(recibido);
                }
            }
        } catch (IOException e) {

        }
    }
}
