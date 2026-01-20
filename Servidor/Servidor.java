package Servidor;
import java.io.*;
import conexion.conexion;

public class Servidor extends conexion //Se hereda de conexión para hacer uso de los sockets y demás
{
    public Servidor() throws IOException{super("servidor");} //Se usa el constructor para servidor de Conexion

    public void startServer()//Método para iniciar el servidor
    {
        try
        {
            System.out.println("Esperando..."); //Esperando conexión

            conexioncliente = servidorsocket.accept(); //Accept comienza el socket y espera una conexión desde un cliente
            if(conexioncliente.isConnected()){
                System.out.println("Cliente en línea");
            }

            //Se obtiene el flujo entrante desde el cliente
            BufferedReader entrada = new BufferedReader(new InputStreamReader(conexioncliente.getInputStream()));

            //flujo de entrada
            DataInputStream dis = new DataInputStream(conexioncliente.getInputStream());
            //flujo de salida
            PrintWriter out = new PrintWriter(conexioncliente.getOutputStream(), true);
            String mensajes=null;
            //mientras reciba datos del socket del cliente
            while (servidorsocket.isClosed()==false){
                mensajes=dis.readUTF();
                System.out.println("Orden recibida: "+mensajes);
                if (mensajes.equalsIgnoreCase("salir")) break;
                //aqui si el mensaje contiene o es el mensaje podemos ejecutar funciones extras
            }

            System.out.println("Fin de la conexión");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}