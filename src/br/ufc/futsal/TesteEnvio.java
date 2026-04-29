package br.ufc.futsal;

import br.ufc.futsal.model.Atleta;
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
            System.out.println("\n=== TESTE 3: TCP (SIMULADO) ===");

            // Este try especial abre o "servidor" e o "cliente" e os fecha automaticamente no final
            try (java.net.ServerSocket servidorFake = new java.net.ServerSocket(5000);
                 java.net.Socket socketCliente = new java.net.Socket("localhost", 5000)) {

                // O servidor aceita a conexão do cliente
                java.net.Socket conexaoNoServidor = servidorFake.accept();

                // Usamos seu AtletaOutputStream para enviar pelo cano da rede
                AtletaOutputStream aosRede = new AtletaOutputStream(lista, 2, socketCliente.getOutputStream());
                aosRede.enviarDados();

                System.out.println("Dados enviados via TCP com sucesso para a porta 5000!");
            }

        } catch (IOException e) {
            System.err.println("Ops, deu erro na manipulação dos dados: " + e.getMessage());
        }
        }
}
