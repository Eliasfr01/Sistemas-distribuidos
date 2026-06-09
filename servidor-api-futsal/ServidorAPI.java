import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ServidorAPI {
    private static List<Atleta> listaAtletas = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        // Massa de teste cumprindo os requisitos de OO
        listaAtletas.add(new Atleta("Falcao", 12, "Ala"));
        listaAtletas.add(new Atleta("Rodrigo", 4, "Fixo"));

        // Cria o servidor HTTP na porta 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Endpoint /atletas (Objeto Distribuído exposto)
        server.createContext("/atletas", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*"); // Evita erro de CORS

                // Monta o JSON manualmente para ser independente de libs externas
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < listaAtletas.size(); i++) {
                    Atleta a = listaAtletas.get(i);
                    json.append(String.format("{\"nome\":\"%s\",\"numero\":%d,\"posicao\":\"%s\"}", a.nome, a.numero, a.posicao));
                    if (i < listaAtletas.size() - 1) json.append(",");
                }
                json.append("]");

                byte[] responseBytes = json.toString().getBytes();
                exchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();
            }
        });

        System.out.println("Servidor API Futsal Rodando na porta 8080...");
        server.start();
    }
}