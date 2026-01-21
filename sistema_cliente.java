import java.io.IOException;
import java.util.Scanner;
import cliente.Cliente;

public class sistema_cliente {
    public static void main(String[] args) throws IOException {
        Cliente usuario = new Cliente();
        Scanner scan = new Scanner(System.in);
        System.out.println("Inicializando al cliente...");
        int opcion = 0;
        boolean conectado = false;
        System.out.println("Bienvenido al sistema, por favor selecciona una acción \n");

        while (opcion != 4) {
            System.out.println("1. Abrir carpeta (Agregar texto a archivos)");
            System.out.println("2. Conectar al servidor");
            System.out.println("3. Desconectar del servidor");
            System.out.println("4. Salir");
            System.out.print("Opción: ");
            opcion = scan.nextInt();
            scan.nextLine(); // Limpiar buffer

            if (opcion == 1) {
                if (!conectado) {
                        System.out.println("Función aún no disponible: aún no estás conectado");
                        continue;
                }
                abrirCarpeta(usuario, scan);
            }

            if (opcion == 2) {
                if (!conectado) {
                    System.out.println("Conectando....");
                    try {
                        usuario.startClient();
                        conectado = true;
                        usuario.sendmessage("usuario entro al sistema");
                        System.out.println("Conectado al servidor exitosamente");
                    } catch (IOException e) {
                        System.out.println("No se pudo conectar al servidor. Asegúrate de que esté activo.");
                        conectado = false;
                    }
                } else {
                    System.out.println("Ya estás conectado al servidor");
                }
            }

            if (opcion == 3) {
                if (conectado) {
                    System.out.println("Desconectando....");
                    try {
                        usuario.sendmessage("salir");
                        usuario.disconnect_client();
                        conectado = false;
                        System.out.println("✓ Desconectado del servidor");
                    } catch (Exception e) {
                        System.out.println("Error al desconectar: " + e.getMessage());
                    }
                } else {
                    System.out.println("Aún no estás conectado al sistema");
                }
            }
            if(opcion == 4){
                System.out.println("bye,bye");
                break;
            }
        }

        if (conectado) {
            usuario.disconnect_client();
        }
        scan.close();
    }

    // Función para abrir carpeta y agregar texto a archivos
    private static void abrirCarpeta(Cliente usuario, Scanner scan) {
        try {
            // Solicitar lista de archivos
            int subOpcion=0;
            while(subOpcion!=3) {//hasta que se quiera salir
            String respuesta = usuario.solicitarListaArchivos();
            if (respuesta.equals("SIN_ARCHIVOS")) {
                System.out.println("La carpeta está vacía. Aún no hay archivos.");
            } else if (respuesta.startsWith("ARCHIVOS:")) {
                String[] archivos = respuesta.substring(9).split(",");
                System.out.println("Archivos disponibles:");
                for (int i = 0; i < archivos.length; i++) {
                    if (!archivos[i].trim().isEmpty()) {
                        System.out.println("  - " + archivos[i]);
                    }
                }
            }
            System.out.println("\n¿Qué deseas hacer?");
            System.out.println("1. Agregar texto a un archivo");
            System.out.println("2. ver texto de un archivo");
            System.out.println("3. Volver al menú principal");
            System.out.print("Opción: ");
            subOpcion = scan.nextInt();
            scan.nextLine(); // Limpiar buffer
                if (subOpcion == 1) {
                    System.out.print("\nNombre del archivo (ej: notas.txt): ");
                    String nombreArchivo = scan.nextLine().trim();

                    System.out.print("Texto a agregar: ");
                    String texto = scan.nextLine();

                    String resultado = usuario.escribirEnArchivo(nombreArchivo, texto);

                    if (resultado.startsWith("OK:")) {
                        System.out.println("✓ " + resultado.substring(3));
                    } else if (resultado.startsWith("ERROR:")) {
                        System.out.println("✗ " + resultado.substring(6));
                    }
                }
                if (subOpcion == 2) {
                    leerArchivo(usuario, scan);
                }
            }

        } catch (IOException e) {
            System.out.println("Error de comunicación con el servidor: " + e.getMessage());
        }
    }

    // Función para leer el contenido de un archivo
    private static void leerArchivo(Cliente usuario, Scanner scan) {
        try {
            // Mostrar archivos disponibles
            String respuesta = usuario.solicitarListaArchivos();

            System.out.print("\nNombre del archivo a leer: ");
            String nombreArchivo = scan.nextLine().trim();

            String contenido = usuario.leerArchivo(nombreArchivo);

            if (contenido.startsWith("CONTENIDO:")) {
                System.out.println("\n--- Contenido de " + nombreArchivo + " ---");
                System.out.println(contenido.substring(10));
                System.out.println("--- Fin del archivo ---");
            } else if (contenido.startsWith("ERROR:")) {
                System.out.println("✗ " + contenido.substring(6));
            }

        } catch (IOException e) {
            System.out.println("Error de comunicación con el servidor: " + e.getMessage());
        }
    }
}