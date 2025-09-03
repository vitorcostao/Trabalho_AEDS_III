package model;

import interfaces.Entidade;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Classe para implementar usuários
 */
public class Usuario implements Entidade {

    // Definir dados
    public int id;
    public String nome;
    public String email;
    public int hashSenha;
    public String perguntaSecreta;
    public String respostaSecreta;

    public Usuario() {

        this.id = -1;
        this.nome = " ";
        this.email = " ";
        this.hashSenha = -1;
        this.perguntaSecreta = " ";
        this.respostaSecreta = " ";
    }

    public Usuario(int id, String nome, String email, int hashSenha, String perguntaSecreta, String respostaSecreta) throws Exception {

        this.id = id;
        this.nome = nome;
        this.email = email;
        this.hashSenha = hashSenha;
        this.perguntaSecreta = perguntaSecreta;
        this.respostaSecreta = respostaSecreta;
    }

    @Override
    public int getId() {

        return this.id;
    }

    @Override
    public void setId(int id) {

        this.id = id;
    }

    @Override
    public byte[] toByteArray() throws Exception {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.email);
        dos.writeInt(this.hashSenha);
        dos.writeUTF(this.perguntaSecreta);
        dos.writeUTF(this.respostaSecreta);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] vb) throws Exception {
        
        ByteArrayInputStream bis = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bis);

        this.id = dis.readInt();
        this.nome = dis.readUTF();
        this.email = dis.readUTF();
        this.hashSenha = dis.readInt();
        this.perguntaSecreta = dis.readUTF();
        this.respostaSecreta = dis.readUTF();
    }

}