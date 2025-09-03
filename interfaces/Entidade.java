package interfaces;

public interface Entidade {
    
    public int getId();
    public void setId(int id);
    public byte[] toByteArray() throws Exception;
    public void fromByteArray(byte[] vb) throws Exception;
}
