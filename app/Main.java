package app;

import arvore.ParIntInt;
import arvore.aed3.ArvoreBMais;
import java.util.Scanner;
import service.ArquivoLista;
import service.ArquivoUsuario;
import view.Painel;



public class Main {
 
	
	// Definir dados
	private static ArquivoUsuario arqUsuario;
	private static ArquivoLista arqLista;
	static ArvoreBMais<ParIntInt> tree;


    
	public static void main(String[] args) throws Exception {
	    arqUsuario = new ArquivoUsuario();
	    arqLista = new ArquivoLista();
		tree = new ArvoreBMais<>(ParIntInt.class.getConstructor(), 5, "arvoreBmais.db");
        Scanner sc = new Scanner(System.in);
	    Painel.tela(null);

        arqLista.close();
        sc.close();  
	    arqUsuario.close();
	  
	}

}
