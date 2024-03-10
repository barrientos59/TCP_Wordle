import java.io.*;
import java.net.*;
import java.util.*;

public class ServidorWordle {

    private static final int PUERTO = 8000;
    private static final List<String> PALABRAS = Arrays.asList("casa", "mesa", "silla", "gato", "perro", "flor", "sol", "luna", "mar", "cielo");

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PUERTO);
        while (true) {
            Socket clienteSocket = serverSocket.accept();
            new Thread(new Controller(clienteSocket)).start();
        }
    }

    private static class Controller implements Runnable {

        private final Socket clienteSocket;

        public Controller(Socket clienteSocket) {
            this.clienteSocket = clienteSocket;
        }

        @Override
        public void run() {
            try (
                    PrintWriter out = new PrintWriter(clienteSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            ) {
                // Generar una palabra
                String palabraSecreta = PALABRAS.get((int) (Math.random() * PALABRAS.size()));
                System.out.println(palabraSecreta);

                // Enviar la longitud de la palabra al cliente
                out.println(palabraSecreta.length());

                // Bucle de juego
                while (true) {
                    // Recibir una palabra del cliente
                    String palabraRecibida = in.readLine();

                    // Comprobar si la palabra es correcta
                    if (palabraRecibida.equals(palabraSecreta)) {
                        out.println("Has Ganado!");
                        // Generar una nueva palabra secreta
                        break;
                    } else {
                        // Enviar pistas al cliente
                        String pistas = "";
                        for (int i = 0; i < palabraSecreta.length(); i++) {
                            if (palabraRecibida.charAt(i) == palabraSecreta.charAt(i)) {
                                pistas += "✓";
                            } else {
                                pistas += "?";
                            }
                        }
                        out.println(pistas);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clienteSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
