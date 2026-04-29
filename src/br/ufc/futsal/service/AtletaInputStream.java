package br.ufc.futsal.service;

import br.ufc.futsal.model.Atleta;

import java.io.IOException;
import java.io.InputStream;

public class AtletaInputStream extends InputStream {
    private InputStream origem;

    public AtletaInputStream(InputStream origem) {
        this.origem = origem;
    }

    @Override
    public int read() throws IOException {
        return origem.read();
    }

    public Atleta lerAtleta() throws IOException {
        int tamanho = origem.read();

        if (tamanho == -1) {
            return null; // Fim do stream
        }

        byte[] buffer = new byte[tamanho];
        origem.read(buffer);

        String linha  = new String(buffer);

        String[] partes = linha.split(",");

        String nome = partes[0];
        int numero  = Integer.parseInt(partes[1]);
        String posicao  = partes[2];

        return new Atleta(nome, numero, posicao);
    }
}
