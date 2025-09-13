package service;

import model.Lista;

public class ArquivoLista extends service.Arquivo<Lista> {

    HashExtensivel<ParCodigoId> indiceIndireto;

    public ArquivoLista() throws Exception {
        super("listas", Lista.class.getConstructor());
        indiceIndireto = new HashExtensivel<>(
            ParCodigoId.class.getConstructor(), 
            4, 
            ".\\dados\\listas\\indiceLista.d.db",   // diretório
            ".\\dados\\listas\\indiceLista.c.db"    // cestos 
        );
    }

    @Override
    public int create(Lista l) throws Exception {
        int id = super.create(l);
        indiceIndireto.create(new ParCodigoId(l.getCodigoCompartilhavel(), id));
        return id;
    }

    public Lista read(String codigoCompartilhavel) throws Exception {
    	ParCodigoId pei = indiceIndireto.read(ParCodigoId.hash(codigoCompartilhavel));
        if(pei == null)
            return null;
        return read(pei.getId());
    }
    
    public boolean delete(String codigoCompartilhavel) throws Exception {
    	ParCodigoId pei = indiceIndireto.read(ParCodigoId.hash(codigoCompartilhavel));
        if(pei != null) 
            if(delete(pei.getId())) 
                return indiceIndireto.delete(ParCodigoId.hash(codigoCompartilhavel));
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Lista l = super.read(id);
        if(l != null) {
            if(super.delete(id))
                return indiceIndireto.delete(ParCodigoId.hash(l.getNome()));
        }
        return false;
    }

    public boolean update(Lista novaLista, String codigoAntigo) throws Exception {
    	
        Lista listaVelha = read(codigoAntigo);
        if(listaVelha == null){

            return false;
        }

        if(super.update(novaLista)){
            if (!novaLista.getCodigoCompartilhavel().equals(listaVelha.getCodigoCompartilhavel())) {
                indiceIndireto.delete(ParCodigoId.hash(codigoAntigo));
                indiceIndireto.create(new ParCodigoId(novaLista.getCodigoCompartilhavel(), novaLista.getId()));
            }
            return true;
        }
        return false;
    }
}