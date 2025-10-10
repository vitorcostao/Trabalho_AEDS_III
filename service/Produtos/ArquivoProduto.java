package service.Produtos;

import java.util.ArrayList;
import model.Produto;
import service.Genérico.HashExtensivel;
import service.Pares.ParCodigoId;

public class ArquivoProduto extends service.Genérico.Arquivo<Produto> {

    HashExtensivel<ParCodigoId> indiceIndireto;

    public ArquivoProduto() throws Exception {
        super("produtos", Produto.class.getConstructor());
        indiceIndireto = new HashExtensivel<>(
                ParCodigoId.class.getConstructor(),
                4,
                ".\\dados\\produtos\\indiceProdutos.d.db", // diretório
                ".\\dados\\produtos\\indiceProdutos.c.db" // cestos
        );
    }

    @Override
    public int create(Produto p) throws Exception {
        int id = super.create(p);
        indiceIndireto.create(new ParCodigoId(p.getGTIN(), id));
        return id;
    }

    public Produto read(String GTIN) throws Exception {
        ParCodigoId pcd = indiceIndireto.read(ParCodigoId.hash(GTIN));
        if (pcd == null)
            return null;

        return super.read(pcd.getId());
    }

    public boolean delete(String GTIN) throws Exception {
        ParCodigoId pcd = indiceIndireto.read(ParCodigoId.hash(GTIN));
        if (pcd != null)
            if (delete(pcd.getId()))
                return indiceIndireto.delete(ParCodigoId.hash(GTIN));
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Produto p = super.read(id);
        if (p != null) {
            if (super.delete(id))
                return indiceIndireto.delete(ParCodigoId.hash(p.getGTIN()));
        }
        return false;
    }

    public boolean update(Produto novoProduto, String codigoAntigo) throws Exception {

        Produto produtoVelhor = read(codigoAntigo);
        if (produtoVelhor == null) {

            return false;
        }

        if (super.update(novoProduto)) {
            if (!novoProduto.getGTIN().equals(produtoVelhor.getGTIN())) {
                indiceIndireto.delete(ParCodigoId.hash(codigoAntigo));
                indiceIndireto.create(new ParCodigoId(novoProduto.getGTIN(), novoProduto.getId()));
            }
            return true;
        }
        return false;
    }

    @Override
    public ArrayList<Produto> readAll() throws Exception {

        ArrayList<Produto> todos = super.readAll();
        ArrayList<Produto> produtos = new ArrayList<>();

        for (Produto p : todos) {
            if (p != null && p.getId() != -1) {
                produtos.add(p);
            }
        }
        return produtos;
    }

}