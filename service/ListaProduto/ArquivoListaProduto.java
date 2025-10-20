package service.ListaProduto;

import arvore.aed3.ArvoreBMais;
import java.util.ArrayList;
import model.ListaProduto;
import service.Pares.ParListaLp;
import service.Pares.ParProdutoLp;

public class ArquivoListaProduto extends service.Genérico.Arquivo<ListaProduto> {

    ArvoreBMais<ParListaLp> treeListaListaProduto;
    ArvoreBMais<ParProdutoLp> treeProdutoListaProduto;

    public ArquivoListaProduto() throws Exception {
        super("listasProdutos", ListaProduto.class.getConstructor());
        treeListaListaProduto = new ArvoreBMais<>(ParListaLp.class.getConstructor(), 5, "arvoreListaListaProdutos.db");
        treeProdutoListaProduto = new ArvoreBMais<>(ParProdutoLp.class.getConstructor(), 5,
                "arvoreProdutoListaProdutos.db");
    }

    @Override    
    public int create(ListaProduto lp) throws Exception {
        int id = super.create(lp);
        treeListaListaProduto.create(new ParListaLp(lp.getIdLista(), id));
        treeProdutoListaProduto.create(new ParProdutoLp(lp.getIdProduto(), id));
        return id;
    }

    @Override  
    public ListaProduto read(int id) throws Exception {
        return super.read(id);
    }

    public ArrayList<ListaProduto> readByLista(int idLista) throws Exception {

        ArrayList<ListaProduto> resultado = new ArrayList<>();
        ArrayList<ParListaLp> pares = treeListaListaProduto.read(new ParListaLp(idLista));

        for (ParListaLp par : pares) {

            ListaProduto lp = super.read(par.getIdListaProduto());

            System.out.println(lp.getIdLista());

            if (lp != null && lp.getId() != -1) {

                resultado.add(lp);
            }
        }

        return resultado;
    }

    public ArrayList<ListaProduto> readByProduto(int idProduto) throws Exception {

        ArrayList<ListaProduto> resultado = new ArrayList<>();
        ArrayList<ParProdutoLp> pares = treeProdutoListaProduto.read(new ParProdutoLp(idProduto));

        for (ParProdutoLp par : pares) {
            ListaProduto lp = super.read(par.getIdListaProduto());
            if (lp != null && lp.getId() != -1) {
                resultado.add(lp);
            }
        }

        return resultado;
    }


    @Override
    public boolean delete(int id) throws Exception {
        
        ListaProduto lp = super.read(id);
        if (lp == null) {
            return false;
        }

        if (!super.delete(id)) {
            return false; 
        }

        treeListaListaProduto.delete(new ParListaLp(lp.getIdLista(), id));
        treeProdutoListaProduto.delete(new ParProdutoLp(lp.getIdProduto(), id));

        return true;
    }

    @Override
    public boolean update(ListaProduto novaListaProduto) throws Exception {
   
        ListaProduto listaProdutoVelha = super.read(novaListaProduto.getId());
        if (listaProdutoVelha == null) {
            return false;    
        }

        if (!super.update(novaListaProduto)) {
            return false;
        }

        if (novaListaProduto.getIdLista() != listaProdutoVelha.getIdLista()) {
            treeListaListaProduto.delete(new ParListaLp(listaProdutoVelha.getIdLista(), listaProdutoVelha.getId()));
            treeListaListaProduto.create(new ParListaLp(novaListaProduto.getIdLista(), novaListaProduto.getId()));
        }

        if (novaListaProduto.getIdProduto() != listaProdutoVelha.getIdProduto()) {
            treeProdutoListaProduto.delete(new ParProdutoLp(listaProdutoVelha.getIdProduto(), listaProdutoVelha.getId()));
            treeProdutoListaProduto.create(new ParProdutoLp(novaListaProduto.getIdProduto(), novaListaProduto.getId()));
        }

        return true;
    }

}