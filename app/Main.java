package app;

import arvore.*;
import arvore.aed3.ArvoreBMais;
import java.util.Scanner;
import model.*;
import service.ArquivoLista;
import service.ArquivoUsuario;
import view.Painel;

public class Main {

    // Definir dados
    private static ArquivoUsuario arqUsuario;
    private static ArquivoLista arqLista;
    static ArvoreBMais<ParUsuarioLista> tree;

    public static void main(String[] args) throws Exception {
        arqUsuario = new ArquivoUsuario();
        arqLista = new ArquivoLista();
        tree = new ArvoreBMais<>(ParUsuarioLista.class.getConstructor(), 5, "arvoreBmais.db");

        Usuario usuarioLogado = null;
        Scanner sc = new Scanner(System.in);
        char opcao;

		Painel.tela(sc); //Chama a tela inicial
		Painel.limparTelaWindows(); //Limpa a tela
		System.out.println("\nIndo para teste do sistema...");
		
        do {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1 - Criar usuário");
            System.out.println("2 - Simular login");
            System.out.println("3 - Criar Lista");
            System.out.println("S - Sair");
            opcao = sc.nextLine().charAt(0);

            switch (opcao) {
                case '1': {
                    // Ler dados do usuário pelo Scanner
                    System.out.print("Digite o nome: ");
                    String nome = sc.nextLine();
                    System.out.print("Digite o email: ");
                    String email = sc.nextLine();
                    System.out.print("Digite a senha: ");
                    String senha = sc.nextLine();
                    System.out.print("Digite a pergunta de segurança: ");
                    String perg = sc.nextLine();
                    System.out.print("Digite a resposta de segurança: ");
                    String resp = sc.nextLine();

                    // OBS: senha.hashCode() NÃO é seguro em produção!
                    Usuario novo = new Usuario(0, nome, email, senha.hashCode(), perg, resp);
                    int id = arqUsuario.create(novo);
                    System.out.println("Usuário criado com ID: " + id);
                    break;
                }

                case '2': {
                    System.out.print("Digite o email: ");
                    String email = sc.nextLine();
                    System.out.print("Digite a senha: ");
                    String senha = sc.nextLine();
                    int hashSenha = senha.hashCode();

                    usuarioLogado = arqUsuario.read(email);

                    if (usuarioLogado != null && usuarioLogado.getHashSenha() == hashSenha) {
                        System.out.println("Usuário logado: " + usuarioLogado.getNome() + " e ID: " + usuarioLogado.getId());
                    } else {
                        System.out.println("Usuário ou senha inválidos.");
                        usuarioLogado = null;
                    }

                    break;
                }

                case '3': {
                    if (usuarioLogado == null) {
                        System.out.println("Você precisa estar logado para criar uma lista.");
                        break;
                    }

                    int idUsuario = usuarioLogado.getId();

                    System.out.print("Digite o nome da lista: ");
                    String nome = sc.nextLine();

                    System.out.print("Digite a descrição da lista: ");
                    String descricao = sc.nextLine();

                    System.out.print("Digite a data de criação (dd/mm/yyyy): ");
                    String dataCriacao = sc.nextLine();

                    System.out.print("Digite a data limite (dd/mm/yyyy): ");
                    String dataLimite = sc.nextLine();

                    System.out.print("Digite o código compartilhável: ");
                    String codigoCompartilhavel = sc.nextLine();

                    Lista lista = new Lista(0, idUsuario, nome, descricao, dataCriacao, dataLimite, codigoCompartilhavel);
                    int id = arqLista.create(lista);
                    lista.setId(id);

                    ParUsuarioLista par = new ParUsuarioLista(usuarioLogado.getId(), lista.getId());
                    tree.create(par);

                    System.out.println("Par Usuario: " + par.getIdUsuario() + " e endereço: " + par.getEnderecoLista());
                    System.out.println("Lista de número " + lista.getId() + " criada pelo usuário " + usuarioLogado.getEmail());

                    tree.print();

                    break;
                }

                case 'S':
                case 's': {
                    System.out.println("Encerrando programa!");
                    break;
                }

                default: {
                    System.out.println("Opção inválida!");
                    break;
                }
            }

        } while (opcao != 'S' && opcao != 's');

        sc.close();
        arqUsuario.close();
    }
}
