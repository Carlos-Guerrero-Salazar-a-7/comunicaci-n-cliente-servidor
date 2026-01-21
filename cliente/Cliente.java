package cliente;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import conexion.conexion;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
public class Cliente extends conexion {
    private DataInputStream entradaServidor;
    public Cliente() throws IOException{super("cliente");} //Se usa el constructor para cliente de Conexion

    public void startClient() //Método para iniciar el cliente
    {
        try
        {
            //Flujo de datos hacia el servidor
            conexioncliente = new Socket(direccion, puerto);//el cliente es comenzado mediante comando y con ello los mensages extras
            salidaServidor = new DataOutputStream(conexioncliente.getOutputStream());//escritura a servidor por comentarios
            entradaServidor = new DataInputStream(conexioncliente.getInputStream());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    //desconecta, debo poner una forma de que siga el sistema
    public void disconnect_client() throws IOException {
        try{
            conexioncliente.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    //manda mensages al sistema, ya esta en bucle para que los obtenga
    public void sendmessage(String informacion) throws IOException{
        try{
            salidaServidor.writeUTF(informacion);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String solicitarListaArchivos() throws IOException {
        salidaServidor.writeUTF("LISTAR_ARCHIVOS");
        salidaServidor.flush();
        return entradaServidor.readUTF();
    }

    // Envía comando para escribir en un archivo
    public String escribirEnArchivo(String nombreArchivo, String texto) throws IOException {
        String comando = "ESCRIBIR:" + nombreArchivo + ":" + texto;
        salidaServidor.writeUTF(comando);
        salidaServidor.flush();
        return entradaServidor.readUTF();
    }

    // Solicita leer un archivo
    public String leerArchivo(String nombreArchivo) throws IOException {
        String comando = "LEER:" + nombreArchivo;
        salidaServidor.writeUTF(comando);
        salidaServidor.flush();
        return entradaServidor.readUTF();
    }
}