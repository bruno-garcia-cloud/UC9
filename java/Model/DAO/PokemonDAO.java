package Model.DAO;

import Model.Pokemon;
import Conexao.ConexaoPostgresDB; // Certifique-se que esta classe existe e funciona
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PokemonDAO {

    public void inserir(Pokemon pokemon) throws SQLException {
        String sql = "INSERT INTO pokemons (nome, tipo_primario, tipo_secundario, nivel, hp_maximo) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoPostgresDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pokemon.getNome());
            stmt.setString(2, pokemon.getTipoPrimario());
            stmt.setString(3, pokemon.getTipoSecundario());
            stmt.setInt(4, pokemon.getNivel());
            stmt.setInt(5, pokemon.getHpMaximo());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pokemon.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Pokemon pokemon) throws SQLException {
        String sql = "UPDATE pokemons SET nome = ?, tipo_primario = ?, tipo_secundario = ?, nivel = ?, hp_maximo = ? WHERE id_pokemon = ?";
        try (Connection conn = ConexaoPostgresDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pokemon.getNome());
            stmt.setString(2, pokemon.getTipoPrimario());
            stmt.setString(3, pokemon.getTipoSecundario());
            stmt.setInt(4, pokemon.getNivel());
            stmt.setInt(5, pokemon.getHpMaximo());
            stmt.setInt(6, pokemon.getId());
            stmt.executeUpdate();
        }
    }

    public void remover(int id) throws SQLException {
        String sql = "DELETE FROM pokemons WHERE id_pokemon = ?";
        try (Connection conn = ConexaoPostgresDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Pokemon buscarPorId(int id) {
        String sql = "SELECT * FROM pokemons WHERE id_pokemon = ?";
        try (Connection conn = ConexaoPostgresDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Pokemon(
                            rs.getInt("id_pokemon"),
                            rs.getString("nome"),
                            rs.getString("tipo_primario"),
                            rs.getString("tipo_secundario"),
                            rs.getInt("nivel"),
                            rs.getInt("hp_maximo")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Pokémon por ID: " + e.getMessage());
        }
        return null;
    }

    public List<Pokemon> listarTodos() {
        List<Pokemon> pokemons = new ArrayList<>();
        String sql = "SELECT * FROM pokemons ORDER BY nome";
        try (Connection conn = ConexaoPostgresDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pokemons.add(new Pokemon(
                        rs.getInt("id_pokemon"),
                        rs.getString("nome"),
                        rs.getString("tipo_primario"),
                        rs.getString("tipo_secundario"),
                        rs.getInt("nivel"),
                        rs.getInt("hp_maximo")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os Pokémons: " + e.getMessage());
        }
        return pokemons;
    }

    public List<Pokemon> buscarPorNome(String nomeBusca) {
        List<Pokemon> pokemons = new ArrayList<>();
        String sql = "SELECT * FROM pokemons WHERE LOWER(nome) LIKE LOWER(?) ORDER BY nome";
        try (Connection conn = ConexaoPostgresDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + nomeBusca + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pokemons.add(new Pokemon(
                            rs.getInt("id_pokemon"),
                            rs.getString("nome"),
                            rs.getString("tipo_primario"),
                            rs.getString("tipo_secundario"),
                            rs.getInt("nivel"),
                            rs.getInt("hp_maximo")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Pokémon por nome: " + e.getMessage());
        }
        return pokemons;
    }

    public boolean pokemonJaExiste(String nome) throws SQLException {
        String sql = "SELECT COUNT(*) FROM pokemons WHERE LOWER(nome) = LOWER(?)";
        try (Connection conn = ConexaoPostgresDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    public void inserirListaPokemons(List<Pokemon> listaPokemon) throws SQLException {
        String sql = "INSERT INTO pokemons (nome, tipo_primario, tipo_secundario, nivel, hp_maximo) VALUES (?, ?, ?, ?, ?)";
        Connection conn = ConexaoPostgresDB.conectar();
        conn.setAutoCommit(false);
        PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        final int Batch_size = 1000;
        for (int i = 0; i < listaPokemon.size();i++) {
            Pokemon meuPokemon = listaPokemon.get(i);
            stmt.setString(1, meuPokemon.getNome());
            stmt.setString(2, meuPokemon.getTipoPrimario());
            stmt.setString(3, meuPokemon.getTipoSecundario());
            stmt.setInt(4, meuPokemon.getNivel());
            stmt.setInt(5, meuPokemon.getHpMaximo());
            stmt.addBatch(); //Adiciona a intrucao ao lote
            if(i+1==listaPokemon.size() ||(i+1) % Batch_size == 0 ) {
                stmt.executeBatch();
                stmt.clearBatch();
            }
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    meuPokemon.setId(rs.getInt(1));
                }
            }
        }
        conn.commit();
        conn.setAutoCommit(true);
    }
    public void atualizarEmLoteHP(List<Pokemon> listaPokemon) throws SQLException {
        String sql = "UPDATE pokemons SET nivel = ? WHERE id_pokemon = ?";
        Connection conn = ConexaoPostgresDB.conectar();
        conn.setAutoCommit(false);
        PreparedStatement stmt = null;
        final int Batch_size = 1000;

        try {
            conn = ConexaoPostgresDB.conectar();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < listaPokemon.size();i++) {
                Pokemon meuPokemon = listaPokemon.get(i);
                stmt.setInt(1, meuPokemon.getNivel());
                stmt.setInt(2, meuPokemon.getId());
                stmt.addBatch(); //Adiciona a intrucao ao lote

                if(i+1==listaPokemon.size() ||(i+1) % Batch_size == 0 ) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }

            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar em lote" + e.getMessage());
        }
        conn.commit();
        conn.setAutoCommit(true);
    }

    public void atualizarEmLoteLVL(List<Pokemon> listaPokemon) throws SQLException {
        String sql = "UPDATE pokemons SET hp_maximo = ? WHERE id_pokemon = ?";
        Connection conn = ConexaoPostgresDB.conectar();
        conn.setAutoCommit(false);
        PreparedStatement stmt = null;
        final int Batch_size = 1000;

        try {
            conn = ConexaoPostgresDB.conectar();
            conn.setAutoCommit(false);
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < listaPokemon.size();i++) {
                Pokemon meuPokemon = listaPokemon.get(i);
                stmt.setInt(1, meuPokemon.getHpMaximo());
                stmt.setInt(2, meuPokemon.getId());
                stmt.addBatch(); //Adiciona a intrucao ao lote

                if(i+1==listaPokemon.size() ||(i+1) % Batch_size == 0 ) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }

            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar em lote" + e.getMessage());
        }
        conn.commit();
        conn.setAutoCommit(true);
    }
}