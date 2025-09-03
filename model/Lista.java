package model;

import interfaces.Entidade;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe para representar listas de presentes
 */
public class Lista implements Entidade {

    public int id;
    public int idUsuario; // ID do usuário dono da lista
    public String nome;
    public String descricao;
    public String dataCriacao;  
    public String dataLimite;   
    public String codigoCompartilhavel;

    public Lista() {
        this.id = -1;
        this.idUsuario = -1;
        this.nome = " ";
        this.descricao = " ";
        this.dataCriacao = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        this.dataLimite = "NaN";
        this.codigoCompartilhavel = " ";
    }

    public Lista(int id, int idUsuario, String nome, String descricao, String dataCriacao, String dataLimite, String codigoCompartilhavel) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataLimite = dataLimite;
        this.codigoCompartilhavel = codigoCompartilhavel;
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
}
