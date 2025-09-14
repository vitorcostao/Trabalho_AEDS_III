package view;

import arvore.ParIntInt;
import controle.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
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

    public static void pausar(Scanner sc) {
        System.out.println("\nPressione ENTER para continuar...");
        sc.nextLine();
    }
    /*-+-+-+-+- ________________________ -+-+-+-+- */

    /*-+-+-+-+- Funões de Painel -+-+-+-+- */

    public static void tela(Scanner sc) {
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
    public static void exibirMenuInicial(Scanner sc) {
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
    public static void painelLogin(Scanner sc) {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------\n>Login\n");
        System.out.println("Digite o email: ");
        String email = sc.nextLine();
        System.out.println("Digite a senha: ");
        String senha = sc.nextLine();

        try {
            Usuario usuario = controleUsuario.buscarPorEmail(email);

            if (usuario.getHashSenha() == senha.hashCode()) {
                System.out.println("\nLogin bem-sucedido! Bem-vindo(a), " + usuario.getNome() + "!");
                controleUsuario.setUser(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getHashSenha(),
                        usuario.getPerguntaSecreta(), usuario.getPerguntaSecreta());

                pausar(sc);
                painelInicio(sc); // Redireciona para o painel do usuário
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
    public static void painelCadastro(Scanner sc) {
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
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println(">Início >Meus dados\n");

        System.out.println("ID: " + controleUsuario.getUser().getId());
        System.out.println("Nome: " + controleUsuario.getUser().getNome());
        System.out.println("Email: " + controleUsuario.getUser().getEmail());
        System.out.println("Pergunta secreta: " + controleUsuario.getUser().getPerguntaSecreta());
        // System.out.println("Hash da senha: " + usuario.getHashSenha());
        System.out.println("\n(1) Alterar meus dados");
        System.out.println("(2) Excluir minha conta");
        System.out.println("(3) Retornar ao menu anterior");
        char op = sc.nextLine().charAt(0);
        switch (op) {
            case '1' -> {
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
            try {

                boolean sucesso = controleUsuario.removerUsuarioPorId(controleUsuario.getUser().getId());

                if (sucesso) {
                    System.out.println("Conta excluída com sucesso.");
                    controleUsuario.fechar();
                    executando = false;
                } else {
                    System.out.println("Erro ao excluir. Tente novamente.");
                }
            } catch (Exception e) {
                System.out.println("Erro ao excluir usuário: " + e.getMessage());
            }
        } else {
            System.out.println("Operação cancelada.");
            pausar(sc);
            painelMeusDados(sc); // Retorna ao painel de dados
        }
    }

    /*-+-+-+-+-  Painel de Alteração de Dados Usuario  -+-+-+-+- */
    public static void alterarMeusDados(Scanner sc) throws Exception {

        Usuario novoUsuario = new Usuario();
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println(">Inicio >Meus dados >Alterar\n");

        System.out.print("Novo nome (deixe em branco para manter): ");
        String nome = sc.nextLine();
        if (!nome.isEmpty())
            novoUsuario.setNome(nome);

        System.out.print("Novo email (deixe em branco para manter): ");
        String email = sc.nextLine();
        if (!email.isEmpty())
            novoUsuario.setEmail(email);

        System.out.print("Nova senha (deixe em branco para manter): ");
        String senha = sc.nextLine();
        if (!senha.isEmpty())
            novoUsuario.setHashSenha(senha.hashCode());

        System.out.print("Nova pergunta secreta (deixe em branco para manter): ");
        String pergunta = sc.nextLine();
        if (!pergunta.isEmpty())
            novoUsuario.setPerguntaSecreta(pergunta);

        System.out.print("Nova resposta secreta (deixe em branco para manter): ");
        String resposta = sc.nextLine();
        if (!resposta.isEmpty())
            novoUsuario.setRespostaSecreta(resposta);

        novoUsuario.setId(controleUsuario.getUser().getId());

        if (controleUsuario.atualizarUsuario(novoUsuario, controleUsuario.getUser().getEmail())) {

            controleUsuario.setUser(novoUsuario.getId(), novoUsuario.getNome(), novoUsuario.getEmail(),
                    novoUsuario.getHashSenha(), novoUsuario.getPerguntaSecreta(), novoUsuario.getPerguntaSecreta());
            System.out.println("\nDados atualizados com sucesso!");
        } else {

            System.out.println("\nErro: Dados não foram atualizados!");
        }
        pausar(sc);
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

        // Mostra todas as listas/presentes do usuário
        int idUsuario = ControleLista.usuarioLogado.getId();
        ParIntInt busca = new ParIntInt(idUsuario, -1);

        ArrayList<Lista> listasUsuario = new ArrayList<>();
        int contador = 1;

        try {
            ArrayList<ParIntInt> listaPresentes = ControleLista.tree.read(busca);

            if (listaPresentes.isEmpty()) {
                System.out.println("Você ainda não possui presentes cadastrados.");
            } else {
                System.out.println("Seus presentes:");

                for (ParIntInt par : listaPresentes) {
                    int idLista = par.getNum2();
                    Lista presente = controleUsuario.getControl().getArquivoLista().read(idLista);
                    listasUsuario.add(presente);
                    System.out.printf("(%d) %s - %s\n", contador, presente.getNome(), presente.getDataLimite());
                    contador++;
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar seus presentes.");
        }

        System.out.println("\n(N) Nova lista");
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");
        String opcao = sc.nextLine().toUpperCase();

        switch (opcao) {
            case "N" -> painelCadastroListas(sc);
            case "R" -> painelInicio(sc);
            default -> {
                try {
                    int escolha = Integer.parseInt(opcao);
                    if (escolha >= 1 && escolha <= listasUsuario.size()) {
                        Lista listaSelecionada = listasUsuario.get(escolha - 1);
                        painelDetalhesLista(sc, listaSelecionada);
                    } else {
                        System.out.println("Opção inválida!");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida!");
                }
            }
        }
    }

    public static void painelCadastroListas(Scanner sc) {

        System.out.println("Presente Fácil 1.0\n-----------------\n>Cadastro Listas\n");
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

            @SuppressWarnings("unused")
            Lista novaLista = controleUsuario.getControl().cadastrarLista(controleUsuario.getUser().getId(), nome,
                    descricao,
                    dataCriacao, dataLimite, codigo);

            @SuppressWarnings("static-access")
            ArrayList<ParIntInt> array = controleUsuario.getControl().tree
                    .read(new ParIntInt(controleUsuario.getUser().getId(), codigo.hashCode()));
            array.forEach(par -> System.out.println(par));

            pausar(sc);
            painelMinhasListas(sc);

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public static void painelDetalhesLista(Scanner sc, Lista lista) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println(">Início >Minhas listas >" + lista.getNome() + "\n");

        System.out.println("CÓDIGO: " + lista.getCodigoCompartilhavel());
        System.out.println("NOME: " + lista.getNome());
        System.out.println("DESCRIÇÃO: " + lista.getDescricao());
        System.out.println("DATA DE CRIAÇÃO: " + lista.getDataCriacao());
        System.out.println("DATA LIMITE: "
                + (lista.getDataLimite().equalsIgnoreCase("NaN") ? "Não definida" : lista.getDataLimite()));

        System.out.println("\n(1) Gerenciar produtos da lista");
        System.out.println("(2) Alterar dados da lista");
        System.out.println("(3) Excluir lista");
        System.out.println("(R) Retornar ao menu anterior");

        System.out.print("Opção: ");
        String op = sc.nextLine().toUpperCase();

        switch (op) {
            case "1" -> {
                pausar(sc);
            }
            case "2" -> {
                alterarDadosLista(sc, lista);
                pausar(sc);
            }
            case "3" -> {
                excluirLista(sc, lista);
                pausar(sc);
            }
            case "R" -> {

                painelMinhasListas(sc);
            }

            default -> {
                System.out.println("Opção inválida.");
                pausar(sc);
            }
        }
    }

    public static void excluirLista(Scanner sc, Lista lista) throws Exception{
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println(">Início >Minhas listas > Excluir\n");

        System.out.println("Você tem certeza que deseja excluir a lista \"" + lista.getNome() + "\"? (S/N): ");
        String confirm = sc.nextLine().toUpperCase();

        if (confirm.equals("S")) {
            try {
                // Remove do arquivo
                controleUsuario.getControl().getArquivoLista().delete(lista.getId());

                // Remove da árvore B+
                ParIntInt par = new ParIntInt(lista.getIdUsuario(), lista.getId());
                ControleLista.tree.delete(par);

                System.out.println("Lista excluída com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro ao excluir a lista: " + e.getMessage());
            }
        } else {
            System.out.println("Exclusão cancelada.");
        }

        painelMinhasListas(sc);
    }

    public static void alterarDadosLista(Scanner sc, Lista lista) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println(">Início >Minhas listas > Alterar dados\n");

        try {
            System.out.println("CÓDIGO: " + lista.getCodigoCompartilhavel());

            // Nome
            System.out.print("Novo nome (deixe em branco para manter \"" + lista.getNome() + "\"): ");
            String novoNome = sc.nextLine();
            if (!novoNome.isBlank()) {
                lista.setNome(novoNome);
            }

            // Descrição
            System.out.print("Nova descrição (deixe em branco para manter \"" + lista.getDescricao() + "\"): ");
            String novaDesc = sc.nextLine();
            if (!novaDesc.isBlank()) {
                lista.setDescricao(novaDesc);
            }

            // Data limite
            System.out.print("Nova data limite (AAAA-MM-DD) ou deixe em branco para manter \""
                    + (lista.getDataLimite().equalsIgnoreCase("NaN") ? "Não definida" : lista.getDataLimite())
                    + "\"): ");
            String novaData = sc.nextLine();
            if (!novaData.isBlank()) {
                // Validação simples de formato YYYY-MM-DD
                if (novaData.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    lista.setDataLimite(novaData);
                } else {
                    System.out.println("Formato inválido! Mantendo a data atual.");
                }
            }

            // Salva as alterações no arquivo
            controleUsuario.getControl().getArquivoLista().update(lista);

            System.out.println("\nLista atualizada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar a lista: " + e.getMessage());
        }

        painelDetalhesLista(sc, lista);
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
