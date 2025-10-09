package controle;

import java.util.ArrayList;
import java.util.Scanner;
import model.Lista;
import model.ListaProduto;
import model.Produto;
import model.Usuario;
import service.ListaProduto.ArquivoListaProduto;
import service.Listas.ArquivoLista;
import service.Produtos.ArquivoProduto;

public class ControleProduto {

    private static ArquivoProduto arquivoProduto;
    private static ArquivoListaProduto arquivoListaProduto;
    private static Usuario usuarioLogado;
    private static Lista listaAtual;

    public ControleProduto(Usuario usuarioLogado, Lista listaAtual) throws Exception {
        arquivoProduto = new ArquivoProduto();
        arquivoListaProduto = new ArquivoListaProduto();
        ControleProduto.usuarioLogado = usuarioLogado;
        ControleProduto.listaAtual = listaAtual;
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

    public boolean adicionarProdutoNaLista(int idProduto) throws Exception {
        if (listaAtual == null) {
            System.out.println("Nenhuma lista selecionada!");
            return false;
        }

        ListaProduto lp = new ListaProduto(listaAtual.getId(), idProduto);
        arquivoListaProduto.create(lp);
        return true;
    }

    public ArrayList<Produto> listarProdutosDaLista() throws Exception {
        ArrayList<Produto> produtos = new ArrayList<>();
        if (listaAtual == null)
            return produtos;

        ArrayList<ListaProduto> relacoes = arquivoListaProduto.readByLista(listaAtual.getId());
        for (ListaProduto lp : relacoes) {
            Produto p = arquivoProduto.read(lp.getIdProduto());
            if (p != null)
                produtos.add(p);
        }
        return produtos;
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

    public boolean removerProduto(int idProduto) throws Exception {
        ArrayList<ListaProduto> relacoes = arquivoListaProduto.readByProduto(idProduto);
        for (ListaProduto lp : relacoes) {
            if (lp.getIdLista() == listaAtual.getId()) {
                arquivoListaProduto.delete(lp.getId());
            }
        }

        return arquivoProduto.delete(idProduto);
    }

    public void exibirProdutosDaLista() throws Exception {
        System.out.println("Presente Fácil 1.0\n----------------------------------------->");
        System.out.println("> Início > Listas > " + listaAtual.getNome() + " > Produtos\n");

        ArrayList<Produto> produtos = listarProdutosDaLista();
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado nesta lista.");
            return;
        }

        for (Produto p : produtos) {
            System.out.println("ID: " + p.getId() + " | Nome: " + p.getNome() +
                    " | GTIN: " + p.getGTIN() +
                    " | Descrição: " + p.getDescricao());
        }
    }

    public void fechar() throws Exception {
        arquivoProduto.close();
        arquivoListaProduto.close();
    }

    // Métodos auxiliares úteis

    public Produto buscarProdutoPorGTIN(String gtin) throws Exception {
        return arquivoProduto.read(gtin); // usa o índice indireto
    }

    public ArrayList<Produto> listarProdutosOrdenados() throws Exception {
        ArrayList<Produto> produtos = arquivoProduto.readAll();
        produtos.sort((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()));
        return produtos;
    }

    public ArrayList<String> listarNomesDasMinhasListasQueContemProduto(int idProduto) throws Exception {
        ArrayList<String> nomes = new ArrayList<>();
        ArrayList<ListaProduto> relacoes = arquivoListaProduto.readByProduto(idProduto);

        for (ListaProduto lp : relacoes) {
            if (lp != null && lp.getId() != -1) {
                Lista lista = new ArquivoLista().read(lp.getIdLista());
                if (lista != null && lista.getIdUsuario() == usuarioLogado.getId()) {
                    nomes.add(lista.getNome());
                }
            }
        }

        return nomes;
    }

    public int contarListasDeOutrosUsuariosQueContemProduto(int idProduto) throws Exception {
        int count = 0;
        ArrayList<ListaProduto> relacoes = arquivoListaProduto.readByProduto(idProduto);

        for (ListaProduto lp : relacoes) {
            Lista lista = new ArquivoLista().read(lp.getIdLista());
            if (lista != null && lista.getIdUsuario() != usuarioLogado.getId()) {
                count++;
            }
        }

        return count;
    }
}
