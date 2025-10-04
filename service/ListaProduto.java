package service;

public class ListaProduto {

    //Atributos necessarios
    public int idProduto;
    public int idLista;
    public int quantidade;
    public String observacao;

    //Construtores (com e sem observação)
    public ListaProduto(){
        this.idProduto = -1;
        this.idLista = -1;
        this.quantidade = -1;
        this.observacao = "";
    }

    public ListaProduto(int idProduto, int idLista, int quantidade){
        this.idProduto = idProduto;
        this.idLista = idLista;
        this.quantidade = quantidade;
        this.observacao = "Sem observações";
    }

    public ListaProduto(int idProduto, int idLista, int quantidade, String observacao){
        this.idProduto = idProduto;
        this.idLista = idLista;
        this.quantidade = quantidade;
        this.observacao = observacao;
    }

    //Getters e Setters
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

}
