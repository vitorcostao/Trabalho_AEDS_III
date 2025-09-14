package controle;

import arvore.ParIntInt;
import arvore.aed3.ArvoreBMais;
import model.Lista;
import model.Usuario;
import service.ArquivoLista;

public class ControleLista {

    private static ArquivoLista arquivoLista;
    public static ArvoreBMais<ParIntInt> tree;
    public static Usuario usuarioLogado;
    
    public ControleLista() throws Exception {
        arquivoLista = new ArquivoLista();
        tree = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 5, "arvoreBmais.db");
    }

    public ControleLista(Usuario usuarioLogado) throws Exception {
        arquivoLista = new ArquivoLista();
        ControleLista.usuarioLogado = usuarioLogado;
        tree = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 5, "arvoreBmais.db");
    }

    @SuppressWarnings("static-access")
    public ArquivoLista getArquivoLista(){

        return this.arquivoLista;
    }

    @SuppressWarnings("rawtypes")
    public ArvoreBMais getArvoreBMais(){

        return tree;
    }

    /*-+-+-+-+- Cadastrar Lista -+-+-+-+- */
    public Lista cadastrarLista(int idUsuario, String nome, String descricao, String dataCriacao, String dataLimite, String codigo) throws Exception {
        if (arquivoLista.read(codigo) != null) {
            throw new IllegalArgumentException("Email já cadastrado!");
        }
        Lista novo = new Lista(0, idUsuario, nome, descricao, dataCriacao, dataLimite, codigo);

        int id = arquivoLista.create(novo);
        novo.setId(id);
        tree.create(new ParIntInt(idUsuario, id));

    
        tree.print();

        return novo;
    }


    // Busca usuário pelo email
    public Lista buscarPorCodigo(String codigo) throws Exception {
        return arquivoLista.read(codigo);
    }

    // Busca usuário pelo ID
    public Lista buscarPorId(int id) throws Exception {
        return arquivoLista.read(id);
    }

    // Atualiza usuário
    public boolean atualizarUsuario(Lista listaAtualizada, String emailAntigo) throws Exception {

        return arquivoLista.update(listaAtualizada, emailAntigo);
    }

    // Remove usuário por email
    public boolean removerUsuarioPorEmail(String email) throws Exception {
        return arquivoLista.delete(email);
    }

    // Remove usuário por ID
    public boolean removerUsuarioPorId(int id) throws Exception {
        return arquivoLista.delete(id);
    }

    public void fechar() throws Exception {
        arquivoLista.close();
    }

    /* 
    // Método main para testes rápidos
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Presente Fácil 1.0\n-----------------\n>Cadastro\n");
        System.out.println("Digite o nome: ");
	    String nome = sc.nextLine();
	    System.out.println("Digite a descricao: ");
	    String descricao = sc.nextLine();
	    
        LocalDate hoje = LocalDate.now();
	    String dataCriacao = hoje.toString();


	    System.out.println("Digite a data limite(dd/MM/yyyy): ");
	    String dataLimite = sc.nextLine();

	    System.out.println("Codigo compartilhavel(Ja sera implementado)");
	    String codigo = sc.nextLine();
       
        try {
            ControleLista controleLista = new ControleLista();
            Lista novaLista = controleLista.cadastrarLista(usuarioLogado.getId(), nome, descricao, dataCriacao, dataLimite, codigo);
            System.out.println(novaLista.toString());
            controleLista.fechar();
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
        sc.close();
    }
    */
}
