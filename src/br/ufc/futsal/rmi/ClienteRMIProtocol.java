package br.ufc.futsal.rmi;

import br.ufc.futsal.model.Mensagem;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ClienteRMIProtocol {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            Registry reg = LocateRegistry.getRegistry("localhost", 1099);
            ProtocolService serv = (ProtocolService) reg.lookup("FutsalService");

            System.out.println("=== MENU RMI PROTOCOL ===");
            System.out.println("Este cliente envia mensagens estruturadas (`Mensagem`) para o servidor.");

            while (true) {
                System.out.println("\n--- MENU RMI PROTOCOL ---");
                System.out.println("1) Registrar Atleta");
                System.out.println("2) Listar Atletas");
                System.out.println("3) Registrar Time");
                System.out.println("4) Calcular Resultado");
                System.out.println("5) Consultar Arbitro");
                System.out.println("6) Enviar Mensagem bruta (JSON)");
                System.out.println("0) Sair");
                System.out.print("Escolha: ");
                String opt = sc.nextLine().trim();

                if (opt.equals("0")) break;

                switch (opt) {
                    case "1": {
                        System.out.print("Nome: "); String nome = sc.nextLine();
                        System.out.print("Número camisa: "); String num = sc.nextLine();
                        System.out.print("Posição: "); String pos = sc.nextLine();
                        String json = "{\"nome\":\""+escape(nome)+"\",\"numeroCamisa\":"+num+",\"posicao\":\""+escape(pos)+"\"}";
                        Mensagem req = new Mensagem(0, nextId(), "FutsalService", 1, json.getBytes("UTF-8"));
                        Mensagem rep = serv.doOperation(req);
                        System.out.println("Reply: " + new String(rep.getArguments(), "UTF-8"));
                        break;
                    }
                    case "2": {
                        Mensagem req = new Mensagem(0, nextId(), "FutsalService", 2, null);
                        Mensagem rep = serv.doOperation(req);
                        System.out.println("Atletas: " + new String(rep.getArguments(), "UTF-8"));
                        break;
                    }
                    case "3": {
                        System.out.print("Nome do time: "); String nomeTime = sc.nextLine();
                        System.out.print("Cidade: "); String cidade = sc.nextLine();
                        String json = "{\"nome\":\""+escape(nomeTime)+"\",\"cidade\":\""+escape(cidade)+"\"}";
                        Mensagem req = new Mensagem(0, nextId(), "FutsalService", 3, json.getBytes("UTF-8"));
                        Mensagem rep = serv.doOperation(req);
                        System.out.println("Reply: " + new String(rep.getArguments(), "UTF-8"));
                        break;
                    }
                    case "4": {
                        System.out.print("Nome time A: "); String nA = sc.nextLine();
                        System.out.print("Cidade time A: "); String cA = sc.nextLine();
                        System.out.print("Nome time B: "); String nB = sc.nextLine();
                        System.out.print("Cidade time B: "); String cB = sc.nextLine();
                        System.out.print("Gols time A: "); String gA = sc.nextLine();
                        System.out.print("Gols time B: "); String gB = sc.nextLine();
                        String json = "{\"timeA\":{\"nome\":\""+escape(nA)+"\",\"cidade\":\""+escape(cA)+"\"},\"timeB\":{\"nome\":\""+escape(nB)+"\",\"cidade\":\""+escape(cB)+"\"},\"golsA\":"+gA+",\"golsB\":"+gB+"}";
                        Mensagem req = new Mensagem(0, nextId(), "FutsalService", 4, json.getBytes("UTF-8"));
                        Mensagem rep = serv.doOperation(req);
                        System.out.println("Resultado JSON: " + new String(rep.getArguments(), "UTF-8"));
                        break;
                    }
                    case "5": {
                        System.out.print("Nome do arbitro: "); String nome = sc.nextLine();
                        System.out.print("Categoria do arbitro: "); String categoria = sc.nextLine();
                        String json = "{\"nome\":\""+escape(nome)+"\",\"categoria\":\""+escape(categoria)+"\"}";
                        Mensagem req = new Mensagem(0, nextId(), "FutsalService", 5, json.getBytes("UTF-8"));
                        Mensagem rep = serv.doOperation(req);
                        System.out.println("Arbitro: " + new String(rep.getArguments(), "UTF-8"));
                        break;
                    }
                    case "6": {
                        System.out.println("Cole o JSON de arguments (ex: {\"foo\":1}):");
                        String json = sc.nextLine();
                        System.out.print("objectReference: ");
                        String objRef = sc.nextLine();
                        System.out.print("methodId (int): ");
                        int mid = Integer.parseInt(sc.nextLine());
                        Mensagem req = new Mensagem(0, nextId(), objRef, mid, json.getBytes("UTF-8"));
                        Mensagem rep = serv.doOperation(req);
                        System.out.println("Reply: " + new String(rep.getArguments(), "UTF-8"));
                        break;
                    }
                    default:
                        System.out.println("Opção inválida.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int counter = 100;
    private static synchronized int nextId() { return counter++; }
    private static String escape(String s) { return s == null ? "" : s.replace("\"","\\\""); }
}
