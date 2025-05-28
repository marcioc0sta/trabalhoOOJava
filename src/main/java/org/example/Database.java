package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import cadastroProduto.Produto;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static String USER = "postgres";//usuario
    private static String PASSWORD = "postgres";//senha
    private static Connection conn = null;

    public static void conexao() {
        try{
            // Estabelecendo a conexão
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (conn != null) {
                System.out.println("Conexao estabelecida com sucesso!");
            } else {
                System.out.println("Falha ao conectar ao banco de dados.");
            }
        }catch (
                SQLException e) {
            e.printStackTrace();
            System.out.println("Deu Algum Problema");

        } finally {
            try {
                // Fechar a conexão se ela foi aberta
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println("Deu Algum Problema para Fechar conexao");

            }
        }
    }

    public static List<Produto> GetProducts() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT nome, preco, quantidade FROM produtos";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                int quantidade = rs.getInt("quantidade");
                produtos.add(new Produto(nome, preco, quantidade));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produtos;
    }

    public static void AddProduct(Produto produto) {
        String sql = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, produto.getNome());
            pstmt.setDouble(2, produto.getPreco());
            pstmt.setInt(3, produto.getQuantidade());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
