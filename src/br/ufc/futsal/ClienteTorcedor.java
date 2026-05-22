package br.ufc.futsal;

import br.ufc.futsal.model.Atleta;
import br.ufc.futsal.service.TorcedorMulticast;
import java.util.Scanner;

public class ClienteTorcedor {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("=== CLIENTE DO TORCEDOR ===");
            System.out.println("Este cliente permite votação livre e recebimento de avisos.");
            System.out.println();

            // inicia listener RMI em background
            TorcedorMulticast listener = new TorcedorMulticast();
            Thread listenerThread = new Thread(listener);
            listenerThread.setDaemon(true);
            listenerThread.start();

            System.out.println();
            mostrarCandidatos(sc);
            System.out.println("Menu do Torcedor:");
            while (true) {
                System.out.println("1) Votar");
                System.out.println("2) Listar atletas cadastrados");
                System.out.println("3) Listar candidatos ao craque");
                System.out.println("0) Sair");
                System.out.print("Escolha: ");
                String opt = sc.nextLine();
                if (opt.equals("0")) break;
                if (opt.equals("1")) {
                    java.rmi.registry.Registry reg = java.rmi.registry.LocateRegistry.getRegistry("localhost", 1099);
                    br.ufc.futsal.rmi.FutsalServiceRemote serv = (br.ufc.futsal.rmi.FutsalServiceRemote) reg.lookup("FutsalService");
                    String[] candidatos = serv.listCraqueCandidates();
                    if (candidatos.length == 0) {
                        System.out.println("Nenhum candidato ao craque foi definido ainda.");
                        continue;
                    }

                    System.out.println("Candidatos ao craque:");
                    for (int i = 0; i < candidatos.length; i++) {
                        System.out.println((i + 1) + ") " + candidatos[i]);
                    }

                    System.out.print("Digite seu login: ");
                    String login = sc.nextLine().trim();
                    System.out.print("Escolha o número do craque: ");
                    String escolha = sc.nextLine().trim();

                    int indice;
                    try {
                        indice = Integer.parseInt(escolha) - 1;
                    } catch (NumberFormatException e) {
                        System.out.println("Escolha inválida.");
                        continue;
                    }

                    if (login.isEmpty() || indice < 0 || indice >= candidatos.length) {
                        System.out.println("Login vazio ou escolha inválida.");
                        continue;
                    }

                    String craque = candidatos[indice];
                    serv.vote(login, craque);
                    System.out.println("[OK] Voto registrado para: " + craque);
                    continue;
                }
                if (opt.equals("2")) {
                    java.rmi.registry.Registry reg = java.rmi.registry.LocateRegistry.getRegistry("localhost", 1099);
                    br.ufc.futsal.rmi.FutsalServiceRemote serv = (br.ufc.futsal.rmi.FutsalServiceRemote) reg.lookup("FutsalService");
                    Atleta[] atletas = serv.listAtletas();
                    if (atletas.length == 0) {
                        System.out.println("Nenhum atleta cadastrado ainda.");
                    } else {
                        System.out.println("Atletas cadastrados:");
                        for (Atleta atleta : atletas) {
                            System.out.println("- " + atleta.getNome() + " | camisa " + atleta.getNumeroCamisa() + " | posição " + atleta.getPosicao());
                        }
                    }
                }
                if (opt.equals("3")) {
                    mostrarCandidatos(sc);
                }
            }
            System.out.println("Cliente do torcedor encerrado.");
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            System.err.println("Certifique-se de que o servidor RMI está rodando.");
            e.printStackTrace();
        }
    }

    private static void mostrarCandidatos(Scanner sc) {
        try {
            java.rmi.registry.Registry reg = java.rmi.registry.LocateRegistry.getRegistry("localhost", 1099);
            br.ufc.futsal.rmi.FutsalServiceRemote serv = (br.ufc.futsal.rmi.FutsalServiceRemote) reg.lookup("FutsalService");
            String[] candidatos = serv.listCraqueCandidates();
            System.out.println("Candidatos ao craque:");
            if (candidatos.length == 0) {
                System.out.println("Nenhum candidato definido ainda.");
                return;
            }
            for (int i = 0; i < candidatos.length; i++) {
                System.out.println("- " + candidatos[i]);
            }
        } catch (Exception e) {
            System.out.println("Não foi possível carregar os candidatos ao craque.");
        }
    }
}