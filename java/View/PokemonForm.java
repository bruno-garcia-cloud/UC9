package View;

import Controller.PokemonController;
import Controller.TreinadorController;
import Model.Pokemon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PokemonForm extends JInternalFrame {

    private PokemonController controller;
    private JTextField txtNome, txtTipoPrimario, txtTipoSecundario, txtNivel, txtHpMaximo;
    private JButton btnSalvar, btnAtualizar, btnRemover, btnListar;
    private JTable tabela;
    private DefaultTableModel tableModel;

    public PokemonForm(PokemonController controller) {
        super("Gerenciamento de Pokémon", true, true, true, true);
        this.controller = controller;

        setSize(800, 600);
        setLayout(new BorderLayout());

        // Painel do formulário
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // Nome
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        txtNome = new JTextField(15);
        panelForm.add(txtNome, gbc);
        row++;

        // Tipo Primário
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Tipo Primário:"), gbc);
        gbc.gridx = 1;
        txtTipoPrimario = new JTextField(15);
        panelForm.add(txtTipoPrimario, gbc);
        row++;

        // Tipo Secundário
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Tipo Secundário:"), gbc);
        gbc.gridx = 1;
        txtTipoSecundario = new JTextField(15);
        panelForm.add(txtTipoSecundario, gbc);
        row++;

        // Nível
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("Nível (1-100):"), gbc);
        gbc.gridx = 1;
        txtNivel = new JTextField(15);
        panelForm.add(txtNivel, gbc);
        row++;

        // HP Máximo
        gbc.gridx = 0; gbc.gridy = row;
        panelForm.add(new JLabel("HP Máximo (>0):"), gbc);
        gbc.gridx = 1;
        txtHpMaximo = new JTextField(15);
        panelForm.add(txtHpMaximo, gbc);
        row++;

        // Botões
        JPanel panelButtons = new JPanel(new FlowLayout());
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvarPokemon());
        panelButtons.add(btnSalvar);

        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> atualizarPokemon());
        panelButtons.add(btnAtualizar);

        btnRemover = new JButton("Remover");
        btnRemover.addActionListener(e -> removerPokemon());
        panelButtons.add(btnRemover);

        btnListar = new JButton("Listar Todos");
        btnListar.addActionListener(e -> listarPokemons());
        panelButtons.add(btnListar);

        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        panelForm.add(panelButtons, gbc);

        add(panelForm, BorderLayout.NORTH);

        // Tabela
        String[] colunas = {"ID", "Treinador", "Nome", "Tipo Primário", "Tipo Secundário", "Nível", "HP Máximo", "HP Atual"};
        tableModel = new DefaultTableModel(colunas, 0);
        tabela = new JTable(tableModel);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }



    private void salvarPokemon() {
        try {
            String nome = txtNome.getText().trim();
            String tipoPrimario = txtTipoPrimario.getText().trim();
            String tipoSecundario = txtTipoSecundario.getText().trim();
            int nivel = Integer.parseInt(txtNivel.getText().trim());
            int hpMaximo = Integer.parseInt(txtHpMaximo.getText().trim());

            if (nome.isEmpty() || tipoPrimario.isEmpty()) {
                throw new IllegalArgumentException("Nome e Tipo Primário são obrigatórios.");
            }

            if (tipoSecundario.isEmpty()) {
                tipoSecundario = null;
            }

            controller.cadastrarPokemon(nome, tipoPrimario, tipoSecundario, nivel, hpMaximo, hpMaximo);
            JOptionPane.showMessageDialog(this, "Pokémon cadastrado com sucesso!");
            limparCampos();
            listarPokemons();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Nível ou HP Máximo inválido.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar Pokémon: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarPokemon() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um Pokémon na tabela para atualizar.");
            return;
        }

        try {
            int id = (int) tabela.getValueAt(row, 0);
            int fkTreinador = (int) tabela.getValueAt(row, 1); // supondo que já tenha
            String nome = JOptionPane.showInputDialog(this, "Novo nome:", tabela.getValueAt(row, 2));
            String tipoPrimario = JOptionPane.showInputDialog(this, "Novo tipo primário:", tabela.getValueAt(row, 3));
            String tipoSecundario = JOptionPane.showInputDialog(this, "Novo tipo secundário:", tabela.getValueAt(row, 4));
            int nivel = Integer.parseInt(JOptionPane.showInputDialog(this, "Novo nível:", tabela.getValueAt(row, 5)));
            int hpMaximo = Integer.parseInt(JOptionPane.showInputDialog(this, "Novo HP Máximo:", tabela.getValueAt(row, 6)));
            int hpAtual = Integer.parseInt(JOptionPane.showInputDialog(this, "Novo HP Atual:", tabela.getValueAt(row, 7)));

            controller.atualizarPokemon(id, fkTreinador, nome, tipoPrimario, tipoSecundario, nivel, hpMaximo, hpAtual);
            JOptionPane.showMessageDialog(this, "Pokémon atualizado com sucesso!");
            listarPokemons();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerPokemon() {
        int row = tabela.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um Pokémon na tabela para remover.");
            return;
        }

        int id = (int) tabela.getValueAt(row, 0);

        try {
            controller.removerPokemon(id);
            JOptionPane.showMessageDialog(this, "Pokémon removido com sucesso!");
            listarPokemons();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao remover: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarPokemons() {
        tableModel.setRowCount(0); // limpa tabela
        List<Pokemon> pokemons = controller.listarTodosPokemons();
        for (Pokemon p : pokemons) {
            tableModel.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    p.getTipoPrimario(),
                    p.getTipoSecundario(),
                    p.getNivel(),
                    p.getHpMaximo(),
                    p.getHpAtual()
            });
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtTipoPrimario.setText("");
        txtTipoSecundario.setText("");
        txtNivel.setText("");
        txtHpMaximo.setText("");
    }
}
