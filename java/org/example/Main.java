package org.example;

import Controller.PokemonController;
import Controller.TreinadorController;
import View.PokemonForm;

import javax.swing.*;

public class Main extends JFrame {

    private JDesktopPane desktopPane;
    private PokemonController pokemonController;
    private TreinadorController treinadorController;

    public Main() {
        super("Sistema de Gerenciamento de Pokémons");
        this.pokemonController = new PokemonController();
        this.treinadorController = new TreinadorController();

        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        desktopPane = new JDesktopPane();
        setContentPane(desktopPane);

        createMenuBar();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Pokémons
        JMenu menuPokemons = new JMenu("Pokémons");
        JMenuItem itemCadastrarPokemon = new JMenuItem("Cadastrar Pokémon");
        JMenuItem itemListarPokemons = new JMenuItem("Listar Pokémons");

        itemCadastrarPokemon.addActionListener(e -> openPokemonForm(null));

        menuPokemons.add(itemCadastrarPokemon);
        menuPokemons.add(itemListarPokemons);
        menuBar.add(menuPokemons);

     ;




        // Menu Sair
        JMenu menuSair = new JMenu("Sair");
        JMenuItem itemSair = new JMenuItem("Sair do Sistema");
        itemSair.addActionListener(e -> System.exit(0));
        menuSair.add(itemSair);
        menuBar.add(menuSair);

        setJMenuBar(menuBar);
    }

    // Abre o formulário de Pokémon
    private void openPokemonForm(PokemonForm existingForm) {
        PokemonForm form = new PokemonForm(pokemonController);
        desktopPane.add(form);
        form.setVisible(true);
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
