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
    private JTextArea areaProdutos;
    private JLabel labelLista;

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

        areaProdutos = new JTextArea(10, 40);
        areaProdutos.setEditable(false);
        areaProdutos.setVisible(false);

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
        painelLista.add(new JScrollPane(areaProdutos), BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(painelSuperior, BorderLayout.NORTH);
        add(painelLista, BorderLayout.CENTER);

        botaoCadastrar.addActionListener(e -> cadastrarProduto());
        botaoLimpar.addActionListener(e -> limparCampos());
        botaoListar.addActionListener(e -> listarProdutos());

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
    private void listarProdutos() {
        StringBuilder sb = new StringBuilder();
        List<Produto> listaProdutos = Database.GetProducts();

        if (listaProdutos.isEmpty()) {
            sb.append("Nenhum produto cadastrado.");
        } else {
            for (Produto p : listaProdutos) {
                sb.append(p).append("\n");
            }
        }
        labelLista.setVisible(true);
        areaProdutos.setText(sb.toString());
        areaProdutos.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}