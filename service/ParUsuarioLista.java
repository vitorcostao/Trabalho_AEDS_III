
package service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParUsuarioLista implements interfaces.RegistroHashExtensivel<ParUsuarioLista> {

  private String nome;
  private int id;
  private short TAMANHO = 44;

  public ParUsuarioLista() {
    this("", -1);
  }

  public ParUsuarioLista(String n, int i) {
    try {
      this.nome = n;
      this.id = i;
      if (n.getBytes().length + 4 > TAMANHO)
        throw new Exception("Número de caracteres do nome maior que o permitido. Os dados serão cortados.");
    } catch (Exception ec) {
      ec.printStackTrace();
    }
  }

  public int getId() {
	  
	  return this.id;
  }
  
  
  @Override
  public int hashCode() {
    return Math.abs(this.nome.hashCode());
  }

  public short size() {
    return this.TAMANHO;
  }

  public String toString() {
    return this.nome + ";" + this.id;
  }

  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeUTF(nome);
    dos.writeInt(id);
    byte[] bs = baos.toByteArray();
    byte[] bs2 = new byte[TAMANHO];
    for (int i = 0; i < TAMANHO; i++)
      bs2[i] = ' ';
    for (int i = 0; i < bs.length && i < TAMANHO; i++)
      bs2[i] = bs[i];
    return bs2;
  }

  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.nome = dis.readUTF();
    this.id = dis.readInt();
  }

  public static int hash(String nome) {
    return Math.abs(nome.hashCode());
  }

}