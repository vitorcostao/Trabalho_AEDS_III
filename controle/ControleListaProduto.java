package controle;

import java.util.ArrayList;

import model.Lista;
import model.ListaProduto;
import model.Produto;
import model.Usuario;
import service.ListaProduto.ArquivoListaProduto;
import service.Listas.ArquivoLista;
import service.Pares.ParProdutoLp;
import service.Produtos.ArquivoProduto;

public class ControleListaProduto {

    private static ArquivoLista arquivoLista;
    private static ArquivoProduto arquivoProduto;
    private static ArquivoListaProduto arquivoListaProduto;
    private static Usuario usuarioLogado;

    public ControleListaProduto(Usuario usuarioLogado) throws Exception {

        this.arquivoLista = new ArquivoLista();
        this.arquivoProduto = new ArquivoProduto();
        this.arquivoListaProduto = new ArquivoListaProduto();
        this.usuarioLogado = usuarioLogado;
    }

    public boolean adicionarProdutoNaLista(int idProduto, Lista listaAtual) throws Exception {
        if (listaAtual == null) {
            System.out.println("Lista inválida.");
            return false;
        }

        // Verifica se o produto já está na lista
        ArrayList<ListaProduto> relacoes = arquivoListaProduto.readByLista(listaAtual.getId());
        for (ListaProduto lp : relacoes) {
            if (lp.getIdProduto() == idProduto) {
                return false;
            }
        }

        // Se não estiver, cria a associação normalmente
        ListaProduto novaRelacao = new ListaProduto(listaAtual.getId(), idProduto, 0);
        arquivoListaProduto.create(novaRelacao);
        return true;
    }

    public ArrayList<Produto> listarProdutosDaLista(Lista listaAtual) throws Exception {
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

    public void exibirProdutosDaLista(Lista listaAtual) throws Exception {
        System.out.println("Presente Fácil 1.0\n----------------------------------------->");
        System.out.println("> Início > Listas > " + listaAtual.getNome() + " > Produtos\n");

        ArrayList<Produto> produtos = listarProdutosDaLista(listaAtual);
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
