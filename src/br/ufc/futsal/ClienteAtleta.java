package br.ufc.futsal;

import br.ufc.futsal.model.Atleta;
import java.util.Scanner;

public class ClienteAtleta {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.println("=== CLIENTE DE CADASTRO DE ATLETA ===");
            System.out.println("(Para uma demo completa, execute br.ufc.futsal.rmi.ClienteRMI)");
            System.out.println();

            System.out.print("Nome do atleta: "); String nome = sc.nextLine();
            System.out.print("Número da camisa: "); int num = Integer.parseInt(sc.nextLine());
            System.out.print("Posição: "); String pos = sc.nextLine();

            java.rmi.registry.Registry reg = java.rmi.registry.LocateRegistry.getRegistry("localhost", 1099);
            br.ufc.futsal.rmi.FutsalServiceRemote serv = (br.ufc.futsal.rmi.FutsalServiceRemote) reg.lookup("FutsalService");

            Atleta atleta = new Atleta(nome, num, pos);
            String res = serv.registerAtleta(atleta);
            System.out.println("\n[OK] " + res);
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            System.err.println("Certifique-se de que o servidor RMI está rodando (br.ufc.futsal.rmi.ServidorRMI)");
            e.printStackTrace();
        }
    }
}