package cadastroProduto;

public class Produto {
    private String nome;
    private int quantidade;
    private double preco;

    public Produto(String nome, double preco, int quantidade) {
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public static Produto fromFields(String nome, String precoStr, String quantidadeStr) {
        // field validations
        if (nome == null || precoStr == null || quantidadeStr == null ||
                nome.trim().isEmpty() || precoStr.trim().isEmpty() || quantidadeStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Todos os campos devem ser preenchidos.");
        }

        double preco;
        int quantidade;
        try {
            preco = Double.parseDouble(precoStr.trim());
            quantidade = Integer.parseInt(quantidadeStr.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Preço ou quantidade inválidos. Digite números válidos.");
        }

        if (preco < 0 || quantidade < 0) {
            throw new IllegalArgumentException("Preço e quantidade não podem ser negativos.");
        }

        return new Produto(nome.trim(), preco, quantidade);
    }

    @Override
    public String toString() {
        return String.format("Nome: %s | Preço: R$ %.2f | Quantidade: %d", nome, preco, quantidade);
    }

}


