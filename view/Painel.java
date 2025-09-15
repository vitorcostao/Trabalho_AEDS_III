package view;

import controle.*;
import java.io.IOException;
import java.util.*;
import model.Lista;
import model.Usuario;

public class Painel {

    private static boolean executando = true;
    public static ControleUsuario controleUsuario = null;

    /*-+-+-+-+- Funões de Auxilio de Painel -+-+-+-+- */
    public static void limparTelaWindows() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (IOException | InterruptedException e) {
            for (int i = 0; i < 50; i++)
                System.out.println(); // Simula limpar tela
        }
    }

    public static void pausar(Scanner sc) throws Exception {
        System.out.println("\nPressione ENTER para continuar...");
        sc.nextLine();
    }
    /*-+-+-+-+- ________________________ -+-+-+-+- */

    /*-+-+-+-+- Funões de Painel -+-+-+-+- */

    public static void tela(Scanner sc) throws Exception {
        try {
            controleUsuario = new ControleUsuario();
        } catch (Exception e) {
            System.out.println("Erro ao iniciar controle de usuários: " + e.getMessage());
            executando = false;
            return;
        }

        while (executando) {
            exibirMenuInicial(sc);
        }
    }

    /*-+-+-+-+-  Menu Inicial  -+-+-+-+- */
    public static void exibirMenuInicial(Scanner sc) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println("\n(1) Login");
        System.out.println("(2) Novo usuário");
        System.out.println("(S) Sair");
        System.out.print("Opção: ");
        char op = sc.nextLine().toUpperCase().charAt(0);
        switch (op) {
            case '1' -> {
                System.out.println("Login selecionado");
                painelLogin(sc);
            }

            case '2' -> {
                System.out.println("Cadastrar selecionado");
                painelCadastro(sc);
            }

            case 'S' -> {
                System.out.println("Saindo...");
                executando = false;
            }

            default -> System.out.println("Opção inválida!");
        }

    }

    /*-+-+-+-+-  Painel de Login  -+-+-+-+- */
    public static void painelLogin(Scanner sc) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------\n>Login\n");
        System.out.println("Digite o email: ");
        String email = sc.nextLine();
        System.out.println("Digite a senha: ");
        String senha = sc.nextLine();

        if (controleUsuario.fazerLogin(email, senha)) {

            System.out.println("\nLogin bem-sucedido! Bem-vindo(a), " + controleUsuario.getUser().getNome() + "!");
            pausar(sc);
            painelInicio(sc);
        } else {

            System.out.println("\nFalha no login! Email ou senha incorretos.");
            pausar(sc);
            exibirMenuInicial(sc);
        }
    }

    /*-+-+-+-+-  Painel de Cadastro  -+-+-+-+- */
    public static void painelCadastro(Scanner sc) throws Exception {
        limparTelaWindows();
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
            Usuario novoUsuario = controleUsuario.cadastrarUsuario(nome, email, senha, perg, resp);
            System.out.println(novoUsuario.toString());
            pausar(sc);
            painelInicio(sc); // Simula login após cadastro
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
            pausar(sc);
        }

    }

    /*-+-+-+-+-  Painel de Dados Usuario  -+-+-+-+- */
    public static void painelMeusDados(Scanner sc) throws Exception {
        limparTelaWindows();

        controleUsuario.exibirDados();

        System.out.println("\n(1) Alterar meus dados");
        System.out.println("(2) Excluir minha conta");
        System.out.println("(3) Retornar ao menu anterior");
        char op = sc.nextLine().charAt(0);
        switch (op) {
            case '1' -> {
                limparTelaWindows();
                alterarMeusDados(sc);
                painelMeusDados(sc); // Reexibe após alteração
            }
            case '2' -> excluirUsuario(sc);
            case '3' -> painelInicio(sc);
            case 'S' -> painelInicio(sc);
            default -> {
                System.out.print("Opção inválida!");
                pausar(sc);
                painelMeusDados(sc);
            }
        }
    }

    /*-+-+-+-+-  Painel de Exclusão de Dados Usuario  -+-+-+-+- */
    public static void excluirUsuario(Scanner sc) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println(">Inicio >Meus dados >Excluir\n");

        System.out.print("Tem certeza que deseja excluir sua conta? (S/N): ");
        String resp = sc.nextLine().toUpperCase();

        if (resp.equals("S")) {

            executando = controleUsuario.excluirUsuario(executando);
        } else {
            System.out.println("Operação cancelada.");
            pausar(sc);
            painelMeusDados(sc);
        }
    }

    /*-+-+-+-+-  Painel de Alteração de Dados Usuario  -+-+-+-+- */
    public static boolean alterarMeusDados(Scanner sc) throws Exception {

        boolean resp = false;

        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println(">Inicio >Meus dados >Alterar\n");

        if (controleUsuario.alterarMeusDados(sc)) {

            System.out.println("Dados atualizados com sucesso!");
            resp = true;
        } else {

            System.out.println("Não houve alteração dos dados!");
        }

        pausar(sc);
        return resp;
    }

    /*-+-+-+-+-  Painel de Inicio  -+-+-+-+- */
    public static void painelInicio(Scanner sc) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------\n>Inicio\n");
        System.out.println("(1) Meus dados");
        System.out.println("(2) Minhas listas");
        System.out.println("(3) Produtos");
        System.out.println("(4) Buscar lista");
        System.out.println("(S) Sair");
        System.out.print("Opção: ");
        char op = sc.nextLine().charAt(0);
        switch (op) {
            case '1' -> {
                System.out.println("Meus dados selecionado");
                painelMeusDados(sc);
            }

            case '2' -> {
                System.out.println("Ver minhas listas");
                painelMinhasListas(sc);
            }

            case '3' -> {
                System.out.println("Acessar produtos");
                System.out.println("Em desenvolvimento...");
                pausar(sc);
            }
            case '4' -> {
                System.out.println("Buscar lista");
                System.out.println("Em desenvolvimento...");
                pausar(sc);
            }

            case 'S' -> {
                System.out.println("Saindo...");
                executando = false;
            }

            default -> System.out.println("Opção inválida!");
        }
    }

    /*-+-+-+-+-  Painel de Minhas Listas -+-+-+-+- */
    public static void painelMinhasListas(Scanner sc) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println(">Início >Minhas listas\n");

        ArrayList<Lista> listas = controleUsuario.getControl().exibirListas();

        System.out.println("\n(N) Nova lista");
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");
        String opcao = sc.nextLine().toUpperCase();

        switch (opcao) {
            case "N" -> painelCadastroListas(sc);
            case "R" -> painelInicio(sc);
            default -> {
                limparTelaWindows();
                int escolha = Integer.parseInt(opcao);
                System.out.println("Presente Fácil 1.0\n-----------------");
                controleUsuario.getControl().exibirDetalhesLista(listas, escolha, sc);
            }
        }
    }

    public static void painelCadastroListas(Scanner sc) {

        System.out.println("Presente Fácil 1.0\n-----------------\n>Cadastro Listas\n");
        try {
            controleUsuario.getControl().cadastrarLista(sc);
            pausar(sc);
            painelMinhasListas(sc);

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public static void painelDetalhesLista(ArrayList<Lista> listasUsuario, int escolha, Scanner sc) throws Exception {

        controleUsuario.getControl().exibirDetalhesLista(listasUsuario, escolha, sc);        
    }

    public static void excluirLista(Scanner sc, Lista lista) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println(">Início >Minhas listas > Excluir\n");

        if(controleUsuario.getControl().excluirLista(sc, lista)){

            System.out.println("Lista excluída com sucesso!");
        }

        painelMinhasListas(sc);
    }

    public static void alterarDadosLista(Scanner sc, Lista lista, int escolha, ArrayList<Lista> array) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println(">Início >Minhas listas > Alterar dados\n");

        if(controleUsuario.getControl().alterarDadosLista(sc, lista)){

            System.out.println("\nLista atualizada com sucesso!");
            pausar(sc);
        } else {

            System.out.println("\nLista nao atualizada!");
            pausar(sc);
        }

        painelDetalhesLista(array, escolha, sc);
    }

    /*-+-+-+-+- ________________________ -+-+-+-+- */
    /*
     * //Para teste
     * public static void main(String[] args){
     * Scanner sc = new Scanner(System.in);
     * tela(sc);
     * sc.close();
     * }
     */

}
