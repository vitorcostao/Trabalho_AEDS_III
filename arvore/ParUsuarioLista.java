package arvore;

import interfaces.RegistroHashExtensivel;
import java.io.*;

public class ParUsuarioLista implements RegistroHashExtensivel<ParUsuarioLista>, arvore.aed3.RegistroArvoreBMais<ParUsuarioLista>{

    private int idUsuario;   // chave
    private long enderecoLista; 
    private final short TAMANHO = 12; 

    public ParUsuarioLista() {
        this.idUsuario = -1;
        this.enderecoLista = -1;
    }

    public ParUsuarioLista(int idUsuario, long enderecoLista) {
        this.idUsuario = idUsuario;
        this.enderecoLista = enderecoLista;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public long getEnderecoLista() {
        return enderecoLista;
    }
    

    public static int hash(int idUsuario) {
        return Math.abs(idUsuario); 
    }

    @Override
    public int hashCode() {
        return this.idUsuario;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ParUsuarioLista other = (ParUsuarioLista) obj;
        return idUsuario == other.idUsuario && enderecoLista == other.enderecoLista;
    }

    @Override
    public short size() {
        return this.TAMANHO;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idUsuario);
        dos.writeLong(enderecoLista);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        this.idUsuario = dis.readInt();
        this.enderecoLista = dis.readLong();
    }

    @Override
    public String toString() {
        return "(" + idUsuario + ";" + enderecoLista + ")";
    }

    @Override
    public int compareTo(ParUsuarioLista obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }

    @Override
    public ParUsuarioLista clone() {
        return new ParUsuarioLista(this.idUsuario, this.enderecoLista);
    }

}
