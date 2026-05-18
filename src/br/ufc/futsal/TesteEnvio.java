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

            // --- TESTE iii: Envio via RMI ---
            System.out.println("\n=== TESTE 3: RMI (COMUNICAÇÃO REMOTA) ===");
            try {
                java.rmi.registry.Registry reg = java.rmi.registry.LocateRegistry.getRegistry("localhost", 1099);
                br.ufc.futsal.rmi.FutsalServiceRemote serv = (br.ufc.futsal.rmi.FutsalServiceRemote) reg.lookup("FutsalService");
                for (Atleta a : lista) {
                    String res = serv.registerAtleta(a);
                    System.out.println("Servidor RMI: " + res);
                }
            } catch (Exception e) {
                System.err.println("Erro no teste RMI: " + e.getMessage());
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
