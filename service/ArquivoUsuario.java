package service;

import model.Usuario;

public class ArquivoUsuario extends service.Arquivo<Usuario>{
	
	public Arquivo<Usuario> arqUsuario;
	public HashExtensivel<ParEmailEndereco> indiceIndireto;
	
	public ArquivoUsuario() throws Exception {
		
		super("usuarios", Usuario.class.getConstructor());
		indiceIndireto = new HashExtensivel<>(ParEmailEndereco.class.getConstructor(), 4, 
			".\\dados\\usuarios\\indiceEmail.d.db",   // diretório
	        ".\\dados\\usuarios\\indiceEmail.c.db"    // cestos 
	    );
	}
	
	public int create(Usuario u) throws Exception {
		
		int id = super.create(u);
		indiceIndireto.create(new ParEmailEndereco(u.email, id));
		return id;
	}
	
	public Usuario read(String email) throws Exception {
		
		ParEmailEndereco pie = indiceIndireto.read(email.hashCode());
        if(pie == null)
            return null;
        return super.read((int)pie.getEndereco());
	}
	
	public boolean delete(String email) throws Exception {
        ParEmailEndereco pie = indiceIndireto.read(ParEmailEndereco.hash(email));
        if(pie != null) 
            if(delete(pie.getEmail())) 
                return indiceIndireto.delete(ParEmailEndereco.hash(email));
        return false;
    }
	
	@Override
    public boolean delete(int id) throws Exception {
        Usuario u = super.read(id);
        if(u != null) {
            if(super.delete(id))
                return indiceIndireto.delete(u.email.hashCode());
        }
        return false;
    }
}
