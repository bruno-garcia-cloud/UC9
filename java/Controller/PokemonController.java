package Controller;

import Model.DAO.PokemonDAO;
import Model.Pokemon;

import java.sql.SQLException;
import java.util.List;

public class PokemonController {
    private PokemonDAO pokemonDAO;

    public PokemonController() {
        this.pokemonDAO = new PokemonDAO();
    }

    public void cadastrarPokemon(String nome, String tipoPrimario, String tipoSecundario, int nivel, int hpMaximo, int hpAtual) throws Exception {
        Pokemon pokemon = new Pokemon(nome, tipoPrimario, tipoSecundario, nivel, hpMaximo, hpAtual);
        try {
            pokemonDAO.inserir(pokemon);
        } catch (SQLException e) {
            throw new Exception("Erro ao cadastrar Pokémon no banco de dados: " + e.getMessage());
        }
    }
    public void atualizarPokemon(int id, int fk_id_Treinador, String nome, String tipoPrimario, String tipoSecundario, int nivel, int hpMaximo, int hpAtual) throws Exception {
        Pokemon pokemon = new Pokemon(id, fk_id_Treinador, nome, tipoPrimario, tipoSecundario, nivel, hpMaximo, hpAtual);
        try {
            pokemonDAO.atualizar(pokemon);
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar Pokémon no banco de dados: " + e.getMessage());
        }
    }

    public List<Pokemon> listarTodosPokemons() {
        return pokemonDAO.listarTodos();
    }


    public Pokemon buscarPokemonPorId(int id) {
        return pokemonDAO.buscarPorId(id);
    }

    public void removerPokemon(int id) throws Exception {
        try {
            pokemonDAO.remover(id);
        } catch (SQLException e) {
            throw new Exception("Erro ao remover Pokémon: " + e.getMessage());
        }
    }

    public List<Pokemon> buscarPokemonPorNome(String nome) {
        return pokemonDAO.buscarPorNome(nome);
    }

    public void insereListaPokemons(List<Pokemon> listaPokemons) throws SQLException {
        pokemonDAO.inserirListaPokemons(listaPokemons);
    }


    public boolean pokemonJaExiste(String nome) throws SQLException {
        return pokemonDAO.pokemonJaExiste(nome);
    }


    public void atualizarNivelEmLote(List<Pokemon> listaPokemon) throws SQLException {
        pokemonDAO.atualizarEmLoteLVL(listaPokemon);
    }

    public void atualizarHpMaximoEmLote(List<Pokemon> listaPokemon) throws SQLException {
        pokemonDAO.atualizarEmLoteHP(listaPokemon);
    }

}
