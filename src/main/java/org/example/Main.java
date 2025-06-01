package org.example;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import cadastroProduto.Produto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main extends JFrame {
    private JTextField campoNome, campoPreco, campoQuantidade;
    private JButton botaoCadastrar, botaoLimpar, botaoListar, botaoExcluir, botaoAtualizar;
    private JLabel labelLista;
    private DefaultListModel<Produto> produtoListModel;
    private JList<Produto> listaProdutosJList;

    public Main() {
        super("Cadastro de Produtos");

        campoNome = new JTextField();
        campoPreco = new JTextField();
        campoQuantidade = new JTextField();

        Dimension campoSize = new Dimension(200, 25);
        campoNome.setPreferredSize(campoSize);
        campoPreco.setPreferredSize(campoSize);
        campoQuantidade.setPreferredSize(campoSize);

        botaoCadastrar = new JButton("Cadastrar");
        botaoLimpar = new JButton("Limpar");
        botaoListar = new JButton("Listar");
        botaoExcluir = new JButton("Excluir");
        botaoAtualizar = new JButton("Atualizar");

        Dimension botaoSize = new Dimension(100, 30);
        Color corLilás = Color.decode("#ccccff");

        botaoCadastrar.setPreferredSize(botaoSize);
        botaoCadastrar.setBackground(corLilás);

        botaoLimpar.setPreferredSize(botaoSize);
        botaoLimpar.setBackground(corLilás);

        botaoListar.setPreferredSize(botaoSize);
        botaoListar.setBackground(corLilás);

        produtoListModel = new DefaultListModel<>();
        listaProdutosJList = new JList<>(produtoListModel);
        listaProdutosJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaProdutosJList.setVisible(false);
        listaProdutosJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Produto selected = listaProdutosJList.getSelectedValue();
                if (selected != null) {
                    campoNome.setText(selected.getNome());
                    campoPreco.setText(String.valueOf(selected.getPreco()));
                    campoQuantidade.setText(String.valueOf(selected.getQuantidade()));
                }
            }
        });

        labelLista = new JLabel("Produtos Cadastrados:");
        labelLista.setVisible(false);

        JPanel painelEntrada = new JPanel();
        painelEntrada.setLayout(new BoxLayout(painelEntrada, BoxLayout.Y_AXIS));

        JPanel linhaNome = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaNome.add(new JLabel("Nome:"));
        linhaNome.add(campoNome);

        JPanel linhaPreco = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaPreco.add(new JLabel("Preço:"));
        linhaPreco.add(campoPreco);

        JPanel linhaQuantidade = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linhaQuantidade.add(new JLabel("Quantidade:"));
        linhaQuantidade.add(campoQuantidade);

        painelEntrada.add(linhaNome);
        painelEntrada.add(linhaPreco);
        painelEntrada.add(linhaQuantidade);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoLimpar);
        painelBotoes.add(botaoListar);
        painelBotoes.add(botaoExcluir);
        painelBotoes.add(botaoAtualizar);

        JPanel painelSuperior = new JPanel();
        painelSuperior.setLayout(new BorderLayout());
        painelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 10, 20));
        painelSuperior.add(painelEntrada, BorderLayout.NORTH);
        painelSuperior.add(painelBotoes, BorderLayout.CENTER);

        JPanel painelLista = new JPanel(new BorderLayout());
        painelLista.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        painelLista.add(labelLista, BorderLayout.NORTH);
        painelLista.add(new JScrollPane(listaProdutosJList), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(painelSuperior, BorderLayout.NORTH);
        add(painelLista, BorderLayout.CENTER);

        botaoCadastrar.addActionListener(e -> cadastrarProduto());
        botaoLimpar.addActionListener(e -> limparCampos());
        botaoListar.addActionListener(e -> listarProdutos());
        botaoAtualizar.addActionListener(e -> atualizaProduto());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 1200);
        setLocationRelativeTo(null);
        setVisible(true);

        Database.conexao();
    }

    private void cadastrarProduto() {
        try {
            Produto p = Produto.fromFields(
                    campoNome.getText(),
                    campoPreco.getText(),
                    campoQuantidade.getText()
            );
            Database.AddProduct(p);

            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
            limparCampos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço ou quantidade inválidos. Digite números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        campoNome.setText("");
        campoPreco.setText("");
        campoQuantidade.setText("");
    }

    private void atualizaProduto() {
        Produto selected = listaProdutosJList.getSelectedValue();
        if (selected == null || selected.getId() == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto válido para atualizar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String nome = campoNome.getText();
            double preco = Double.parseDouble(campoPreco.getText());
            int quantidade = Integer.parseInt(campoQuantidade.getText());
            Produto atualizado = new Produto(selected.getId(), nome, preco, quantidade);
            Database.UpdateProduct(atualizado);
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            listarProdutos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço ou quantidade inválidos. Digite números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarProdutos() {
        produtoListModel.clear();
        List<Produto> listaProdutos = Database.GetProducts();
        if (listaProdutos.isEmpty()) {
            produtoListModel.addElement(new Produto("Nenhum produto cadastrado.", 0, 0));
            listaProdutosJList.setEnabled(false);
        } else {
            for (Produto p : listaProdutos) {
                produtoListModel.addElement(p);
            }
            listaProdutosJList.setEnabled(true);
        }
        labelLista.setVisible(true);
        listaProdutosJList.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}

