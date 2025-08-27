package Controller;

import Model.DAO.TreinadorDAO;
import Model.Treinador;
import java.sql.SQLException;
import java.util.List;

public class TreinadorController {
    private TreinadorDAO treinadorDAO;

    public TreinadorController() {
        this.treinadorDAO = new TreinadorDAO();
    }

    public void cadastrarTreinador(String nome, String cidade) throws Exception {
        Treinador treinador = new Treinador(nome, cidade);
        try {
            treinadorDAO.inserir(treinador);
        } catch (SQLException e) {
            throw new Exception("Erro ao cadastrar Treinador no banco de dados: " + e.getMessage());
        }
    }

    public List<Treinador> listarTodosTreinadores() {
        return treinadorDAO.listarTodos();
    }

    public Treinador buscarTreinadorPorId(int id) {
        return treinadorDAO.buscarPorId(id);
    }
}