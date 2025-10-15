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
    private String GTIN;
    private String nome;
    private String descricao;
    private Byte status; // 1 para item ativo e 0 para item inativo
    private int quantidade;
    private String observacoes;

    // Construtor vazio
    public Produto() {
        this.id = -1;
        this.nome = " ";
        this.GTIN = " ";
        this.descricao = " ";
        this.status = 1;//item ativo
        this.quantidade = 0;
        this.observacoes = " ";
    }

    // Construtor com id
    public Produto(int id, String nome, String GTIN, String descricao, int quantidade, String observacoes) {
        this.id = id;
        this.nome = nome;
        this.GTIN = GTIN;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.observacoes = observacoes;
        this.status = 1;
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

    public byte getStatus(){

        return this.status;
    }

    public void Desativar(){

        this.status = 0;
    }

    public void Reativar(){

        this.status = 1;
    }
    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidade(){
        return this.quantidade;
    }

    public void setQuantidade(int quantidade){
        this.quantidade = quantidade;
    }
    public String getObservacoes(){
        return this.observacoes;
    }

    public void setObservacoes(String observacoes){
        this.observacoes = observacoes;
    }



    @Override
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
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
        this.GTIN = dis.readUTF();
        this.nome = dis.readUTF();
        this.descricao = dis.readUTF();
    }

    @Override
    public String toString() {
        return "ID: " + this.id + ", GTIN:" + this.GTIN + ", Nome: " + this.nome + ", Descricao:" + this.descricao;
    }
}
