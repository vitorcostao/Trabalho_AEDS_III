package controle;

import model.Usuario;
import service.ArquivoUsuario;

public class ControleUsuario {

    private final ArquivoUsuario arquivoUsuario;
    private final ControleLista controleLista;
    private final Usuario usuarioLogado;
    

    public ControleUsuario() throws Exception {
        arquivoUsuario = new ArquivoUsuario();
        usuarioLogado = new Usuario();
        controleLista = new ControleLista(usuarioLogado);
    }

    public Usuario getUser(){

        return usuarioLogado;
    }

    public ControleLista getControl(){

        return controleLista;
    }

    public void setUser(int id, String nome, String email, int senha, String pergunta, String resposta){

        this.usuarioLogado.setId(id);
        this.usuarioLogado.setNome(nome);
        this.usuarioLogado.setEmail(email);
        this.usuarioLogado.setHashSenha(senha);
        this.usuarioLogado.setPerguntaSecreta(pergunta);
        this.usuarioLogado.setRespostaSecreta(resposta);
    }

    /*-+-+-+-+- Cadastrar Usuario -+-+-+-+- */
    public Usuario cadastrarUsuario(String nome, String email, String senha, String pergunta, String resposta) throws Exception {
        if (arquivoUsuario.read(email) != null) {
            throw new IllegalArgumentException("Email já cadastrado!");
        }
        Usuario novo = new Usuario();
        novo.setNome(nome);
        novo.setEmail(email);
        novo.setSenha(senha);  
        novo.setPerguntaSecreta(pergunta);
        novo.setRespostaSecreta(resposta);

        int id = arquivoUsuario.create(novo);
        novo.setId(id);

        return novo;
    }


    // Busca usuário pelo email
    public Usuario buscarPorEmail(String email) throws Exception {
        return arquivoUsuario.read(email);
    }

    // Busca usuário pelo ID
    public Usuario buscarPorId(int id) throws Exception {
        return arquivoUsuario.read(id);
    }

    // Atualiza usuário
    public boolean atualizarUsuario(Usuario usuarioAtualizado, String emailAntigo) throws Exception {

        return arquivoUsuario.update(emailAntigo, usuarioAtualizado);
    }

    // Remove usuário por email
    public boolean removerUsuarioPorEmail(String email) throws Exception {
        return arquivoUsuario.delete(email);
    }

    // Remove usuário por ID
    public boolean removerUsuarioPorId(int id) throws Exception {
        return arquivoUsuario.delete(id);
    }

    public void fechar() throws Exception {
        arquivoUsuario.close();
    }

    /* 
    // Método main para testes rápidos
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Presente Fácil 1.0\n-----------------\n>Cadastro\n");
        System.out.println("Digite o nome: ");
	    String nome = sc.nextLine();
	    System.out.println("Digite o email: ");
	    String email = sc.nextLine();
	    System.out.println("Digite a senha: ");
	    String senha = sc.nextLine();
	    System.out.println("Digite a pergunta de segurança: ");
	    String perg = sc.nextLine();
	    System.out.println("Digite a resposta de segurança: ");
	    String resp = sc.nextLine();
       
        try {
            ControleUsuario controleUsuario = new ControleUsuario();
            Usuario novoUsuario = controleUsuario.cadastrarUsuario(nome, email, senha, perg, resp);
            System.out.println(novoUsuario.toString());
            controleUsuario.fechar();
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
        sc.close();
    }
    */
}
