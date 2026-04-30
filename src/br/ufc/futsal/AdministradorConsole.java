package br.ufc.futsal;

import br.ufc.futsal.service.AdministradorService;
import java.util.Scanner;

public class AdministradorConsole {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println("Digite um aviso para enviar a TODOS os torcedores (ou 'sair'):");
            String msg = sc.nextLine();
            if(msg.equalsIgnoreCase("sair")) break;
            AdministradorService.enviarAviso(msg);
        }
    }
}