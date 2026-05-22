package br.ufc.futsal;

import br.ufc.futsal.service.AdministradorService;
import java.util.Scanner;

public class AdministradorConsole {
    public static void main(String[] args) {
        System.out.println("=== CONSOLE DO ADMINISTRADOR ===");
        System.out.println("Envie notificações para todos os clientes conectados.");
        System.out.println("(Digite 'sair' para encerrar)");
        System.out.println();

        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.print("Notificação: ");
            String msg = sc.nextLine();
            if(msg.equalsIgnoreCase("sair")) break;
            if(msg.trim().isEmpty()) continue;
            AdministradorService.enviarAviso(msg);
        }
        System.out.println("Console do administrador encerrado.");
    }
}