package view;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import controle.ControleUsuario;
import model.Lista;
import model.Usuario;

public class Painel {

    private static boolean executando = true;
    private static ControleUsuario controleUsuario = null;

    /*-+-+-+-+- Funões de Auxilio de Painel -+-+-+-+- */
    public static void limparTelaWindows() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        } catch (Exception e) {
            for(int i = 0; i < 50; i++) System.out.println(); //Simula limpar tela
        }
    }

    public static void pausar(Scanner sc) {
        System.out.println("\nPressione ENTER para continuar...");
        sc.nextLine();
    }
    /*-+-+-+-+- ________________________ -+-+-+-+- */

    /*-+-+-+-+- Funões de Painel -+-+-+-+- */

   public static void tela(Scanner sc){
    try {
        controleUsuario = new ControleUsuario();
    } catch (Exception e) {
        System.out.println("Erro ao iniciar controle de usuários: " + e.getMessage());
        executando = false;
        return;
    }

    while (executando){
        exibirMenuInicial(sc);
    }
}

    /*-+-+-+-+-  Menu Inicial  -+-+-+-+- */
    public static void exibirMenuInicial(Scanner sc) {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println("\n(1) Login");
        System.out.println("(2) Novo usuário");
        System.out.println("(S) Sair");
        System.out.print("Opção: ");
        char op = sc.nextLine().toUpperCase().charAt(0);
        switch (op) {
            case '1':
                System.out.println("Login selecionado");
                painelLogin(sc);
            break;

            case '2':
                System.out.println("Cadastrar selecionado");
                painelCadastro(sc);
            break;

            case 'S':
                System.out.println("Saindo...");  
                executando = false;
            break;
        
            default:
            System.out.println("Opção inválida!");
            break;
        }
        
    }

     /*-+-+-+-+-  Painel de Login  -+-+-+-+- */
    public static void painelLogin(Scanner sc){
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------\n>Login\n");
        System.out.println("Digite o email: ");
        String email = sc.nextLine();
        System.out.println("Digite a senha: ");
        String senha = sc.nextLine();
        
        try {
        Usuario usuario = controleUsuario.buscarPorEmail(email);
        
        if (usuario != null && usuario.getHashSenha() == senha.hashCode()) {
            System.out.println("\nLogin bem-sucedido! Bem-vindo(a), " + usuario.getNome() + "!");
            pausar(sc);
            painelInicio(sc, usuario);  // Redireciona para o painel do usuário
        } else {
            System.out.println("\nFalha no login! Email ou senha incorretos.");
            pausar(sc);
        }
    } catch (Exception e) {
        System.out.println("Erro ao tentar fazer login: " + e.getMessage());
        pausar(sc);
    }
    }
    
     /*-+-+-+-+-  Painel de Cadastro  -+-+-+-+- */
    public static void painelCadastro(Scanner sc){
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
            painelInicio(sc, novoUsuario); // Simula login após cadastro
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
            pausar(sc);
        }


    }

     /*-+-+-+-+-  Painel de Dados Usuario  -+-+-+-+- */
    public static void painelMeusDados(Scanner sc, Usuario usuario) {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println(">Início >Meus dados\n");

        System.out.println("ID: " + usuario.getId());
        System.out.println("Nome: " + usuario.getNome());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Pergunta secreta: " + usuario.getPerguntaSecreta());
        // System.out.println("Hash da senha: " + usuario.getHashSenha());
        System.out.println("\n(1) Alterar meus dados");
        System.out.println("(2) Excluir minha conta");
        System.out.println("(3) Retornar ao menu anterior");
        char op = sc.nextLine().charAt(0);
        switch (op) {
                case '1':
                    alterarMeusDados(sc, usuario);
                    painelMeusDados(sc, usuario); // Reexibe após alteração
                    break;
                case '2':
                    excluirUsuario(sc, usuario);
                    break;
                case '3':
                    painelInicio(sc, usuario);
                    break;
                case 'S':
                    painelInicio(sc, usuario);
                    break;
                default:
                    System.out.print("Opção inválida!");  
                    pausar(sc);
                    painelMeusDados(sc, usuario);      
        }
    }

     /*-+-+-+-+-  Painel de Exclusão de Dados Usuario  -+-+-+-+- */
    public static void excluirUsuario(Scanner sc, Usuario usuario) {
    limparTelaWindows();
    System.out.println("Presente Fácil 1.0\n-----------------");
    System.out.println(">Inicio >Meus dados >Excluir\n");

    System.out.print("Tem certeza que deseja excluir sua conta? (S/N): ");
    String resp = sc.nextLine().toUpperCase();

    if (resp.equals("S")) {
        try {
            ControleUsuario controle = new ControleUsuario(); // ← garante instância funcional
            boolean sucesso = controle.removerUsuarioPorId(usuario.getId());

            if (sucesso) {
                System.out.println("Conta excluída com sucesso.");
                controle.fechar();
                executando = false; // encerra sistema
            } else {
                System.out.println("Erro ao excluir. Tente novamente.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao excluir usuário: " + e.getMessage());
        }
    } else {
        System.out.println("Operação cancelada.");
        pausar(sc);
        painelMeusDados(sc, usuario); // Retorna ao painel de dados
    }
}


     /*-+-+-+-+-  Painel de Alteração de Dados Usuario  -+-+-+-+- */
    public static void alterarMeusDados(Scanner sc, Usuario usuario) {
    limparTelaWindows();
    System.out.println("Presente Fácil 1.0\n-----------------");
    System.out.println(">Inicio >Meus dados >Alterar\n");

    System.out.print("Novo nome (deixe em branco para manter): ");
    String nome = sc.nextLine();
    if (!nome.isEmpty()) usuario.setNome(nome);

    System.out.print("Novo email (deixe em branco para manter): ");
    String email = sc.nextLine();
    if (!email.isEmpty()) usuario.setEmail(email);

    System.out.print("Nova senha (deixe em branco para manter): ");
    String senha = sc.nextLine();
    if (!senha.isEmpty()) usuario.setHashSenha(senha.hashCode());

    System.out.print("Nova pergunta secreta (deixe em branco para manter): ");
    String pergunta = sc.nextLine();
    if (!pergunta.isEmpty()) usuario.setPerguntaSecreta(pergunta);

    System.out.print("Nova resposta secreta (deixe em branco para manter): ");
    String resposta = sc.nextLine();
    if (!resposta.isEmpty()) usuario.setRespostaSecreta(resposta);

    // Aqui você deveria persistir os dados no arquivo via seu CRUD (update)
    // Exemplo: arqUsuario.update(usuario);

    System.out.println("\nDados atualizados com sucesso!");
    pausar(sc);
}

     /*-+-+-+-+-  Painel de Inicio  -+-+-+-+- */
    public static void painelInicio(Scanner sc, Usuario usuario) {
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
            case '1':
                System.out.println("Meus dados selecionado");
                painelMeusDados(sc, usuario);
               
            break;

            case '2':
                System.out.println("Ver minhas listas");
                painelMinhasListas(sc, usuario);
            break;

            case '3':
                System.out.println("Acessar produtos");
                System.out.println("Em desenvolvimento...");
                pausar(sc);

            break;
            case '4':
                System.out.println("Buscar lista"); 
                System.out.println("Em desenvolvimento...");
                pausar(sc);
             break;

            case 'S':
                System.out.println("Saindo...");  
                executando = false;
            break;
        
            default:
            System.out.println("Opção inválida!");
            break;
        }
    }

    
    

    /*-+-+-+-+-  Painel de Minhas Listas -+-+-+-+- */
    public static void painelMinhasListas(Scanner sc, Usuario usuario) {
    limparTelaWindows();
    System.out.println("Presente Fácil 1.0\n-----------------");
    System.out.println(">Início >Minhas listas\n");

    // TODO: Trocar essa parte pelo carregamento real via CRUD
    ArrayList<Lista> listasDoUsuario = new ArrayList<>();

    // Simulando algumas listas
    listasDoUsuario.add(new Lista(1, usuario.getId(), "Aniversário 2025", "Sugestões de presentes", "2025-07-30", "2025-10-31", "tdfd9as8bp"));
    listasDoUsuario.add(new Lista(2, usuario.getId(), "Decoração da casa", "Itens para o novo apê", "2025-03-15", "NaN", "x4a7kl0z9q"));
    listasDoUsuario.add(new Lista(3, usuario.getId(), "Dia dos pais", "Presentes pro paizão", "2025-07-20", "2025-08-10", "a8m3pwqk2l"));
    listasDoUsuario.add(new Lista(4, usuario.getId(), "Natal 2024", "Ideias natalinas", "2024-12-01", "NaN", "mnbv8723zx"));

    // Ordena alfabeticamente por nome
    listasDoUsuario.sort(Comparator.comparing(Lista::getNome));

    System.out.println("LISTAS");
    for (int i = 0; i < listasDoUsuario.size(); i++) {
        Lista l = listasDoUsuario.get(i);
        System.out.printf("(%d) %s - %s\n", i + 1, l.getNome(), l.getDataCriacao());
    }

    System.out.println("\n(N) Nova lista");
    System.out.println("(R) Retornar ao menu anterior");
    System.out.print("\nOpção: ");
    String opcao = sc.nextLine().toUpperCase();

    switch (opcao) {
        case "N":
            System.out.println("Criação de nova lista ainda não implementada.");
            pausar(sc);
            break;
        case "R":
           painelInicio(sc, usuario);
        default:
            try {
                int indice = Integer.parseInt(opcao) - 1;
                if (indice >= 0 && indice < listasDoUsuario.size()) {
                    painelDetalhesLista(sc, listasDoUsuario.get(indice));
                } else {
                    System.out.println("Opção inválida.");
                    pausar(sc);
                }
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida.");
                pausar(sc);
            }
    }

    painelMinhasListas(sc, usuario); // Volta após ação
}


