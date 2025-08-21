package org.example;

import Model.Pokemon;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static List<Pokemon> listaPokemon = new ArrayList<>();
    private static JMenuBar menuBar = new JMenuBar();
    private static JMenuItem itemInserirListaPokemon = new JMenuItem("Inserir Lista de Pokémons");

    public static void main(String[] args) {
        inicializarPokemons();

        itemInserirListaPokemon.addActionListener(e -> {
            try {
                insereListaPokemons();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        menuBar.add(itemInserirListaPokemon);

        // Exemplo de interface (opcional)
        JFrame frame = new JFrame("Pokémon App");
        frame.setJMenuBar(menuBar);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void inicializarPokemons() {
        listaPokemon.add(new Pokemon("Bulbasaur", "Planta", "Veneno", 5, 15));
        listaPokemon.add(new Pokemon("Ivysaur", "Planta", "Veneno", 16, 48));
        listaPokemon.add(new Pokemon("Venusaur", "Planta", "Veneno", 36, 108));
        listaPokemon.add(new Pokemon("Charmander", "Fogo", null, 5, 15));
        // [... continue adicionando todos os outros pokémons ...]
        listaPokemon.add(new Pokemon("Mewtwo", "Psíquico", null, 70, 210));
        listaPokemon.add(new Pokemon("Mew", "Psíquico", null, 50, 150));
    }

    private static void insereListaPokemons() throws SQLException {
        for (Pokemon p : listaPokemon) {
            System.out.println(p); 
        }
    }
}
