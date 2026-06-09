public class Atleta extends Pessoa {
    public int numero;
    public String posicao;
    public Atleta(String nome, int numero, String posicao) {
        super(nome);
        this.numero = numero;
        this.posicao = posicao;
    }
}