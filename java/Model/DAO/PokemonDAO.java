package Model.DAO;

import Model.Pokemon;
import Conexao.ConexaoPostgresDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PokemonDAO {

    public void inserir(Pokemon pokemon) throws SQLException {
        String sql = "INSERT INTO pokemons (fk_id_treinador, nome, tipo_primario, tipo_secundario, nivel, hp_maximo, hp_atual) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoPostgresDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (pokemon.getFk_id_treinador() == null) {
                stmt.setNull(1, java.sql.Types.INTEGER);
            } else {
                stmt.setInt(1, pokemon.getFk_id_treinador());
            }

            stmt.setString(2, pokemon.getNome());
            stmt.setString(3, pokemon.getTipoPrimario());
            stmt.setString(4, pokemon.getTipoSecundario());
            stmt.setInt(5, pokemon.getNivel());
            stmt.setInt(6, pokemon.getHpMaximo());
            stmt.setInt(7, pokemon.getHpAtual());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pokemon.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Pokemon pokemon) throws SQLException {
        String sql = "UPDATE pokemons SET fk_id_treinador = ?, nome = ?, tipo_primario = ?, tipo_secundario = ?, nivel = ?, hp_maximo = ?, hp_atual = ? WHERE id_pokemon = ?";
        try (Connection conn = ConexaoPostgresDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (pokemon.getFk_id_treinador() == null) {
                stmt.setNull(1, Types.INTEGER);
            } else {
                stmt.setInt(1, pokemon.getFk_id_treinador());
            }
            stmt.setString(2, pokemon.getNome());
            stmt.setString(3, pokemon.getTipoPrimario());
            stmt.setString(4, pokemon.getTipoSecundario());
            stmt.setInt(5, pokemon.getNivel());
            stmt.setInt(6, pokemon.getHpMaximo());
            stmt.setInt(7, pokemon.getHpAtual());
            stmt.setInt(8, pokemon.getId());

            stmt.executeUpdate();
        }
    }

    //Remover
    public void remover(int id) throws SQLException {
        String sql = "DELETE FROM pokemons WHERE id_pokemon = ?";
        try (Connection conn = ConexaoPostgresDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    //Buscar por ID
    public Pokemon buscarPorId(int id) {
        String sql = "SELECT * FROM pokemons WHERE id_pokemon = ?";
        try (Connection conn = ConexaoPostgresDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Pokemon(
                            rs.getInt("id_pokemon"),
                            rs.getInt("fk_id_treinador"),
                            rs.getString("nome"),
                            rs.getString("tipo_primario"),
                            rs.getString("tipo_secundario"),
                            rs.getInt("nivel"),
                            rs.getInt("hp_maximo"),
                            rs.getInt("hp_atual")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Pokémon por ID: " + e.getMessage());
        }
        return null;
    }

    //Listar Todos
    public List<Pokemon> listarTodos() {
        List<Pokemon> pokemons = new ArrayList<>();
        String sql = "SELECT * FROM pokemons ORDER BY nome";
        try (Connection conn = ConexaoPostgresDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pokemons.add(new Pokemon(
                        rs.getInt("id_pokemon"),
                        rs.getInt("fk_id_treinador"),
                        rs.getString("nome"),
                        rs.getString("tipo_primario"),
                        rs.getString("tipo_secundario"),
                        rs.getInt("nivel"),
                        rs.getInt("hp_maximo"),
                        rs.getInt("hp_atual")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar todos os Pokémons: " + e.getMessage());
        }
        return pokemons;
    }

    //Buscar por Nome
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
                            rs.getInt("fk_id_treinador"),
                            rs.getString("nome"),
                            rs.getString("tipo_primario"),
                            rs.getString("tipo_secundario"),
                            rs.getInt("nivel"),
                            rs.getInt("hp_maximo"),
                            rs.getInt("hp_atual")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar Pokémon por nome: " + e.getMessage());
        }
        return pokemons;
    }

    //Ja existe
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
        String sql = "INSERT INTO pokemons (fk_id_treinador, nome, tipo_primario, tipo_secundario, nivel, hp_maximo, hp_atual) VALUES (?, ?, ?, ?, ?, ?, ?)";
        final int BATCH_SIZE = 1000;

        try (Connection conn = ConexaoPostgresDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);

            for (int i = 0; i < listaPokemon.size(); i++) {
                Pokemon p = listaPokemon.get(i);

                if (p.getFk_id_treinador() == null) {
                    stmt.setNull(1, Types.INTEGER);
                } else {
                    stmt.setInt(1, p.getFk_id_treinador());
                }

                stmt.setString(2, p.getNome());
                stmt.setString(3, p.getTipoPrimario());
                stmt.setString(4, p.getTipoSecundario());
                stmt.setInt(5, p.getNivel());
                stmt.setInt(6, p.getHpMaximo());
                stmt.setInt(7, p.getHpAtual());
                stmt.addBatch();

                // Quando atingir o tamanho do lote ou for o último item
                if ((i + 1) % BATCH_SIZE == 0 || i + 1 == listaPokemon.size()) {
                    stmt.executeBatch();

                    // Obter os IDs gerados para os pokémons recém inseridos
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        List<Pokemon> batchPokemons = listaPokemon.subList(i + 1 - (i % BATCH_SIZE == 0 ? BATCH_SIZE : (i % BATCH_SIZE) + 1), i + 1);
                        int index = 0;
                        while (rs.next() && index < batchPokemons.size()) {
                            batchPokemons.get(index).setId(rs.getInt(1));
                            index++;
                        }
                    }

                    stmt.clearBatch();
                }
            }

            conn.commit();
            conn.setAutoCommit(true);

        } catch (SQLException e) {
            throw new SQLException("Erro ao inserir lista de pokémons: " + e.getMessage(), e);
        }
    }

    public void atualizarEmLoteLVL(List<Pokemon> listaPokemon) throws SQLException {
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
                stmt.addBatch();

                if(i+1==listaPokemon.size() ||(i+1) % Batch_size == 0 ) {
                    stmt.executeBatch();
                    stmt.clearBatch();
                }

            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar hp em lote" + e.getMessage());
        }
        conn.commit();
        conn.setAutoCommit(true);
    }

    public void atualizarEmLoteHP(List<Pokemon> listaPokemon) throws SQLException {
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
            System.err.println("Erro ao atualizar lvl em lote" + e.getMessage());
        }
        conn.commit();
        conn.setAutoCommit(true);
    }

}