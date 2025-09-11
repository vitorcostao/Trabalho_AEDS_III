package app;

import arvore.*;
import arvore.aed3.ArvoreBMais;
import java.util.Scanner;
import model.*;
import service.ArquivoLista;
import service.ArquivoUsuario;



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

	    do {
	        System.out.println("\nEscolha uma opção:");
	        System.out.println("1 - Criar usuário");
	        System.out.println("2 - Simular login");
	        System.out.println("3 - Criar Lista");
	        System.out.println("S - Sair");
	        opcao = sc.nextLine().charAt(0);

	        switch (opcao) {
	            case '1' -> {
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

	                Usuario novo = new Usuario(0, nome, email, senha.hashCode(), perg, resp);
	                int id = arqUsuario.create(novo);
	                System.out.println("Usuário criado com ID: " + id);
	            }

	            case '2' -> {
	                // Ler dados do login
	                System.out.print("Digite o email: ");
	                String email = sc.nextLine();
	                System.out.print("Digite a senha: ");
	                String senha = sc.nextLine();
	                int hashSenha = senha.hashCode();

	                usuarioLogado = arqUsuario.read(email);
	                if (usuarioLogado != null && usuarioLogado.getHashSenha() == hashSenha) {
	                    System.out.println("Usuário logado: " + usuarioLogado.getNome() + "e id: " + usuarioLogado.getId());
	                } else {
	                    System.out.println("Usuário ou senha inválidos.");
	                }
	            }

	            case 'S', 's' -> System.out.println("Encerrando programa!");
	            default -> System.out.println("Opção inválida!");
	        }

	    } while (opcao != 'S' && opcao != 's');

	    sc.close();
	    arqUsuario.close();
	  
	}

}
