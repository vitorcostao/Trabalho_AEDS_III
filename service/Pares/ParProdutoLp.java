package service.Pares;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParProdutoLp implements arvore.aed3.RegistroArvoreBMais<ParProdutoLp> {

  private int idLista;
  private int idListaProduto;
  private short TAMANHO = 8;

  public ParProdutoLp() {
    this(-1, -1);
  }

  public ParProdutoLp(int idProduto) {
    this(idProduto, -1);
  }

  public ParProdutoLp(int idProduto, int id) {
    try {
      this.idLista = idProduto; 
      this.idListaProduto = id; 
    } catch (Exception ec) {
    } 
  }

  public int getIdLista() {
    return idLista;
  }

  public int getIdListaProduto() {
    return idListaProduto;
  }

  @Override
  @SuppressWarnings("CloneDeclaresCloneNotSupported")
  public ParProdutoLp clone() {
    return new ParProdutoLp(this.idLista, this.idListaProduto);
  }

  @Override
  public short size() {
    return this.TAMANHO;
  }

  @Override
  public int compareTo(ParProdutoLp a) {
    if (this.idLista != a.idLista)
      return this.idLista - a.idLista;
    else
      return this.idListaProduto == -1 ? 0 : this.idListaProduto - a.idListaProduto;
  }

  @Override
  public String toString() {
    return String.format("%3d", this.idLista) + ";" + String.format("%-3d", this.idListaProduto);
  }

  @Override
  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeInt(this.idLista);
    dos.writeInt(this.idListaProduto);
    return baos.toByteArray();
  }

  @Override
  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.idLista = dis.readInt();
    this.idListaProduto = dis.readInt();
  }

}