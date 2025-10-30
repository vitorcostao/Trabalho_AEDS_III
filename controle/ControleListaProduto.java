package controle;

import java.util.ArrayList;
import model.Lista;
import model.ListaProduto;
import model.Produto;
import model.Usuario;
import service.ListaProduto.ArquivoListaProduto;
import service.Listas.ArquivoLista;
import service.Produtos.ArquivoProduto;

public class ControleListaProduto {

    private static ArquivoLista arquivoLista;
    private static ArquivoProduto arquivoProduto;
    private static ArquivoListaProduto arquivoListaProduto;
    private static Usuario usuarioLogado;

    public ControleListaProduto(Usuario usuarioLogado) throws Exception {

        arquivoLista = new ArquivoLista();
        arquivoProduto = new ArquivoProduto();
        arquivoListaProduto = new ArquivoListaProduto();
        this.usuarioLogado = usuarioLogado;
    }

    public boolean adicionarProdutoNaLista(int idProduto, Lista listaAtual) throws Exception {

        if (listaAtual == null) {
            return false;
        }

        ArrayList<ListaProduto> relacoes = arquivoListaProduto.readByLista(listaAtual.getId());
        for (ListaProduto lp : relacoes) {
            if (lp.getIdProduto() == idProduto) {
                return false;
            }
        }

        ListaProduto novaRelacao = new ListaProduto(idProduto, listaAtual.getId(), 1);
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

            if (p != null && p.getStatus())
                produtos.add(p);
        }
        return produtos;
    }

    public static ArquivoLista getArquivoLista() {
        return arquivoLista;
    }

    public static ArquivoListaProduto getArquivoListaProduto() {
        return arquivoListaProduto;
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

            if (p.getStatus()) {

                System.out.println("ID: " + p.getId() + " | Nome: " + p.getNome() +
                        " | GTIN: " + p.getGTIN() +
                        " | Descrição: " + p.getDescricao());
            }
        }
    }

    public boolean removerProdutoDaLista(int idProduto, int idLista) throws Exception {
        ArrayList<ListaProduto> relacoes = arquivoListaProduto.readByLista(idLista);
        for (ListaProduto lp : relacoes) {
            if (lp.getIdProduto() == idProduto) {
                return arquivoListaProduto.delete(lp.getId());
            }
        }
        return false;
    }

    public int contarListasDeOutrosUsuariosQueContemProduto(int idProduto) throws Exception {
        int count = 0;
        ArrayList<ListaProduto> relacoes = arquivoListaProduto.readByProduto(idProduto);
        Produto p = arquivoProduto.read(idProduto);

        if (p.getStatus()) {
           
            for (ListaProduto lp : relacoes) {
                Lista lista = new ArquivoLista().read(lp.getIdLista());
                if (lista != null && lista.getIdUsuario() != usuarioLogado.getId()) {
                    count++;
                }
            }
        }

        return count;
    }

    public ArrayList<Lista> findListFromProducts(int idProduto) throws Exception {

        ArrayList<ListaProduto> idLista = arquivoListaProduto.readByProduto(idProduto);
        ArrayList<Lista> result = new ArrayList<>();
        Produto p = arquivoProduto.read(idProduto);

        if (p.getStatus()) {

            for (ListaProduto lp : idLista) {

                result.add(arquivoLista.read(lp.getIdLista()));
            }
        }

        return result;
    }

}
