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
        String sql = "SELECT id, nome, preco, quantidade FROM produtos ORDER BY id";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String nome = rs.getString("nome");
                double preco = rs.getDouble("preco");
                int quantidade = rs.getInt("quantidade");
                int id = rs.getInt("id");
                produtos.add(new Produto(id, nome, preco, quantidade));
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

    public static void UpdateProduct(Produto produto) {
        String sql = "UPDATE produtos SET preco = ?, quantidade = ?, nome = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, produto.getPreco());
            pstmt.setInt(2, produto.getQuantidade());
            pstmt.setString(3, produto.getNome());
            pstmt.setInt(4, produto.getId()); // Pass id as integer
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Produto GetProductById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido. Deve ser maior que zero.");
        }
        String sql = "SELECT nome, preco, quantidade FROM produtos WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    double preco = rs.getDouble("preco");
                    int quantidade = rs.getInt("quantidade");
                    return new Produto(id, nome, preco, quantidade);
                } else {
                    throw new IllegalArgumentException("Produto não encontrado.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void DeleteProduct(int id) {
        Produto produto = GetProductById(id);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado.");
        }
        if (produto.getQuantidade() > 1) {
            String updateSql = "UPDATE produtos SET quantidade = quantidade - 1 WHERE id = ?";
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, id);
                updateStmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String deleteSql = "DELETE FROM produtos WHERE id = ?";
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                deleteStmt.setInt(1, id);
                deleteStmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
