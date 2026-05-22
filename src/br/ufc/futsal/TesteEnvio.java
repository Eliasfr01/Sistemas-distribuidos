package br.ufc.futsal;

import br.ufc.futsal.model.Atleta;
import br.ufc.futsal.service.AtletaInputStream;
import br.ufc.futsal.service.AtletaOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class TesteEnvio {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("=== TESTE DE ENVIO ===");
            System.out.println("Informe os atletas manualmente para testar console, arquivo e RMI.");

            int quantidade = lerInteiro(sc, "Quantos atletas deseja cadastrar? ");
            if (quantidade < 0) {
                quantidade = 0;
            }

            Atleta[] lista = new Atleta[quantidade];
            for (int i = 0; i < quantidade; i++) {
                System.out.println("Atleta " + (i + 1) + ":");
                String nome = lerTextoObrigatorio(sc, "Nome: ");
                int numero = lerInteiro(sc, "Número da camisa: ");
                String posicao = lerTextoObrigatorio(sc, "Posição: ");
                lista[i] = new Atleta(nome, numero, posicao);
            }

            // --- TESTE i: Saída Padrão (Console) ---
            System.out.println("=== TESTE 1: CONSOLE ===");
            AtletaOutputStream aosConsole = new AtletaOutputStream(lista, quantidade, System.out);
            aosConsole.enviarDados();
            System.out.println("\n");

            // --- TESTE ii: Arquivo (Físico no PC) ---
            System.out.println("=== TESTE 2: ARQUIVO ===");
            try (FileOutputStream arquivo = new FileOutputStream("atletas.txt")) {
                AtletaOutputStream aosArquivo = new AtletaOutputStream(lista, quantidade, arquivo);
                aosArquivo.enviarDados();
            }
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
            return;
        }

        System.out.println("\n=== TESTE 4: LEITURA DE ARQUIVO ===");

        // Abrimos o arquivo para leitura (FileInputStream)
        try (java.io.FileInputStream fis = new java.io.FileInputStream("atletas.txt")) {

            // Criamos o nosso "leitor" personalizado
            AtletaInputStream ais = new AtletaInputStream(fis);

            System.out.println("Lendo dados do arquivo e reconstruindo objetos...");
            int quantidadeLida = quantidadeDoArquivo("atletas.txt");

            // Vamos tentar ler a quantidade informada pelo usuário
            for (int i = 0; i < quantidadeLida; i++) {
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

    private static String lerTextoObrigatorio(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String valor = sc.nextLine().trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("O valor não pode ser vazio.");
        }
    }

    private static int lerInteiro(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String valor = sc.nextLine().trim();
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private static int quantidadeDoArquivo(String arquivo) {
        try (java.io.FileInputStream fis = new java.io.FileInputStream(arquivo)) {
            int quantidade = 0;
            while (fis.read() != -1) {
                int tamanho = fis.read();
                if (tamanho == -1) {
                    break;
                }
                fis.skip(tamanho);
                quantidade++;
            }
            return quantidade;
        } catch (IOException e) {
            return 0;
        }
    }
}
