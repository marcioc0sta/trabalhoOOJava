package org.example;

import cadastroProduto.Produto;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProdutoUI {
    public JTextField campoNome, campoPreco, campoQuantidade;
    public JButton botaoCadastrar, botaoLimpar, botaoListar, botaoExcluir, botaoAtualizar;
    public JLabel labelLista;
    public DefaultListModel<Produto> produtoListModel;
    public JList<Produto> listaProdutosJList;
    public JPanel mainPanel;

    public ProdutoUI() {
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

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(painelSuperior, BorderLayout.NORTH);
        mainPanel.add(painelLista, BorderLayout.CENTER);
    }
}

