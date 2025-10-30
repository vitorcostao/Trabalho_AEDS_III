package controle;

import service.Genérico.Árvore.ArvoreBMais;
import service.Genérico.Árvore.ParIntInt;

import java.io.File;
import java.time.LocalDate;
import java.util.*;
import model.Lista;
import model.Usuario;
import service.Listas.ArquivoLista;
import service.Listas.NanoID;

public class ControleLista {

    private static ArquivoLista arquivoLista;
    public static ArvoreBMais<ParIntInt> tree;
    public Usuario usuarioLogado;

    public ControleLista() throws Exception {
        arquivoLista = new ArquivoLista();
        File pastaArvore = new File("dados\\listas");
        if (!pastaArvore.exists())
            pastaArvore.mkdirs();
        tree = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 5, "dados\\listas\\arvoreBmais.db");
    }

    public ControleLista(Usuario usuarioLogado) throws Exception {
        arquivoLista = new ArquivoLista();
        this.usuarioLogado = usuarioLogado;
        File pastaArvore = new File("dados\\listas");
        if (!pastaArvore.exists())
            pastaArvore.mkdirs();
        tree = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 5, "dados\\listas\\arvoreBmais.db");
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

        // esse do while gera um nanoID verifica se o código compartilhável gerado já
        // está presente em alguma lista
        // e caso encontre essa colisão, gera outro código, até passar sem colidir

        String Codigo;

        do {
            Codigo = NanoID.gerarNanoID(10);

        } while (PesquisarPorCodigoB(Codigo));

        Lista novo = new Lista(0, usuarioLogado.getId(), nome, descricao, dataCriacao, dataLimite, Codigo);
        System.out.println("Codigo compartilhavel: " + novo.getCodigoCompartilhavel());

        int id = arquivoLista.create(novo);
        novo.setId(id);
        tree.create(new ParIntInt(usuarioLogado.getId(), id));
    }

    public boolean PesquisarPorCodigoB(String codigo) throws Exception {

        // cria uma variavel da classe ArquivoLista para acessar os metodos de pesquisa
        arquivoLista = new ArquivoLista();

        Lista listaAux = arquivoLista.read(codigo); // cria a lista para receber os valores da pesquisa (retorna null
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
                    System.out.printf("(%d) %s - %s \n", contador, lista.getNome(), lista.getDataLimite());
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

}
