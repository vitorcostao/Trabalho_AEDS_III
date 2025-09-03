package service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import interfaces.RegistroHashExtensivel;

public class ParEmailEndereco implements RegistroHashExtensivel<ParEmailEndereco> {
    
	private String email;      // chave
	private long endereco;     // valor
	private final short TAMANHO = 58;  // 50 chars * 1 byte cada + 8 bytes para long


	public ParEmailEndereco() {
	    this.email = "";
	    this.endereco = -1;
	}

	public ParEmailEndereco(String email, long endereco) {
	    this.email = email;
	    this.endereco = endereco;
	}

	public String getEmail() {
	    return email;
	}

	public long getEndereco() {
	    return endereco;
	}

	public static int hash(String email) {
        return email.hashCode();
    }

	
    @Override
    public int hashCode() {
        return email.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ParEmailEndereco other = (ParEmailEndereco) obj;
        return email.equals(other.email) && endereco == other.endereco;
    }


    @Override
    public short size() {
        return this.TAMANHO;
    }

    @Override
    public String toString() {
        return "("+this.email + ";" + this.endereco+")";
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // escreve o email fixo em 50 bytes
        byte[] emailBytes = new byte[50];
        byte[] eBytes = email.getBytes("UTF-8");
        System.arraycopy(eBytes, 0, emailBytes, 0, Math.min(eBytes.length, 50));
        dos.write(emailBytes);

        dos.writeLong(endereco);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        byte[] emailBytes = new byte[50];
        dis.read(emailBytes);
        this.email = new String(emailBytes, "UTF-8").trim();

        this.endereco = dis.readLong();
    }

}
