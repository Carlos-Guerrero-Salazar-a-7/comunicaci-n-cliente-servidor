import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import cliente.Cliente;
import conexion.conexion;
public class sistema_cliente {
    public static void main(String[] args) throws IOException {
        Cliente usuario = new Cliente();
        Scanner scan= new Scanner(System.in);
        System.out.println("inicializando al cliente...");
        int opcion = 0;
        boolean conectado=false;
        System.out.println("Bienvenido al sistema, por favor selecciona una acción \n");
        while (opcion != 7) {
            System.out.println("1/Abrir carpeta");
            System.out.println("2/Cerrar cerrar carpeta");
            System.out.println("3/Leer archivo");
            System.out.println("4/Escribir archivo");
            System.out.println("5/Conectar al servidor");
            System.out.println("6/Desconectar al servidor");
            opcion = scan.nextInt();
            if(opcion<5) {
                if (conectado) {
                    switch (opcion) {
                        case 1:
                            usuario.sendmessage("solicito abrir la carpeta");
                            break;
                        case 2:
                            usuario.sendmessage("escogio opcion 2");
                            break;
                        case 3:
                            usuario.sendmessage("escogio opcion 3");
                            break;
                        case 4:
                            usuario.sendmessage("escogio opcion 4");
                            break;
                    }
                } else {
                    System.out.println("funcion aun no disponible: aun no estas conectado");
                }
            }
            if(opcion==5) {
                if (!conectado) {
                    System.out.println("conectando....");
                    try {
                        usuario.startClient();
                        conectado=true;
                        usuario.sendmessage("usuario entro al sistema");
                    } catch (IOException e) {
                        System.out.println("no haz activado el sistema");
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("el sistema aun no es conecta al servidor");
                }
            }
            if(opcion==6){
                if (conectado) {
                    System.out.println("desconectando....");
                    try {
                        usuario.sendmessage("el usuario se ha desconectado, cerrando programa");
                        usuario.disconnect_client();
                        conectado=false;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Aun no estas conectado al sistema, favor de conectarte antes");
                }
            }
        }
        usuario.startClient();
    }
}
