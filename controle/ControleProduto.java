package controle;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import model.Produto;
import model.Usuario;
import service.ListaProduto.ArquivoListaProduto;
import service.Produtos.ArquivoProduto;
import service.Genérico.IndiceInvertido.*;
import java.lang.Math;

public class ControleProduto {

    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "o", "a", "os", "as", "um", "uma", "uns", "umas",
            "de", "do", "da", "dos", "das", "em", "no", "na", "nos", "nas",
            "por", "pelo", "pela", "pelos", "pelas", "para", "com", "sem", "sob",
            "sobre", "entre", "até", "após", "contra", "ante", "via",
            "eu", "tu", "ele", "ela", "eles", "elas", "nós", "vós",
            "me", "te", "se", "lhe", "nos", "vos",
            "meu", "minha", "meus", "minhas", "teu", "tua", "teus", "tuas",
            "nosso", "nossa", "nossos", "nossas", "vosso", "vossa", "vossos", "vossas",
            "este", "esta", "estes", "estas", "esse", "essa", "esses", "essas",
            "aquele", "aquela", "aqueles", "aquelas", "isso", "isto", "aquilo",
            "quem", "qual", "quais", "cujo", "cuja", "cujos", "cujas", "onde",
            "um", "dois", "três", "quatro", "cinco", "seis", "sete", "oito", "nove", "dez",
            "primeiro", "segundo", "terceiro", "quarto", "quinto", "sexto", "sétimo", "oitavo", "nono", "décimo"));

    private static ListaInvertida indiceInvertido;
    private static ArquivoProduto arquivoProduto;
    private static ArquivoListaProduto arquivoListaProduto;
    private static Usuario usuarioLogado;

    public ControleProduto(Usuario usuarioLogado) throws Exception {

        arquivoProduto = new ArquivoProduto();
        arquivoListaProduto = new ArquivoListaProduto();
        ControleProduto.usuarioLogado = usuarioLogado;
        indiceInvertido = new ListaInvertida(5, "dicionario.db", "blocos.db");
    }

    public ArquivoProduto getArquivoProduto() {
        return arquivoProduto;
    }

    public ArquivoListaProduto getArquivoListaProduto() {
        return arquivoListaProduto;
    }

    public ListaInvertida getIndiceInvertido() {

        return indiceInvertido;
    }

    public static String gerarGTIN13() {
        StringBuilder sb = new StringBuilder(13);
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            int d = ThreadLocalRandom.current().nextInt(10);
            sb.append(d);
            soma += (i % 2 == 0) ? d : 3 * d;
        }
        int dv = (10 - (soma % 10)) % 10;
        sb.append(dv);
        return sb.toString();
    }

    public Produto cadastrarProduto(String nome, String descricao) throws Exception {

        String gtin;
        do {
            gtin = gerarGTIN13();

        } while (arquivoProduto.read(gtin) != null);

        Produto novo = new Produto(-1, nome, gtin, descricao);
        int id = arquivoProduto.create(novo);
        novo.setId(id);

        String nomeNormalizado = Normalizer.normalize(nome, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase();

        String[] palavras = nomeNormalizado.split("\\s+");
        List<String> palavrasFiltradas = new ArrayList<>();
        Map<String, Integer> contagem = new HashMap<>();

        for (String p : palavras) {

            if (!STOP_WORDS.contains(p) && !p.isBlank()) {
                palavrasFiltradas.add(p);
                contagem.put(p, contagem.getOrDefault(p, 0) + 1);
            }
        }

        int totalPalavras = palavrasFiltradas.size();

        for (String palavra : contagem.keySet()) {

            int ocorrencias = contagem.get(palavra);
            float frequencia = (float) ocorrencias / totalPalavras;
            indiceInvertido.create(palavra, new ElementoLista(id, frequencia));
        }

        return novo;
    }

    public boolean atualizarProduto(Scanner sc, int idProduto) throws Exception {
        Produto produtoAntigo = arquivoProduto.read(idProduto);
        if (produtoAntigo == null) {
            System.out.println("Produto não encontrado.");
            return false;
        }

        System.out.print("Novo nome (deixe em branco para manter): ");
        String nome = sc.nextLine();
        if (!nome.isEmpty())
            produtoAntigo.setNome(nome);

        System.out.print("Nova descrição (deixe em branco para manter): ");
        String descricao = sc.nextLine();
        if (!descricao.isEmpty())
            produtoAntigo.setDescricao(descricao);

        return arquivoProduto.update(produtoAntigo);
    }

    public boolean desativar(Scanner sc, int idProduto) throws Exception {
        Produto produtoAntigo = arquivoProduto.read(idProduto);
        produtoAntigo.Desativar();
        return arquivoProduto.update(produtoAntigo);
    }

    public boolean reativar(Scanner sc, int idProduto) throws Exception {
        Produto produtoAntigo = arquivoProduto.read(idProduto);
        produtoAntigo.Reativar();
        return arquivoProduto.update(produtoAntigo);
    }

    public void fechar() throws Exception {
        arquivoProduto.close();
        arquivoListaProduto.close();
    }

    public ArrayList<Produto> buscarProdutoPorNome(String nome) throws Exception {

        nome = Normalizer.normalize(nome, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
        String[] termosBusca = nome.split(" ");

        ArrayList<Produto> l = arquivoProduto.readAll();
        int total = l.size();

        ArrayList<ElementoLista> resultados = new ArrayList<>();
        HashMap<Integer, Float> mapaRelevancia = new HashMap<>();
        ArrayList<Produto> produtosOrdenados = new ArrayList<>();

        for (String termo : termosBusca) {

            ElementoLista[] lista = indiceInvertido.read(termo);
            float idfPalavra = (float) (Math.log((float) total / lista.length) + 1);

            if (lista.length > 0) {

                for (ElementoLista el : lista) {

                    float tfidf = el.getFrequencia() * idfPalavra;
                    mapaRelevancia.put(el.getId(), mapaRelevancia.getOrDefault(el.getId(), 0f) + tfidf);
                }
            }
        }

        ArrayList<Integer> ids = new ArrayList<>(mapaRelevancia.keySet());

        Collections.sort(ids, new Comparator<Integer>() {
            public int compare(Integer id1, Integer id2) {
                return Float.compare(mapaRelevancia.get(id2), mapaRelevancia.get(id1));
            }
        });

        for (int id : ids) {
            produtosOrdenados.add(arquivoProduto.read(id));
        }

        return produtosOrdenados;
    }

    public Produto buscarProdutoPorGTIN(String gtin) throws Exception {

        return arquivoProduto.read(gtin);
    }

    public ArrayList<Produto> listarProdutosOrdenados() throws Exception {
        ArrayList<Produto> produtos = arquivoProduto.readAll();
        produtos.sort((a, b) -> a.getNome().compareToIgnoreCase(b.getNome()));
        return produtos;
    }
}
