package service.Usuarios;

import model.Usuario;
import service.Genérico.Arquivo;
import service.Genérico.HashExtensivel;
import service.Pares.ParEmailID;

public class ArquivoUsuario extends service.Genérico.Arquivo<Usuario> {

    HashExtensivel<ParEmailID> indiceIndiretoEmail;

    public ArquivoUsuario() throws Exception {
        super("usuarios", Usuario.class.getConstructor());
        indiceIndiretoEmail = new HashExtensivel<>(
                ParEmailID.class.getConstructor(),
                4,
                ".\\dados\\usuarios\\indiceEmail.d.db", // diretório
                ".\\dados\\usuarios\\indiceEmail.c.db" // cestos
        );
    }

    @Override
    public int create(Usuario u) throws Exception {
        int id = super.create(u);
        indiceIndiretoEmail.create(new ParEmailID(u.getEmail(), id));
        return id;
    }

    public Usuario read(String email) throws Exception {
        ParEmailID pei = indiceIndiretoEmail.read(ParEmailID.hash(email));
        if (pei == null)
            return null;
        return read(pei.getId());
    }

    public boolean delete(String email) throws Exception {
        ParEmailID pei = indiceIndiretoEmail.read(ParEmailID.hash(email));
        if (pei != null)
            if (delete(pei.getId()))
                return indiceIndiretoEmail.delete(ParEmailID.hash(email));
        return false;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Usuario u = super.read(id);
        if (u != null) {
            if (super.delete(id))
                return indiceIndiretoEmail.delete(ParEmailID.hash(u.getEmail()));
        }
        return false;
    }

    public boolean update(String emailAntigo, Usuario novoUsuario) throws Exception {
     
        Usuario usuarioVelho = read(emailAntigo);
        if (usuarioVelho == null) {
            return false;
        } 
        
        if (super.update(novoUsuario)) {
            if (!novoUsuario.getEmail().equals(usuarioVelho.getEmail())) {
                indiceIndiretoEmail.delete(ParEmailID.hash(emailAntigo));
                indiceIndiretoEmail.create(new ParEmailID(novoUsuario.getEmail(), novoUsuario.getId()));
            }
            return true;
        }
        return false;
    }
}