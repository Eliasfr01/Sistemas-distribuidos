package br.ufc.futsal;

import br.ufc.futsal.model.Atleta;
import br.ufc.futsal.service.AtletaInputStream;
import br.ufc.futsal.service.AtletaOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TesteEnvio {
    public static void main(String[] args) {
        try {
            // 1. Criando os dados (nossos POJOs)
            Atleta[] lista = new Atleta[2];
            lista[0] = new Atleta("Falcao", 12, "Ala");
            lista[1] = new Atleta("Ricardinho", 10, "Fixo");

            // --- TESTE i: Saída Padrão (Console) ---
            System.out.println("=== TESTE 1: CONSOLE ===");
            AtletaOutputStream aosConsole = new AtletaOutputStream(lista, 2, System.out);
            aosConsole.enviarDados();
            System.out.println("\n");

            // --- TESTE ii: Arquivo (Físico no PC) ---
            System.out.println("=== TESTE 2: ARQUIVO ===");
            FileOutputStream arquivo = new FileOutputStream("atletas.txt");
            AtletaOutputStream aosArquivo = new AtletaOutputStream(lista, 2, arquivo);
            aosArquivo.enviarDados();
            arquivo.close();
            System.out.println("Arquivo 'atletas.txt' criado com sucesso!");

            // --- TESTE iii: Servidor Remoto (TCP) ---
            System.out.println("\n=== TESTE 3: TCP (COMUNICAÇÃO REAL) ===");

            try (java.net.ServerSocket servidorFake = new java.net.ServerSocket(5000);
                 java.net.Socket socketCliente = new java.net.Socket("localhost", 5000)) {

                // 1. O servidor aceita a conexão
                java.net.Socket conexaoNoServidor = servidorFake.accept();

                // 2. LADO DO CLIENTE: Envia os dados
                AtletaOutputStream aosRede = new AtletaOutputStream(lista, 2, socketCliente.getOutputStream());
                aosRede.enviarDados();
                System.out.println("Cliente: Enviei os atletas via rede...");

                // 3. LADO DO SERVIDOR: Recebe e reconstrói na hora!
                // Note que usamos 'conexaoNoServidor.getInputStream()'
                AtletaInputStream aisRede = new AtletaInputStream(conexaoNoServidor.getInputStream());

                System.out.println("Servidor: Recebi e estou reconstruindo...");
                for (int i = 0; i < 2; i++) {
                    Atleta recebido = aisRede.lerAtleta();
                    System.out.println("Servidor recuperou: " + recebido.getNome() + " (" + recebido.getPosicao() + ")");
                }

                System.out.println("Comunicação TCP finalizada com sucesso!");
            }

        } catch (IOException e) {
            System.err.println("Ops, deu erro na manipulação dos dados: " + e.getMessage());
        }
        System.out.println("\n=== TESTE 4: LEITURA DE ARQUIVO ===");

        // Abrimos o arquivo para leitura (FileInputStream)
        try (java.io.FileInputStream fis = new java.io.FileInputStream("atletas.txt")) {

            // Criamos o nosso "leitor" personalizado
            AtletaInputStream ais = new AtletaInputStream(fis);

            System.out.println("Lendo dados do arquivo e reconstruindo objetos...");

            // Vamos tentar ler os 2 atletas que sabemos que estão lá
            for (int i = 0; i < 2; i++) {
                Atleta recuperado = ais.lerAtleta();
                if (recuperado != null) {
                    System.out.println("Objeto reconstruído com sucesso!");
                    System.out.println("Nome: " + recuperado.getNome());
                    System.out.println("Número: " + recuperado.getNumeroCamisa());
                    System.out.println("Posição: " + recuperado.getPosicao());
                    System.out.println("----------------------------");
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        }
}
