package Servidor;
import java.io.*;
import conexion.conexion;
import java.nio.file.*;

public class Servidor extends conexion {
    private static final String CARPETA_COMPARTIDA = "src/archivos/";

    public Servidor() throws IOException {
        super("servidor");
    }

    public void startServer() {
        try {
            while (true) {
                try {
                    System.out.println("Esperando...");

                    conexioncliente = servidorsocket.accept();
                    System.out.println("Cliente conectado");
                    // Flujo de entrada
                    DataInputStream dis = new DataInputStream(conexioncliente.getInputStream());
                    // Flujo de salida
                    DataOutputStream dos = new DataOutputStream(conexioncliente.getOutputStream());

                    String mensajes = null;

                    // Mientras reciba datos del socket del cliente
                    while (servidorsocket.isClosed() == false) {
                        mensajes = dis.readUTF();
                        System.out.println("Orden recibida: " + mensajes);

                        if (mensajes.equalsIgnoreCase("salir")) {
                            break;
                        }

                        // Procesar comandos
                        if (mensajes.startsWith("LISTAR_ARCHIVOS")) {
                            String listaArchivos = listarArchivos();
                            dos.writeUTF(listaArchivos);
                            dos.flush();
                        }
                        else if (mensajes.startsWith("ESCRIBIR:")) {
                            // Formato: ESCRIBIR:nombreArchivo:texto
                            String[] partes = mensajes.split(":", 3);
                            if (partes.length == 3) {
                                String nombreArchivo = partes[1];
                                String texto = partes[2];
                                boolean exito = escribirEnArchivo(nombreArchivo, texto);

                                if (exito) {
                                    dos.writeUTF("OK:Texto agregado correctamente");
                                } else {
                                    dos.writeUTF("ERROR:No se pudo escribir en el archivo");
                                }
                                dos.flush();
                            }
                        }
                        else if (mensajes.startsWith("LEER:")) {
                            // Formato: LEER:nombreArchivo
                            String[] partes = mensajes.split(":", 2);
                            if (partes.length == 2) {
                                String nombreArchivo = partes[1];
                                String contenido = leerArchivo(nombreArchivo);
                                dos.writeUTF(contenido);
                                dos.flush();
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error en conexión: " + e.getMessage());
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Lista todos los archivos en la carpeta compartida
    private String listarArchivos() {
        File carpeta = new File(CARPETA_COMPARTIDA);
        File[] archivos = carpeta.listFiles();

        if (archivos == null || archivos.length == 0) {
            return "SIN_ARCHIVOS";
        }

        StringBuilder lista = new StringBuilder("ARCHIVOS:");
        for (File archivo : archivos) {
            if (archivo.isFile()) {
                lista.append(archivo.getName()).append(",");
            }
        }

        return lista.toString();
    }

    // Escribe texto en un archivo usando el comando echo del sistema
    private boolean escribirEnArchivo(String nombreArchivo, String texto) {
        try {
            String rutaCompleta = CARPETA_COMPARTIDA + nombreArchivo;

            // Construir el comando con ProcessBuilder para mejor manejo
            ProcessBuilder pb = new ProcessBuilder("sh", "-c", "echo '" + texto + "' >> " + rutaCompleta);
            pb.redirectErrorStream(true);
            Process proceso = pb.start();
            proceso.waitFor();

        } catch (Exception e) {
            System.out.println("✗ Error al escribir en archivo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Lee el contenido de un archivo
    private String leerArchivo(String nombreArchivo) {
        try {
            String rutaCompleta = CARPETA_COMPARTIDA + nombreArchivo;
            File archivo = new File(rutaCompleta);

            if (!archivo.exists()) {
                return "ERROR:El archivo no existe";
            }

            BufferedReader reader = new BufferedReader(new FileReader(archivo));
            StringBuilder contenido = new StringBuilder("CONTENIDO:");
            String linea;

            while ((linea = reader.readLine()) != null) {
                contenido.append(linea).append("\n");
            }

            reader.close();
            return contenido.toString();

        } catch (Exception e) {
            return "ERROR:No se pudo leer el archivo";
        }
    }
}