package model;

import interfaces.Entidade;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Lista implements Entidade {

    private int id;
    private int idUsuario;
    private String nome;
    private String descricao;
    private String dataCriacao;
    private String dataLimite;
    private String codigoCompartilhavel;

    public Lista() {
        this.id = -1;
        this.idUsuario = -1;
        this.nome = " ";
        this.descricao = " ";
        this.dataCriacao = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        this.dataLimite = "NaN";
        this.codigoCompartilhavel = " ";
    }

    public Lista(int id, int idUsuario, String nome, String descricao, String dataCriacao, String dataLimite,
            String codigoCompartilhavel) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataLimite = dataLimite;
        this.codigoCompartilhavel = codigoCompartilhavel;
    }

    public void print(){

        System.out.println("Codigo compartilhavel: "+codigoCompartilhavel);
        System.out.println("----------------------------------------->");
        System.out.println("NOME DA LISTA......:" +nome);
        System.out.println("DESCRIÇÃO..........:" +descricao);
        System.out.println("DATA DA CRIAÇÃO....:" +dataCriacao);
        if(dataCriacao.equalsIgnoreCase("NaN")){
            System.out.println("DATA LIMITE........: Não definida");
        }else{
            System.out.println("DATA LIMITE........: "+dataLimite);
        }
        System.out.println("----------------------------------------->");

    }


    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(String dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(String dataLimite) {
        this.dataLimite = dataLimite;
    }

    public String getCodigoCompartilhavel() {
        return codigoCompartilhavel;
    }

    public void setCodigoCompartilhavel(String codigoCompartilhavel) {
        this.codigoCompartilhavel = codigoCompartilhavel;
    }

    @Override
    public byte[] toByteArray() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(id);
        dos.writeInt(idUsuario);
        dos.writeUTF(nome);
        dos.writeUTF(descricao);
        dos.writeUTF(dataCriacao);
        dos.writeUTF(dataLimite);
        dos.writeUTF(codigoCompartilhavel);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        id = dis.readInt();
        idUsuario = dis.readInt();
        nome = dis.readUTF();
        descricao = dis.readUTF();
        dataCriacao = dis.readUTF();
        dataLimite = dis.readUTF();
        codigoCompartilhavel = dis.readUTF();
    }

    @Override
    public String toString() {
        return  "\nNome: " + nome +
                "\nDescrição: " + descricao +
                "\nData de Criação: " + dataCriacao +
                "\nData Limite: " + dataLimite +
                "\nCódigo Compartilhável: " + codigoCompartilhavel +
                "\n--------------------------------------------";
    }

}
