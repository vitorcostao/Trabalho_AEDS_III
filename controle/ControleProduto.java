package controle;

import java.util.ArrayList;
import java.util.Scanner;
import model.Produto;
import model.Usuario;
import service.ListaProduto.ArquivoListaProduto;

import service.Produtos.ArquivoProduto;

public class ControleProduto {

    private static ArquivoProduto arquivoProduto;
    private static ArquivoListaProduto arquivoListaProduto;
    private static Usuario usuarioLogado;

    public ControleProduto(Usuario usuarioLogado) throws Exception {
        arquivoProduto = new ArquivoProduto();
        arquivoListaProduto = new ArquivoListaProduto();
        ControleProduto.usuarioLogado = usuarioLogado;
    }

    public ArquivoProduto getArquivoProduto() {
        return arquivoProduto;
    }

    public ArquivoListaProduto getArquivoListaProduto() {
        return arquivoListaProduto;
    }

    public Produto cadastrarProduto(String nome, String gtin, String descricao) throws Exception {

        Produto novo = new Produto(-1, nome, gtin, descricao);
        int id = arquivoProduto.create(novo);
        novo.setId(id);
        return novo;
    }

    public boolean atualizarProduto(Scanner sc, int idProduto) throws Exception {
        Produto produtoAntigo = arquivoProduto.read(idProduto);
        if (produtoAntigo == null) {
            System.out.println("Produto não encontrado.");
            return false;
        }

        System.out.print("Novo nome (deixe em branco para manter): ");
        String nome = sc.nextLine();
        if (!nome.isEmpty())
            produtoAntigo.setNome(nome);

        System.out.print("Novo GTIN (deixe em branco para manter): ");
        String gtin = sc.nextLine();
        if (!gtin.isEmpty())
            produtoAntigo.setGTIN(gtin);

        System.out.print("Nova descrição (deixe em branco para manter): ");
        String descricao = sc.nextLine();
        if (!descricao.isEmpty())
            produtoAntigo.setDescricao(descricao);

        return arquivoProduto.update(produtoAntigo);
    }

    /*
    public boolean removerProduto(int idProduto) throws Exception {
        ArrayList<ListaProduto> relacoes = arquivoListaProduto.readByProduto(idProduto);
        for (ListaProduto lp : relacoes) {
            if (lp.getIdLista() == listaAtual.getId()) {
                arquivoListaProduto.delete(lp.getId());
            }
        }

        return arquivoProduto.delete(idProduto);
    }

    */

    public void fechar() throws Exception {
        arquivoProduto.close();
        arquivoListaProduto.close();
    }

    public Produto buscarProdutoPorGTIN(String gtin) throws Exception {

        return arquivoProduto.read(gtin);
    }

    public ArrayList<Produto> listarProdutosOrdenados() throws Exception {
        ArrayList<Produto> produtos = arquivoProduto.readAll();
        produtos.sort((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()));
        return produtos;
    }
}
