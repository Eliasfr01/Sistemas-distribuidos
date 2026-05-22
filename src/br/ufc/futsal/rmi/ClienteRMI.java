package br.ufc.futsal.rmi;

import br.ufc.futsal.model.Arbitro;
import br.ufc.futsal.model.Atleta;
import br.ufc.futsal.model.Resultados;
import br.ufc.futsal.model.Time;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClienteRMI {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            FutsalServiceRemote serv = (FutsalServiceRemote) reg.lookup("FutsalService");

            System.out.println("=== CLIENTE RMI DO FUTSAL ===");
            System.out.println("Todos os dados são informados pelo usuário, sem valores simulados.");

            while (true) {
                System.out.println("\n--- MENU PRINCIPAL ---");
                System.out.println("1) Cadastrar atleta");
                System.out.println("2) Listar atletas");
                System.out.println("3) Cadastrar time");
                System.out.println("4) Listar times");
                System.out.println("5) Registrar resultado de partida");
                System.out.println("6) Consultar árbitro");
                System.out.println("7) Definir candidatos ao craque do jogo");
                System.out.println("0) Sair");
                System.out.print("Escolha: ");

                String opcao = sc.nextLine().trim();
                if (opcao.equals("0")) {
                    break;
                }

                switch (opcao) {
                    case "1": {
                        String nome = lerTextoObrigatorio(sc, "Nome do atleta: ");
                        int numero = lerInteiro(sc, "Número da camisa: ");
                        String posicao = lerTextoObrigatorio(sc, "Posição: ");
                        String resposta = serv.registerAtleta(new Atleta(nome, numero, posicao));
                        System.out.println(resposta);
                        break;
                    }
                    case "2": {
                        Atleta[] lista = serv.listAtletas();
                        if (lista.length == 0) {
                            System.out.println("Nenhum atleta cadastrado ainda.");
                        } else {
                            System.out.println("Atletas cadastrados:");
                            for (Atleta atleta : lista) {
                                System.out.println("- " + atleta.getNome() + " | camisa " + atleta.getNumeroCamisa() + " | posição " + atleta.getPosicao());
                            }
                        }
                        break;
                    }
                    case "3": {
                        String nomeTime = lerTextoObrigatorio(sc, "Nome do time: ");
                        String cidade = lerTextoObrigatorio(sc, "Cidade do time: ");
                        Time time = new Time(nomeTime, cidade);
                        RemoteTimeImpl remoto = new RemoteTimeImpl(time);
                        System.out.println(serv.registerTime(remoto));
                        break;
                    }
                    case "4": {
                        Time[] times = serv.listTimes();
                        if (times.length == 0) {
                            System.out.println("Nenhum time cadastrado ainda.");
                        } else {
                            System.out.println("Times cadastrados:");
                            for (Time time : times) {
                                System.out.println("- " + time.getNome() + " | " + time.getCidade());
                            }
                        }
                        break;
                    }
                    case "5": {
                        System.out.println("Time A:");
                        Time timeA = new Time(
                                lerTextoObrigatorio(sc, "Nome: "),
                                lerTextoObrigatorio(sc, "Cidade: "));
                        System.out.println("Time B (adversário):");
                        Time timeB = new Time(
                                lerTextoObrigatorio(sc, "Nome: "),
                                lerTextoObrigatorio(sc, "Cidade: "));
                        int golsA = lerInteiro(sc, "Gols do time A: ");
                        int golsB = lerInteiro(sc, "Gols do time B: ");
                        Resultados resultado = serv.computeResult(timeA, timeB, golsA, golsB);
                        resultado.exibirResultados();
                        break;
                    }
                    case "6": {
                        String nomeArbitro = lerTextoObrigatorio(sc, "Nome do árbitro: ");
                        String categoria = lerTextoObrigatorio(sc, "Categoria do árbitro: ");
                        Arbitro arbitro = serv.getArbitroInfo(nomeArbitro, categoria);
                        System.out.println("Árbitro consultado: " + arbitro.getNome() + " | categoria " + arbitro.getCategoria());
                        break;
                    }
                    case "7": {
                        Atleta[] atletas = serv.listAtletas();
                        if (atletas.length == 0) {
                            System.out.println("Cadastre atletas antes de definir os candidatos ao craque.");
                            break;
                        }

                        System.out.println("Selecione os jogadores que disputarão o craque do jogo:");
                        for (int i = 0; i < atletas.length; i++) {
                            Atleta atleta = atletas[i];
                            System.out.println((i + 1) + ") " + atleta.getNome() + " | camisa " + atleta.getNumeroCamisa() + " | posição " + atleta.getPosicao());
                        }

                        System.out.print("Digite os números separados por vírgula: ");
                        String entrada = sc.nextLine().trim();
                        String[] partes = entrada.split(",");
                        List<String> selecionados = new ArrayList<>();

                        for (String parte : partes) {
                            String valor = parte.trim();
                            if (valor.isEmpty()) {
                                continue;
                            }
                            try {
                                int indice = Integer.parseInt(valor) - 1;
                                if (indice >= 0 && indice < atletas.length) {
                                    String nome = atletas[indice].getNome();
                                    if (!selecionados.contains(nome)) {
                                        selecionados.add(nome);
                                    }
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Ignorando valor inválido: " + valor);
                            }
                        }

                        if (selecionados.isEmpty()) {
                            System.out.println("Nenhum candidato válido foi selecionado.");
                            break;
                        }

                        serv.setCraqueCandidates(selecionados.toArray(new String[0]));
                        System.out.println("Candidatos ao craque definidos com sucesso.");
                        for (String nome : selecionados) {
                            System.out.println("- " + nome);
                        }
                        break;
                    }
                    default:
                        System.out.println("Opção inválida.");
                }
            }

            System.out.println("Cliente encerrado.");
        } catch (Exception e) {
            e.printStackTrace();
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
}
