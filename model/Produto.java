package model;

import interfaces.Entidade;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Classe para implementar usuários
 */
public class Produto implements Entidade {

    // Definir dados
    private int id;
    private Boolean status; //true = ativo, false = inativo
    private String GTIN;
    private String nome;
    private String descricao;

    // Construtor vazio
    public Produto() {
        this.id = -1;
        this.nome = " ";
        this.GTIN = " ";
        this.descricao = " ";
        this.status = true;//item ativo
    }

    // Construtor com id
    public Produto(int id, String nome, String GTIN, String descricao) {
        this.id = id;
        this.nome = nome;
        this.GTIN = GTIN;
        this.descricao = descricao;
        this.status = true;
    }

    // Getters e Setters
    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGTIN(){

        return this.GTIN;
    }

    public void setGTIN(String GTIN){

        this.GTIN = GTIN;
    }

    public String getDescricao(){

        return this.descricao;
    }

    public void setDescricao(String descricao){

        this.descricao = descricao;
    }

    public boolean getStatus(){

        return this.status;
    }

    public void Desativar(){

        this.status = false;
    }

    public void Reativar(){

        this.status = true;
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
        dos.writeBoolean(this.status);
        dos.writeUTF(this.GTIN);
        dos.writeUTF(this.nome);
        dos.writeUTF(this.descricao);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] vb) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bis);

        this.id = dis.readInt();
        this.status = dis.readBoolean();
        this.GTIN = dis.readUTF();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
    }

    @Override
    public String toString() {
        return "ID: " + this.id + ", GTIN:" + this.GTIN + ", Nome: " + this.nome + ", Descricao:" + this.descricao;
    }
}
