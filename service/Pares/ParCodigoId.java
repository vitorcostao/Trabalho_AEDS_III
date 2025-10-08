package service.Pares;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ParCodigoId implements interfaces.RegistroHashExtensivel<ParCodigoId> {

  private String codigo;
  private int id;
  private short TAMANHO = 44;

  public ParCodigoId() {
    this("", -1);
  }

  @SuppressWarnings("CallToPrintStackTrace")
  public ParCodigoId(String codigo, int id) {
    try {
      this.codigo = codigo;
      this.id = id;
      if (codigo.getBytes().length + 4 > TAMANHO)
        throw new Exception("Número de caracteres do código maior que o permitido. Os dados serão cortados.");
    } catch (Exception ec) {
      ec.printStackTrace();
    }
  }

  public int getId() {
    return this.id;
  }

  public String getCodigo() {
    return this.codigo;
  }

  @Override
  public int hashCode() {
    return Math.abs(this.codigo.hashCode());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    ParCodigoId other = (ParCodigoId) obj;
    if (id != other.id)
      return false;
    if (codigo == null) {
      if (other.codigo != null)
        return false;
    } else if (!codigo.equals(other.codigo))
      return false;
    return true;
  }

  @Override
  public short size() {
    return this.TAMANHO;
  }

  @Override
  public String toString() {
    return this.codigo + ";" + this.id;
  }

  @Override
  public byte[] toByteArray() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    dos.writeUTF(codigo);
    dos.writeInt(id);
    byte[] bs = baos.toByteArray();
    byte[] bs2 = new byte[TAMANHO];
    for (int i = 0; i < TAMANHO; i++)
      bs2[i] = ' ';
    for (int i = 0; i < bs.length && i < TAMANHO; i++)
      bs2[i] = bs[i];
    return bs2;
  }

  @Override
  public void fromByteArray(byte[] ba) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(ba);
    DataInputStream dis = new DataInputStream(bais);
    this.codigo = dis.readUTF();
    this.id = dis.readInt();
  }

  public static int hash(String codigo) {
    return Math.abs(codigo.hashCode());
  }

}