public static void painelDetalhesLista(Scanner sc, Lista lista) {
    limparTelaWindows();
    System.out.println("Presente Fácil 1.0\n-----------------");
    System.out.println(">Início >Minhas listas >" + lista.getNome() + "\n");

    System.out.println("CÓDIGO: " + lista.getCodigoCompartilhavel());
    System.out.println("NOME: " + lista.getNome());
    System.out.println("DESCRIÇÃO: " + lista.getDescricao());
    System.out.println("DATA DE CRIAÇÃO: " + lista.getDataCriacao());
    System.out.println("DATA LIMITE: " + (lista.getDataLimite().equalsIgnoreCase("NaN") ? "Não definida" : lista.getDataLimite()));

    System.out.println("\n(1) Gerenciar produtos da lista");
    System.out.println("(2) Alterar dados da lista");
    System.out.println("(3) Excluir lista");
    System.out.println("(R) Retornar ao menu anterior");

    System.out.print("Opção: ");
    String op = sc.nextLine().toUpperCase();

    switch (op) {
        case "1":
            System.out.println("Funcionalidade de produtos ainda não disponível.");
            pausar(sc);
            break;
        case "2":
            System.out.println("Alteração de dados em desenvolvimento.");
            pausar(sc);
            break;
        case "3":
            System.out.println("Exclusão da lista (confirmar...)");
            pausar(sc);
            break;
        case "R":
            return;
        default:
            System.out.println("Opção inválida.");
            pausar(sc);
    }

    painelDetalhesLista(sc, lista); // Loop nos detalhes
}


    
    /*-+-+-+-+- ________________________ -+-+-+-+- */
   /* 
    //Para teste 
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);  
        tela(sc);
        sc.close();
    }*/

}
