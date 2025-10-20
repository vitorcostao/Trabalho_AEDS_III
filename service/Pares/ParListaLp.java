package service.Pares;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParListaLp implements arvore.aed3.RegistroArvoreBMais<ParListaLp> {

  private int idLista;
  private int idListaProduto;
  private short TAMANHO = 8;

  public ParListaLp() {
    this(-1, -1);
  }

  public ParListaLp(int idLista) {
    this(idLista, -1);
  }

  public ParListaLp(int idLista, int idListaProduto) {
    this.idLista = idLista;
    this.idListaProduto = idListaProduto;
  }

  // Métodos de acesso renomeados
  public int getIdLista() {
    return idLista;
  }

  public int getIdListaProduto() {
    return idListaProduto;
  }

  @Override
  @SuppressWarnings("CloneDeclaresCloneNotSupported")
  public ParListaLp clone() {
    return new ParListaLp(this.idLista, this.idListaProduto);
  }

  @Override
  public short size() {
    return this.TAMANHO;
  }

  @Override
  public int compareTo(ParListaLp a) {
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
  public int hashCode() {
    return Math.abs(Integer.hashCode(this.idLista));
  }

  public static int hash(int idLista) {
    return Math.abs(Integer.hashCode(idLista));
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    ParListaLp other = (ParListaLp) obj;
    return this.idLista == other.idLista && this.idListaProduto == other.idListaProduto;
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
