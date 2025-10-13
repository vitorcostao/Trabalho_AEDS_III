package controle;

import java.util.ArrayList;
import java.util.Scanner;

import model.ListaProduto;
import model.Produto;
import model.Usuario;
import service.ListaProduto.ArquivoListaProduto;
import java.util.concurrent.ThreadLocalRandom;

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

    public static String gerarGTIN13() {
        StringBuilder sb = new StringBuilder(13);
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            int d = ThreadLocalRandom.current().nextInt(10);
            sb.append(d);
            soma += (i % 2 == 0) ? d : 3 * d;
        }
        int dv = (10 - (soma % 10)) % 10;
        sb.append(dv);
        return sb.toString();
    }

    public Produto cadastrarProduto(String nome, String descricao, int quantidade, String observacao) throws Exception {

        String gtin;
        do {
            gtin = gerarGTIN13();

        } while (arquivoProduto.read(gtin) != null);

        Produto novo = new Produto(-1, nome, gtin, descricao, quantidade, observacao);
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


        System.out.print("Nova descrição (deixe em branco para manter): ");
        String descricao = sc.nextLine();
        if (!descricao.isEmpty())
            produtoAntigo.setDescricao(descricao);

        System.out.print("Nova quantidade (deixe em branco para manter): ");
        int quantidade =  0;
        quantidade = sc.nextInt();
        if (quantidade != 0)
            produtoAntigo.setQuantidade(quantidade);

        System.out.println("Nova observação (deixe em branco para manter): ");
        String observacao = sc.nextLine();
        if (!observacao.isEmpty())
            produtoAntigo.setObservacoes(observacao);


        return arquivoProduto.update(produtoAntigo);
    }

/*
    public boolean DesativarProduto(int idProduto) throws Exception {


        //aqui eu quero pegar o produto e atualizar o valor "Status" pra 1 (desativado) mas não consegui
        // fazendo um produto.Desativar(); mas não sei como acessar o produto DIRETAMENTE



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
