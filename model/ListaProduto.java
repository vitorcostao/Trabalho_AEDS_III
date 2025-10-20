package model;

import interfaces.Entidade;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ListaProduto implements Entidade {

    // Atributos necessarios
    private int id;
    private int idProduto;
    private int idLista;
    private int quantidade;
    private String observacao;

    // Construtores (com e sem observação)
    public ListaProduto() {
        this.id = -1;
        this.idProduto = -1;
        this.idLista = -1;
        this.quantidade = -1;
        this.observacao = "";
    }

    public ListaProduto(int idProduto, int idLista, int quantidade) {
        this.idProduto = idProduto;
        this.idLista = idLista;
        this.quantidade = quantidade;
        this.observacao = "Sem observações";
    }

    public ListaProduto(int idProduto, int idLista, int quantidade, String observacao) {
        this.idProduto = idProduto;
        this.idLista = idLista;
        this.quantidade = quantidade;
        this.observacao = observacao;
    }

    // Getters e Setters
    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getIdLista() {
        return idLista;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public String getObservacao() {
        return observacao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(this.id);
        dos.writeInt(this.idProduto);
        dos.writeInt(this.idLista);
        dos.writeInt(this.quantidade);
        dos.writeUTF(this.observacao);

        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] vb) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(vb);
        DataInputStream dis = new DataInputStream(bis);

        this.id = dis.readInt();
        this.idProduto = dis.readInt();
        this.idLista = dis.readInt();
        this.quantidade = dis.readInt();
        this.observacao = dis.readUTF();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ListaProduto other = (ListaProduto) obj;
        return this.idLista == other.idLista && this.idProduto == other.idProduto;
    }
}
