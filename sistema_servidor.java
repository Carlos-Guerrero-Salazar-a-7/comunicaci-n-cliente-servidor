import java.io.IOException;
import Servidor.Servidor;
public class sistema_servidor {
    public static void main(String[] args) throws IOException {
        Servidor sistema= new Servidor();
        System.out.println("iniciando el servidor");
        sistema.startServer();
    }
}
