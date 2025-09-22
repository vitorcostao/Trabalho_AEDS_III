package controle;

import arvore.ParIntInt;
import arvore.aed3.ArvoreBMais;
import java.time.LocalDate;
import java.util.*;
import model.Lista;
import model.Usuario;
import service.ArquivoLista;
import service.NanoID;
import view.Painel;

public class ControleLista {

    private static ArquivoLista arquivoLista;
    public static ArvoreBMais<ParIntInt> tree;
    public Usuario usuarioLogado;

    public ControleLista() throws Exception {
        arquivoLista = new ArquivoLista();
        tree = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 5, "arvoreBmais.db");
    }

    public ControleLista(Usuario usuarioLogado) throws Exception {
        arquivoLista = new ArquivoLista();
        this.usuarioLogado = usuarioLogado;
        tree = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 5, "arvoreBmais.db");
    }

    @SuppressWarnings("static-access")
    public ArquivoLista getArquivoLista() {

        return this.arquivoLista;
    }

    @SuppressWarnings("rawtypes")
    public ArvoreBMais getArvoreBMais() {

        return tree;
    }

    public void setUser(Usuario usuarioLogado) {

        this.usuarioLogado = usuarioLogado;
    }

    /*-+-+-+-+- Cadastrar Lista -+-+-+-+- */
    public void cadastrarLista(Scanner sc) throws Exception {

        System.out.println("Digite o nome: ");
        String nome = sc.nextLine();
        System.out.println("Digite a descricao: ");
        String descricao = sc.nextLine();

        LocalDate hoje = LocalDate.now();
        String dataCriacao = hoje.toString();

        System.out.println("Digite a data limite(dd/MM/yyyy): ");
        String dataLimite = sc.nextLine();

        // esse do while gera um nanoID verifica se o código compartilhável gerado ja
        // está presente em alguma lista
        // e caso encontre essa colisão, gera outro código, até passar sem colidir

        String codigo = NanoID.gerarNanoID(10);
        ;
        do {
            codigo = NanoID.gerarNanoID(10);

        } while (PesquisarPorCodigoB(codigo));

        Lista novo = new Lista(0, usuarioLogado.getId(), nome, descricao, dataCriacao, dataLimite, codigo);
        System.out.println("Codigo compartilhavel: " + novo.getCodigoCompartilhavel());

        int id = arquivoLista.create(novo);
        novo.setId(id);
        tree.create(new ParIntInt(usuarioLogado.getId(), id));
    }

    public boolean PesquisarPorCodigoB(String Codigo) throws Exception {

        // cria uma variavel da classe ArquivoLista para acessar os metodos de pesquisa
        arquivoLista = new ArquivoLista();

        Lista listaAux = arquivoLista.read(Codigo); // cria a lista para receber os valores da pesquisa (retorna null
                                                    // caso vazia)

        if (listaAux == null) {
            return false;
        }
        return true;
    }

    public void PesquisarPorCodigo(String Codigo) throws Exception {

        // cria uma variavel da classe ArquivoLista para acessar os metodos de pesquisa
        arquivoLista = new ArquivoLista();

        Lista listaAux = arquivoLista.read(Codigo); // cria a lista para receber os valores da pesquisa (retorna null
                                                    // caso vazia)

        if (listaAux != null) {
            System.out.println("\nLista encontrada!\n");
            listaAux.print();
        } else {
            System.out.println("Lista nao encontrada.");
        }
    }

    public ArrayList<Lista> exibirListas() {
        int idUsuario = usuarioLogado.getId();
        ParIntInt busca = new ParIntInt(idUsuario, -1);
        ArrayList<Lista> listasUsuario = new ArrayList<>();
        int contador = 1;
        try {
            ArrayList<ParIntInt> listaPresentes = ControleLista.tree.read(busca);

            System.out.println("Suas listas:");
            for (ParIntInt par : listaPresentes) {
                int idUser = par.getNum1();
                int idLista = par.getNum2();
                if (idUser == idUsuario) {
                    Lista lista = arquivoLista.read(idLista);
                    listasUsuario.add(lista);
                    System.out.printf("(%d) %s - %s\n", contador, lista.getNome(), lista.getDataLimite());
                    contador++;
                }

            }
        } catch (Exception e) {

        }
        return listasUsuario;
    }

    public static ArrayList<Lista> obterListasUsuario(int idUsuario) throws Exception {

        ArrayList<Lista> listasUsuario = new ArrayList<>();
        ParIntInt busca = new ParIntInt(idUsuario, -1);
        ArrayList<ParIntInt> listaPresentes = tree.read(busca);

        for (ParIntInt par : listaPresentes) {
            int idLista = par.getNum2();
            Lista lista = arquivoLista.read(idLista);
            listasUsuario.add(lista);
        }

        return listasUsuario;
    }

    public void exibirDetalhesLista(ArrayList<Lista> listasUsuario, int escolha, Scanner sc) throws Exception {
        try {
            if (escolha >= 1 && escolha <= listasUsuario.size()) {

                Lista listaSelecionada = listasUsuario.get(escolha - 1);

                listaSelecionada.print();

                System.out.println("\n(1) Alterar dados da lista");
                System.out.println("(2) Excluir lista");
                System.out.println("(0) Retornar ao menu anterior");

                System.out.print("Opção: ");
                String op = sc.nextLine();

                switch (op) {
                    case "1" -> {
                        Painel.alterarDadosLista(sc, listaSelecionada, escolha, listasUsuario);
                        Painel.pausar(sc);
                    }
                    case "2" -> {
                        Painel.excluirLista(sc, listaSelecionada);
                        Painel.pausar(sc);
                    }
                    case "0" -> {

                        Painel.painelMinhasListas(sc);
                    }

                    default -> {
                        System.out.println("Opção inválida!");
                        Painel.pausar(sc);
                    }
                }
            } else {
                System.out.println("Opção inválida!");
                Painel.pausar(sc);
            }
        } catch (NumberFormatException e) {
            System.out.println("Opção inválida!");
        }
    }

    public boolean alterarDadosLista(Scanner sc, Lista lista) {

        boolean resp = false;

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
            if (arquivoLista.update(lista)) {

                resp = true;
            }
        } catch (Exception e) {
            System.out.println("Erro ao atualizar a lista: " + e.getMessage());
        }

        return resp;
    }

    public boolean excluirLista(Scanner sc, Lista lista) {

        boolean resp = false;

        System.out.println("Você tem certeza que deseja excluir a lista \"" + lista.getNome() + "\"? (S/N): ");
        String confirm = sc.nextLine().toUpperCase();

        if (confirm.equals("S")) {
            try {
                // Remove do arquivo
                arquivoLista.delete(lista.getId());

                // Remove da árvore B+
                ParIntInt par = new ParIntInt(lista.getIdUsuario(), lista.getId());
                ControleLista.tree.delete(par);
                resp = true;
            } catch (Exception e) {
                System.out.println("Erro ao excluir a lista: " + e.getMessage());
            }
        } else {
            System.out.println("Exclusão cancelada.");
        }

        return resp;
    }

    public void fechar() throws Exception {
        arquivoLista.close();
    }

    /*
     * // Método main para testes rápidos
     * public static void main(String[] args) {
     * 
     * Scanner sc = new Scanner(System.in);
     * System.out.println("Presente Fácil 1.0\n-----------------\n>Cadastro\n");
     * System.out.println("Digite o nome: ");
     * String nome = sc.nextLine();
     * System.out.println("Digite a descricao: ");
     * String descricao = sc.nextLine();
     * 
     * LocalDate hoje = LocalDate.now();
     * String dataCriacao = hoje.toString();
     * 
     * 
     * System.out.println("Digite a data limite(dd/MM/yyyy): ");
     * String dataLimite = sc.nextLine();
     * 
     * System.out.println("Codigo compartilhavel(Ja sera implementado)");
     * String codigo = sc.nextLine();
     * 
     * try {
     * ControleLista controleLista = new ControleLista();
     * Lista novaLista = controleLista.cadastrarLista(usuarioLogado.getId(), nome,
     * descricao, dataCriacao, dataLimite, codigo);
     * System.out.println(novaLista.toString());
     * controleLista.fechar();
     * } catch (Exception e) {
     * System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
     * }
     * sc.close();
     * }
     */
}
