package service;

import model.*;
import java.util.ArrayList;

public class ArquivoLista extends service.Arquivo<Lista>{
	
	Arquivo<Lista> arqLista;
	HashExtensivel<ParUsuarioLista> indiceIndireto;
	
	public ArquivoLista() throws Exception {
		
		super("listas", Lista.class.getConstructor());
		indiceIndireto = new HashExtensivel<>(ParUsuarioLista.class.getConstructor(), 4, 
			".\\dados\\listas\\indiceLista.d.db",   // diretório
	        ".\\dados\\listas\\indiceLista.c.db"    // cestos 
	    );
	}
	
    public int create(Lista l, Usuario usuarioLogado) throws Exception {
        l.idUsuario = usuarioLogado.getId(); 
        int id = super.create(l);
        indiceIndireto.create(new ParUsuarioLista(l.id, usuarioLogado.getId()));
        return id;
    }
	

    public ArrayList<Lista> readAll(int idUsuario) throws Exception {

    	ArrayList<Lista> listas = new ArrayList<>();
        for (long i = 0; i < super.arquivo.length(); ) {
            super.arquivo.seek(i);

            byte lapide = super.arquivo.readByte();
            short tam = super.arquivo.readShort();
            byte[] ba = new byte[tam];
            super.arquivo.read(ba);

            if (lapide == ' ') {
                Lista l = new Lista();
                l.fromByteArray(ba);
                if (l.idUsuario == idUsuario)
                    listas.add(l);
            }

            i += tam + 3; 
        }
        return listas;
    }
	
    public boolean delete(int idUsuario, int idLista) throws Exception {

        ParUsuarioLista chave = new ParUsuarioLista(idUsuario, idLista);
        ParUsuarioLista pul = indiceIndireto.read(chave.hashCode());

        if (pul != null && pul.getEnderecoLista() == idLista) {
            if (super.delete((int) pul.getEnderecoLista())) {
                return indiceIndireto.delete(chave.hashCode());
            }
        }

        return false;
    }

}
