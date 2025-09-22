package controle;

import java.util.Scanner;
import model.Usuario;
import service.ArquivoUsuario;

public class ControleUsuario {

    private static ArquivoUsuario arquivoUsuario;
    private static ControleLista controleLista;
    private static Usuario usuarioLogado;

    public ControleUsuario() throws Exception {
        arquivoUsuario = new ArquivoUsuario();
        usuarioLogado = new Usuario();
        controleLista = new ControleLista(usuarioLogado);
    }

    public Usuario getUser() {

        return usuarioLogado;
    }

    public ControleLista getControl() {

        return controleLista;
    }

    /*-+-+-+-+- Cadastrar Usuario -+-+-+-+- */
    public Usuario cadastrarUsuario(String nome, String email, String senha, String pergunta, String resposta)
            throws Exception {
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

    public boolean fazerLogin(String email, String senha) throws Exception {

        boolean resp = false;

        usuarioLogado = buscarPorEmail(email);
        controleLista.setUser(usuarioLogado);

        if (usuarioLogado != null) {
            if (usuarioLogado.getHashSenha() == senha.hashCode()) {

                resp = true;
            } else {
                System.out.println("\nFalha no login! Email ou senha incorretos.");
            }
        } else {

            System.out.println("\nFalha no login! Email ou senha incorretos.");
        }

        return resp;
    }

    public void exibirDados() {

        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println(">Início >Meus dados\n");
        System.out.println("ID.................:" + usuarioLogado.getId());
        System.out.println("NOME...............: " + usuarioLogado.getNome());
        System.out.println("EMAIL..............: " + usuarioLogado.getEmail());
        System.out.println("PERGUNTA SECRETA...: " + usuarioLogado.getPerguntaSecreta());
    }

    public boolean alterarMeusDados(Scanner sc) throws Exception {

        boolean resp = false;
        String emailAntigo = usuarioLogado.getEmail();

        // Nome
        System.out.print("Novo nome (deixe em branco para manter): ");
        String nome = sc.nextLine();
        if (!nome.isEmpty())
            usuarioLogado.setNome(nome);

        // Email
        System.out.print("Novo email (deixe em branco para manter): ");
        String email = sc.nextLine();
        if (!email.isEmpty())
            usuarioLogado.setEmail(email);

        // Senha
        System.out.print("Nova senha (deixe em branco para manter): ");
        String senha = sc.nextLine();
        if (!senha.isEmpty())
            usuarioLogado.setHashSenha(senha.hashCode());

        // Pergunta secreta
        System.out.print("Nova pergunta secreta (deixe em branco para manter): ");
        String pergunta = sc.nextLine();
        if (!pergunta.isEmpty())
            usuarioLogado.setPerguntaSecreta(pergunta);

        // Resposta secreta
        System.out.print("Nova resposta secreta (deixe em branco para manter): ");
        String resposta = sc.nextLine();
        if (!resposta.isEmpty())
            usuarioLogado.setRespostaSecreta(resposta);

        if (arquivoUsuario.update(emailAntigo, usuarioLogado)) {

            resp = true;
        }

        return resp;
    }

    public boolean excluirUsuario(boolean executando) throws Exception {

        boolean sucesso = removerUsuarioPorId(usuarioLogado.getId());

        if (sucesso) {

            executando = false;
        } else {
            System.out.println("Erro ao excluir. Tente novamente.");
        }

        return executando;
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
     * // Método main para testes rápidos
     * public static void main(String[] args) {
     * 
     * Scanner sc = new Scanner(System.in);
     * System.out.println("Presente Fácil 1.0\n-----------------\n>Cadastro\n");
     * System.out.println("Digite o nome: ");
     * String nome = sc.nextLine();
     * System.out.println("Digite o email: ");
     * String email = sc.nextLine();
     * System.out.println("Digite a senha: ");
     * String senha = sc.nextLine();
     * System.out.println("Digite a pergunta de segurança: ");
     * String perg = sc.nextLine();
     * System.out.println("Digite a resposta de segurança: ");
     * String resp = sc.nextLine();
     * 
     * try {
     * ControleUsuario controleUsuario = new ControleUsuario();
     * Usuario novoUsuario = controleUsuario.cadastrarUsuario(nome, email, senha,
     * perg, resp);
     * System.out.println(novoUsuario.toString());
     * controleUsuario.fechar();
     * } catch (Exception e) {
     * System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
     * }
     * sc.close();
     * }
     */
}
