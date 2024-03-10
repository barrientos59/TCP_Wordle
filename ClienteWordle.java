import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteWordle {

    private static final String HOST = "localhost";
    private static final int PUERTO = 8000;

    public static void main(String[] args) throws IOException {
        Socket clienteSocket = new Socket(HOST, PUERTO);
        try (
                PrintWriter out = new PrintWriter(clienteSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
        ) {
            // Recibir la longitud de la palabra
            int longitudPalabra = Integer.parseInt(in.readLine());

            // Bucle de juego
            while (true) {
                // Pedir una palabra al usuario
                Scanner scanner = new Scanner(System.in);
                System.out.print("Introduce una palabra de " + longitudPalabra + " letras: ");
                String palabraUsuario = scanner.nextLine();

                // Enviar la palabra al servidor
                out.println(palabraUsuario);

                // Recibir pistas del servidor
                String pistas = in.readLine();

                // Mostrar las pistas al usuario
                System.out.println("Pistas: " + pistas);

                // Si la palabra es correcta, salir del bucle
                if ("✓".repeat(longitudPalabra).equals(pistas)) {
                    break;
                }
            }
        } finally {
            try {
                clienteSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
