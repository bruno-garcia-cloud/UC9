package Model.DAO;

import Model.Treinador;
import Conexao.ConexaoPostgresDB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TreinadorDAO {

    public void inserir(Treinador treinador) throws SQLException {
        String sql = "INSERT INTO treinadores (nome, cidade) VALUES (?, ?)";
        try (Connection conn = ConexaoPostgresDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, treinador.getNome());
            stmt.setString(2, treinador.getCidade());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    treinador.setId(rs.getInt(1));
                }
            }
        }
    }

    public Treinador buscarPorId(int id) {
        String sql = "SELECT * FROM treinadores WHERE id_treinador = ?";
        try (Connection conn = ConexaoPostgresDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Treinador(
                            rs.getInt("id_treinador"),
                            rs.getString("nome"),
                            rs.getString("cidade")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Treinador por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Treinador> listarTodos() {
        List<Treinador> treinadores = new ArrayList<>();
        String sql = "SELECT * FROM treinadores ORDER BY nome";
        try (Connection conn = ConexaoPostgresDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                treinadores.add(new Treinador(
                        rs.getInt("id_treinador"),
                        rs.getString("nome"),
                        rs.getString("cidade")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os Treinadores: " + e.getMessage());
        }
        return treinadores;
    }
}
