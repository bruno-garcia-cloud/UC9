package Model;

public class Treinador {
    private int id_treinador;
    private String nome;
    private String cidade;

    public Treinador(int id, String nome, String cidade) {
        this.id_treinador = id;
        this.nome = nome;
        this.cidade = cidade;
    }

    public Treinador(String nome, String cidade) {
        this.nome = nome;
        this.cidade = cidade;
    }

    public int getId() { return id_treinador; }
    public void setId(int id) { this.id_treinador = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    @Override
    public String toString() {
        return "Pokemon{" +
                "id=" + id_treinador +
                ", nome='" + nome + '\'' +
                ", cidade='" + cidade +
                '}';
    }
}
