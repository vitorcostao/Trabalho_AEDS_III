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
    private int id;
    private String nome;
    private String email;
    private int hashSenha;
    private String perguntaSecreta;
    private String respostaSecreta;

    public Usuario() {

        this.id = -1;
        this.nome = " ";
        this.email = " ";
        this.hashSenha = -1;
        this.perguntaSecreta = " ";
        this.respostaSecreta = " ";
    }

    public Usuario(int id, String nome, String email, int hashSenha, String perguntaSecreta, String respostaSecreta){
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.hashSenha = hashSenha;
        this.perguntaSecreta = perguntaSecreta;
        this.respostaSecreta = respostaSecreta;
    }

    public String getEmail() {

        return this.email;
    }

    
    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {

        this.email = email;
    }

     public int getHashSenha() {
        return this.hashSenha;
    }

    public void setHashSenha(int hashSenha) {
        this.hashSenha = hashSenha;
    }

    public String getPerguntaSecreta() {
        return this.perguntaSecreta;
    }

    public void setPerguntaSecreta(String perguntaSecreta) {
        this.perguntaSecreta = perguntaSecreta;
    }

    public String getRespostaSecreta() {
        return this.respostaSecreta;
    }

    public void setRespostaSecreta(String respostaSecreta) {
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