package app;

import java.util.Scanner;
import service.ArquivoUsuario;
import service.ArquivoLista;
import model.*;
import service.ParEmailEndereco;


public class Main {
 
	
	// Definir dados
	private static ArquivoUsuario arqUsuario;
	private static ArquivoLista arqLista;

    
	public static void main(String[] args) throws Exception {
        arqUsuario = new ArquivoUsuario();
        arqLista = new ArquivoLista();
        Usuario usuarioLogado;
        
        Scanner sc = new Scanner(System.in);
        char opcao;

        do {
            System.out.println("\nEscolha uma opção:");
            System.out.println("1 - Criar usuário");
            System.out.println("2 - Simular login");
            System.out.println("S - Sair");
            opcao = sc.nextLine().charAt(0);

            switch (opcao) {
                case '1' -> {
                    // Criar e salvar usuário no arquivo
                    String email = "aaaa@abc.com";
                    String senha = "aaaa123";
                    String perg = "a";
                    String resp = "a";
                    
                    Usuario novo = new Usuario(0, "aaaa", email, senha.hashCode(), perg, resp);
                    int id = arqUsuario.create(novo);
                    System.out.println("Usuário criado com ID: " + id);
                }

                case '2' -> {
                    // Simular login de usuário
                	String email = "aaaa@abc.com";
                    String senha = "aaaa123";
                    int hashSenha = senha.hashCode();

                    // Buscar usuário pelo e-mail (ou ID se tiver)
                    usuarioLogado = arqUsuario.read(email);
                    if (usuarioLogado != null && usuarioLogado.hashSenha == hashSenha) {
                        System.out.println("Usuário logado: " + usuarioLogado.nome);
                        
                        // Exemplo: criar lista para o usuário logado
                        Lista lista = new Lista(0, usuarioLogado.getId(), "Lista Aniversário", "Presentes do aniversário", "2025-09-01", "2025-09-30", "ABC123");
                        int idLista = arqLista.create(lista, usuarioLogado);
                        System.out.println("Lista criada com ID: " + idLista);
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
        arqLista.close();
    }
}
