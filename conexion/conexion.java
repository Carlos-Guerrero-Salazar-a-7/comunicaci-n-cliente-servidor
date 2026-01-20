package conexion;
import java.net.*;
import java.io.*;
public class conexion {
    protected final int puerto = 1234;
    protected final String direccion="localhost"; //dirección ip
    protected String mensajeServidor; //Mensajes entrantes (recibidos) en el servidor
    protected ServerSocket servidorsocket; //Socket del servidor
    protected Socket conexioncliente; //Socket del cliente
    protected DataOutputStream salidaServidor, salidaCliente; //Flujo de datos de salida

    public conexion(String tipo) throws IOException //Constructor
    {
        if(tipo.equalsIgnoreCase("servidor"))//si es servidor el hijo inicializa socket y conexión si no el cliente tiene la libertad de iniciarlo
        {

            servidorsocket = new ServerSocket(puerto);//servidor socket en puerto
            conexioncliente = new Socket(); //Socket para el cliente
        }
    }
}