package org.example;

import cadastroProduto.Produto;

import javax.swing.*;
import java.util.List;

public class Main extends JFrame {

    public Main() {
        super("Cadastro de Produtos");
        ProdutoUI ui = new ProdutoUI();
        setContentPane(ui.mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 1200);
        setLocationRelativeTo(null);
        setVisible(true);
        Database.conexao();

        ui.listaProdutosJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Produto selected = ui.listaProdutosJList.getSelectedValue();
                if (selected != null) {
                    ui.campoNome.setText(selected.getNome());
                    ui.campoPreco.setText(String.valueOf(selected.getPreco()));
                    ui.campoQuantidade.setText(String.valueOf(selected.getQuantidade()));
                }
            }
        });

        ui.botaoCadastrar.addActionListener(e -> cadastrarProduto(ui));
        ui.botaoLimpar.addActionListener(e -> limparCampos(ui));
        ui.botaoListar.addActionListener(e -> listarProdutos(ui));
        ui.botaoAtualizar.addActionListener(e -> atualizaProduto(ui));
        ui.botaoExcluir.addActionListener(e -> exluirProduto(ui));
    }

    private void cadastrarProduto(ProdutoUI ui) {
        try {
            Produto p = Produto.fromFields(
                    ui.campoNome.getText(),
                    ui.campoPreco.getText(),
                    ui.campoQuantidade.getText()
            );
            Database.AddProduct(p);
            JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
            limparCampos(ui);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço ou quantidade inválidos. Digite números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos(ProdutoUI ui) {
        ui.campoNome.setText("");
        ui.campoPreco.setText("");
        ui.campoQuantidade.setText("");
    }

    private void atualizaProduto(ProdutoUI ui) {
        Produto selected = ui.listaProdutosJList.getSelectedValue();
        if (selected == null || selected.getId() == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto válido para atualizar.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            String nome = ui.campoNome.getText();
            double preco = Double.parseDouble(ui.campoPreco.getText());
            int quantidade = Integer.parseInt(ui.campoQuantidade.getText());
            Produto atualizado = new Produto(selected.getId(), nome, preco, quantidade);
            Database.UpdateProduct(atualizado);
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            listarProdutos(ui);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço ou quantidade inválidos. Digite números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void listarProdutos(ProdutoUI ui) {
        ui.produtoListModel.clear();
        List<Produto> listaProdutos = Database.GetProducts();
        if (listaProdutos.isEmpty()) {
            ui.produtoListModel.addElement(new Produto("Nenhum produto cadastrado.", 0, 0));
            ui.listaProdutosJList.setEnabled(false);
        } else {
            for (Produto p : listaProdutos) {
                ui.produtoListModel.addElement(p);
            }
            ui.listaProdutosJList.setEnabled(true);
        }
        ui.labelLista.setVisible(true);
        ui.listaProdutosJList.setVisible(true);
    }

    private void exluirProduto(ProdutoUI ui) {
        Produto selected = ui.listaProdutosJList.getSelectedValue();
        if (selected == null || selected.getId() == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto válido para excluir.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir o produto selecionado?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Database.DeleteProduct(selected.getId());
            JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
            listarProdutos(ui);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }


}

