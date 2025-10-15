package view;

import controle.*;
import java.io.IOException;
import java.util.*;
import model.Lista;
import model.ListaProduto;
import model.Produto;
import model.Usuario;
import service.ListaProduto.ArquivoListaProduto;
import service.Produtos.ArquivoProduto;

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
        System.out.println("Presente Fácil 1.0\n----------------------------------------->");
        System.out.println("\n(1) Login");
        System.out.println("(2) Novo usuário");
        System.out.println("(S) Sair");
        System.out.print("Opção: ");
        String op = sc.nextLine().toUpperCase();
        switch (op) {
            case "1" -> {
                System.out.println("Login selecionado");
                painelLogin(sc);
            }

            case "2" -> {
                System.out.println("Cadastrar selecionado");
                painelCadastro(sc);
            }

            case "S" -> {
                System.out.println("Saindo...");
                executando = false;
            }

            default -> System.out.println("Opção inválida!");
        }

    }

    /*-+-+-+-+-  Painel de Login  -+-+-+-+- */
    public static void painelLogin(Scanner sc) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n----------------------------------------->");
        System.out.println("> Login\n");
        System.out.println("Digite o email: ");
        String email = sc.nextLine();
        System.out.println("Digite a senha: ");
        String senha = sc.nextLine();

        if (controleUsuario.fazerLogin(email, senha)) {

            System.out.println("\nLogin bem-sucedido! Bem-vindo(a), " + controleUsuario.getUser().getNome() + "!");
            pausar(sc);
            painelInicio(sc);
        } else {

            pausar(sc);
            exibirMenuInicial(sc);
        }
    }

    /*-+-+-+-+-  Painel de Cadastro  -+-+-+-+- */
    public static void painelCadastro(Scanner sc) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n----------------------------------------->");
        System.out.println("> Cadastro\n");
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

            System.out.println("Usuario " + novoUsuario.getNome() + " cadastrado com sucesso!");
            pausar(sc);
            exibirMenuInicial(sc);
            ;
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
            pausar(sc);
        }

    }

    /*-+-+-+-+-  Os códigos abaixos tratam do fluxo usuário -+-+-+-+- */

    public static void painelMeusDados(Scanner sc) throws Exception {
        limparTelaWindows();

        controleUsuario.exibirDados();

        System.out.println("\n(1) Alterar meus dados");
        System.out.println("(2) Excluir minha conta");
        System.out.println("(0) Retornar ao menu anterior");
        String op = sc.nextLine();
        switch (op) {
            case "1" -> {
                limparTelaWindows();
                alterarMeusDados(sc);
                painelMeusDados(sc); // Reexibe após alteração
            }
            case "2" -> excluirUsuario(sc);
            case "3" -> painelInicio(sc);
            case "0" -> painelInicio(sc);
            default -> {
                System.out.print("Opção inválida!");
                pausar(sc);
                painelMeusDados(sc);
            }
        }
    }

    public static void excluirUsuario(Scanner sc) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n----------------------------------------->");
        System.out.println("> Inicio > Meus dados > Excluir\n");

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

    public static boolean alterarMeusDados(Scanner sc) throws Exception {

        boolean resp = false;

        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n----------------------------------------->");
        System.out.println("> Inicio > Meus dados > Alterar\n");

        if (controleUsuario.alterarMeusDados(sc)) {

            System.out.println("Dados atualizados com sucesso!");
            resp = true;
        } else {

            System.out.println("Não houve alteração dos dados!");
        }

        pausar(sc);
        return resp;
    }

    public static void painelInicio(Scanner sc) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n----------------------------------------->");
        System.out.println("> Início");
        System.out.println("(1) Meus dados");
        System.out.println("(2) Minhas listas");
        System.out.println("(3) Produtos");
        System.out.println("(4) Buscar lista");
        System.out.println("(S) Sair");
        System.out.print("Opção: ");
        String op = sc.nextLine().toUpperCase();
        switch (op) {
            case "1" -> {
                System.out.println("Meus dados selecionado");
                painelMeusDados(sc);
            }

            case "2" -> {
                System.out.println("Ver minhas listas");
                painelMinhasListas(sc);
            }

            case "3" -> {
                System.out.println("Acessar produtos");
                painelProdutos(sc);
                pausar(sc);
                painelInicio(sc);
            }
            case "4" -> {
                System.out.println("Buscar lista");
                painelBusca(sc);
            }

            case "S" -> {
                System.out.println("Saindo...");
                executando = false;
            }

            default -> System.out.println("Opção inválida!");
        }
    }

    /*-+-+-+-+-  Os códigos abaixos tratam da visualização dos listas -+-+-+-+- */

    public static void painelMinhasListas(Scanner sc) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n----------------------------------------->");
        System.out.println("> Início > Minhas listas\n");

        ArrayList<Lista> listas = controleUsuario.getControleLista().exibirListas();

        System.out.println("\n(N) Nova lista");
        System.out.println("(0) Retornar ao menu anterior");
        System.out.print("\nOpção: ");
        String opcao = sc.nextLine().toUpperCase();

        switch (opcao) {
            case "N" -> painelCadastroListas(sc);
            case "0" -> painelInicio(sc);
            default -> {
                limparTelaWindows();
                int escolha = Integer.parseInt(opcao);
                Lista listaSelecionada = listas.get(escolha - 1);
                Painel.painelDetalhesLista(listaSelecionada, escolha, sc);
            }
        }
    }

    public static void painelCadastroListas(Scanner sc) {

        System.out.println("Presente Fácil 1.0\n----------------------------------------->\n");
        System.out.println("> Início > Minhas listas > Cadastro Listas\n");
        try {
            controleUsuario.getControleLista().cadastrarLista(sc);
            pausar(sc);
            painelMinhasListas(sc);

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public static void painelDetalhesLista(Lista listaSelecionada, int escolha, Scanner sc) throws Exception {

        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println("> Início > Minhas listas > " + listaSelecionada.getNome());

        listaSelecionada.print();

        System.out.println("\n--- Produtos na Lista ---");
        try {
            ArrayList<Produto> produtosNaLista = controleUsuario.getControleListaProduto().listarProdutosDaLista(listaSelecionada);

            if (produtosNaLista.isEmpty()) {
                System.out.println("Nenhum produto nesta lista.");
            } else {
                for (Produto p : produtosNaLista) {
                    System.out.println("- " + p.getNome() + " (GTIN: " + p.getGTIN() + ")");
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar os produtos da lista: " + e.getMessage());
        }

        System.out.println("\n(1) Gerenciar produtos da lista");
        System.out.println("(2) Alterar dados da lista");
        System.out.println("(3) Excluir Lista");
        System.out.println("(0) Retornar ao menu anterior\n\n");

        System.out.print("Opção: ");
        String op = sc.nextLine();

        switch (op) {
            case "1" -> {
                painelGerenciarProdutos(sc, listaSelecionada, escolha);
            }

            case "2" -> {
                alterarDadosLista(sc, listaSelecionada, escolha);
                pausar(sc);
            }

            case "3" -> {
                excluirLista(sc, listaSelecionada);
                pausar(sc);
            }

            case "0" -> {
                painelMinhasListas(sc);
            }

            default -> {
                System.out.println("Opção inválida!");
                pausar(sc);
            }
        }
    }

    public static void excluirLista(Scanner sc, Lista lista) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n----------------------------------------->");
        System.out.println("> Início > Minhas listas > Excluir\n");

        if (controleUsuario.getControleLista().excluirLista(sc, lista)) {

            System.out.println("Lista excluída com sucesso!");
        }

        painelMinhasListas(sc);
    }

    public static void alterarDadosLista(Scanner sc, Lista lista, int escolha) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n----------------------------------------->");
        System.out.println("> Início > Minhas listas > Alterar dados\n");

        if (controleUsuario.getControleLista().alterarDadosLista(sc, lista)) {

            System.out.println("\nLista atualizada com sucesso!");
            pausar(sc);
        } else {

            System.out.println("\nLista nao atualizada!");
            pausar(sc);
        }

        painelDetalhesLista(lista, escolha, sc);
    }

    public static void painelBusca(Scanner sc) throws Exception {

        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n----------------------------------------->");
        System.out.println("> Início > Busca\n");

        System.out.println("\n\nInsira o codigo compartilhavel: ");
        String codigo = sc.nextLine();

        ControleLista controleLista = new ControleLista(controleUsuario.getUser());

        controleLista.PesquisarPorCodigo(codigo);

        pausar(sc);
        Painel.painelInicio(sc);

    }

    /*-+-+-+-+-  Os códigos abaixos tratam da visualização dos produtos -+-+-+-+- */

    public static void painelProdutos(Scanner sc) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println("> Início > Produtos\n");

        System.out.println("(1) Buscar produtos por GTIN");
        System.out.println("(2) Listar todos os produtos");
        System.out.println("(3) Cadastrar um novo produto");
        System.out.println("\n(R) Retornar ao menu anterior");

        System.out.print("\nOpção: ");
        String op = sc.nextLine().toUpperCase();

        switch (op) {
            case "1" -> painelBuscarProduto(sc);
            case "2" -> painelListarProdutos(sc, 1);
            case "3" -> painelCadastrarProduto(sc);
            case "R" -> painelInicio(sc);
            default -> {
                System.out.println("Opção inválida!");
                pausar(sc);
                painelProdutos(sc);
            }
        }
    }

    public static void painelBuscarProduto(Scanner sc) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println("> Início > Produtos > Buscar por GTIN\n");

        System.out.print("Digite o GTIN-13 do produto: ");
        String gtin = sc.nextLine();

        Produto produto = controleUsuario.getControleProdutos().buscarProdutoPorGTIN(gtin);
        if (produto != null) {
            painelDetalhesProduto(produto, sc);
        } else {
            System.out.println("\nProduto não encontrado.");
            pausar(sc);
            painelProdutos(sc);
        }
    }

    public static void painelListarProdutos(Scanner sc, int pagina) throws Exception {
        limparTelaWindows();
        ArrayList<Produto> todos = controleUsuario.getControleProdutos().listarProdutosOrdenados();

        int total = todos.size();
        int porPagina = 10;
        int totalPaginas = (int) Math.ceil((double) total / porPagina);
        int inicio = (pagina - 1) * porPagina;

        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println("> Início > Produtos > Listagem\n");
        System.out.println("Página " + pagina + " de " + totalPaginas + "\n");

        for (int i = 0; i < porPagina && (inicio + i) < total; i++) {
            Produto p = todos.get(inicio + i);
            System.out.println("(" + (i + 1) + ") " + p.getNome());
        }

        System.out.println("\n(A) Página anterior");
        System.out.println("(P) Próxima página");
        System.out.println("(R) Retornar ao menu anterior");

        System.out.print("\nOpção: ");
        String op = sc.nextLine().toUpperCase();

        switch (op) {
            case "A" -> {
                if (pagina > 1)
                    painelListarProdutos(sc, pagina - 1);
                else
                    painelListarProdutos(sc, pagina);
            }
            case "P" -> {
                if (pagina < totalPaginas)
                    painelListarProdutos(sc, pagina + 1);
                else
                    painelListarProdutos(sc, pagina);
            }
            case "R" -> painelProdutos(sc);
            default -> {
                try {
                    int escolha = Integer.parseInt(op);
                    int index = inicio + escolha - 1;
                    if (escolha >= 1 && index < total) {
                        painelDetalhesProduto(todos.get(index), sc);
                    } else {
                        System.out.println("Produto inválido.");
                        pausar(sc);
                        painelListarProdutos(sc, pagina);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida.");
                    pausar(sc);
                    painelListarProdutos(sc, pagina);
                }
            }
        }
    }

    public static void painelCadastrarProduto(Scanner sc) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println("> Início > Produtos > Cadastrar novo produto\n");


        System.out.print("Nome: ");
        String nome = sc.nextLine();

        System.out.print("Descrição: ");
        String desc = sc.nextLine();

        System.out.print("Quantidade: ");
        int quantidade = sc.nextInt();

        System.out.print("Observações: (Deixe em branco para nenhuma)");
        sc.nextLine();
        String  observacoes = sc.nextLine();
        Produto novo = controleUsuario.getControleProdutos().cadastrarProduto(nome, desc, quantidade, observacoes);

        if (novo != null) {
            System.out.println("Produto cadastrado com sucesso!");
            System.out.print("GTIN-13: "+ novo.getGTIN());
        } else {
            System.out.println("Erro ao cadastrar produto.");
        }

        pausar(sc);
        painelProdutos(sc);
    }

    public static void painelDetalhesProduto(Produto p, Scanner sc) throws Exception {
        limparTelaWindows();
        System.out.println("Presente Fácil 1.0\n-----------------");
        System.out.println("> Início > Produtos > Listagem > " + p.getNome() + "\n");

        System.out.println("NOME.......: " + p.getNome());
        System.out.println("GTIN-13....: " + p.getGTIN());
        System.out.println("DESCRIÇÃO..: " + p.getDescricao());
        System.out.println("QUANTIDADE.: " + p.getQuantidade());
        System.out.println("OBSERVAÇÔES: " + p.getObservacoes());


        System.out.println("\nAparece nas minhas listas:");

        ArrayList<Lista> list = controleUsuario.getControleListaProduto().findListFromProducts(p.getId());

        if (list.isEmpty()){

            System.out.println("- (nenhuma)");
        } else {

            int i = 0;

            for(Lista L : list){

                System.out.println( "- " + list.get(i).getNome());
                i++;
            }
        }

        int countOutras = controleUsuario.getControleListaProduto()
                .contarListasDeOutrosUsuariosQueContemProduto(p.getId());
        System.out.println("\nAparece também em mais " + countOutras + " listas de outras pessoas.\n");


        System.out.println("(1) Alterar os dados do produto");
        if(p.getStatus() == 0){
            System.out.println("(2) Reativar produto");
        }else {
            System.out.println("(2) Desativar produto");
        }
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");
        String op = sc.nextLine().toUpperCase();

        switch (op) {
            case "1" -> {
                controleUsuario.getControleProdutos().atualizarProduto(sc, p.getId());
                Produto novo = controleUsuario.getControleProdutos().getArquivoProduto().read(p.getId());
                painelDetalhesProduto(novo, sc);
            }
            case "2" -> {

                if(p.getStatus() == 0){
                    //nao sei referenciar o produto corretamente (E sei que não é nesse arquivo, mas
                    // o objetivo é selecionar o produto e usar o p.Reativar();
                }else{
                    //nao sei referenciar o produto corretamente (E sei que não é nesse arquivo, mas
                    // o objetivo é selecionar o produto e usar o p.Desativar();

                }

                //controleUsuario.getControleProdutos().DesativarProduto(p.getId());
                painelProdutos(sc);
            }
            case "R" -> painelProdutos(sc);
            default -> {
                System.out.println("Opção inválida.");
                pausar(sc);
                painelDetalhesProduto(p, sc);
            }
        }
    }

    /*-+-+-+-+-  Os códigos abaixos tratam da relação entre listas e produtos -+-+-+-+- */

    
    public static void painelGerenciarProdutos(Scanner sc, Lista listaSelecionada, int escolha) throws Exception {
        limparTelaWindows();

        System.out.println("PresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início > Minhas listas > " + listaSelecionada.getNome() + " > Produtos\n");

        System.out.println("(1) Acrescentar produtos");
        System.out.println("(2) Remover produtos");
        System.out.println("(R) Retornar ao menu anterior\n\n");
        
        System.out.print("Opção: ");
        String op = sc.nextLine().toUpperCase();

        switch(op){

            case "1" -> {

                painelAcrescentarProdutos(sc, listaSelecionada, escolha);    
            }

            case "2" -> {

                painelRemoverProdutos(sc, listaSelecionada, escolha);
            }

            case "R" -> {

                painelDetalhesLista(listaSelecionada, escolha, sc);
            }
        }
    }

    public static void painelRemoverProdutos(Scanner sc, Lista listaSelecionada, int escolha) throws Exception {
        limparTelaWindows();
        System.out.println("PresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início > Minhas listas > " + listaSelecionada.getNome() + " > Produtos > Remover Produtos\n");

        ArrayList<Produto> produtos = controleUsuario.getControleListaProduto().listarProdutosDaLista(listaSelecionada);

        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto para remover nesta lista.");
            pausar(sc);
            painelGerenciarProdutos(sc, listaSelecionada, escolha);
            return;
        }

        for (int i = 0; i < produtos.size(); i++) {
            System.out.println("(" + (i + 1) + ") " + produtos.get(i).getNome());
        }

        System.out.println("\n(R) Retornar");
        System.out.print("\nEscolha o produto para remover: ");
        String op = sc.nextLine().toUpperCase();

        if (op.equals("R")) {
            painelGerenciarProdutos(sc, listaSelecionada, escolha);
            return;
        }

        try {
            int index = Integer.parseInt(op) - 1;
            if (index >= 0 && index < produtos.size()) {
                Produto produtoParaRemover = produtos.get(index);
                if (controleUsuario.getControleListaProduto().removerProdutoDaLista(produtoParaRemover.getId(), listaSelecionada.getId())) {
                    System.out.println("Produto removido com sucesso!");
                } else {
                    System.out.println("Erro ao remover o produto.");
                }
            } else {
                System.out.println("Seleção inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida.");
        }
        pausar(sc);
        painelGerenciarProdutos(sc, listaSelecionada, escolha);
    }

    public static void painelListarProdutosAcrescentar(Scanner sc, int pagina, Lista listaSelecionada) throws Exception {
        
        limparTelaWindows();
        ArrayList<Produto> todos = controleUsuario.getControleProdutos().listarProdutosOrdenados();

        int total = todos.size();
        int porPagina = 10;
        int totalPaginas = (int) Math.ceil((double) total / porPagina);
        int inicio = (pagina - 1) * porPagina;

        System.out.println("PresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início > Minhas listas > " + listaSelecionada.getNome() + " > Produtos" + " > Acrescentar Produtos" + " > Listagem");

        for (int i = 0; i < porPagina && (inicio + i) < total; i++) {
            Produto p = todos.get(inicio + i);
            System.out.println("(" + (i + 1) + ") " + p.getNome());
        }

        System.out.println("\n(A) Página anterior");
        System.out.println("(P) Próxima página");
        System.out.println("(R) Retornar ao menu anterior");

        System.out.print("\nOpção: ");
        String op = sc.nextLine().toUpperCase();

        switch (op) {
            case "A" -> {
                if (pagina > 1)
                    painelListarProdutosAcrescentar(sc, pagina - 1, listaSelecionada);
                else
                    painelListarProdutosAcrescentar(sc, pagina, listaSelecionada);
            }
            case "P" -> {
                if (pagina < totalPaginas)
                    painelListarProdutosAcrescentar(sc, pagina + 1, listaSelecionada);
                else
                    painelListarProdutosAcrescentar(sc, pagina, listaSelecionada);
            }
            case "R" -> painelProdutos(sc);
            default -> {
                try {
                    int escolha = Integer.parseInt(op);
                    int index = inicio + escolha - 1;
                    if (escolha >= 1 && index < total) {

                        if(controleUsuario.getControleListaProduto().adicionarProdutoNaLista(todos.get(index).getId(), listaSelecionada)){

                            System.out.println("Produto associado a " + listaSelecionada.getNome() + "!");
                            pausar(sc);
                        } else {

                            System.out.println("Produto já associado a " + listaSelecionada.getNome() + "!");
                            pausar(sc);
                        }

                        painelMinhasListas(sc);
                    } else {
                        System.out.println("Produto inválido.");
                        pausar(sc);
                        painelMinhasListas(sc);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Opção inválida.");
                    pausar(sc);
                    painelMinhasListas(sc);
                }
            }
        }
    }

    public static void painelAcrescentarProdutos(Scanner sc, Lista listaSelecionada, int escolha) throws Exception {

        limparTelaWindows();

        System.out.println("PresenteFácil 1.0");
        System.out.println("-----------------");
        System.out.println("> Início > Minhas listas > " + listaSelecionada.getNome() + " > Produtos" + " > Acrescentar Produtos\n");
        
        System.out.println("(1) Buscar produtos por GTIN");
        System.out.println("(2) Listar todos os produtos\n");
        System.out.println("(R) Retornar ao menu anterior\n");

        System.out.print("Opção: ");
        String op = sc.nextLine();

        switch (op) {
            case "1" -> {

                System.out.print("Digite o GTIN do produto: ");
                String GTIN = sc.nextLine();

                Produto p = controleUsuario.getControleProdutos().buscarProdutoPorGTIN(GTIN);
                
                if(p != null && p.getStatus() == 1){

                    if(controleUsuario.getControleListaProduto().adicionarProdutoNaLista(p.getId(), listaSelecionada)){
                        
                        System.out.println("Produto associado a " + listaSelecionada.getNome() + "!");
                        pausar(sc);
                    } else {

                        System.out.println("Produto já associado a " + listaSelecionada.getNome() + "!");
                        pausar(sc);
                    }                 
                    
                } else {

                    System.out.println("Produto desativado ou inexistente!");
                    pausar(sc);
                    painelAcrescentarProdutos(sc, listaSelecionada, escolha);
                }

                painelGerenciarProdutos(sc, listaSelecionada, escolha);
            }

            case "2" -> {

                painelListarProdutosAcrescentar(sc, escolha, listaSelecionada);
            }

            case "R" -> {

                painelGerenciarProdutos(sc, listaSelecionada, escolha);
            }
            
            default -> {

                System.out.println("Opcao invalida!");
                pausar(sc);
                painelAcrescentarProdutos(sc, listaSelecionada, escolha);
            }   
        }
    }

}
