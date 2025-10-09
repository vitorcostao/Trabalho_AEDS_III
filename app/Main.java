package app;

import java.util.Scanner;
import view.Painel;

public class Main {

	public static void main(String[] args) throws Exception {

		try (Scanner sc = new Scanner(System.in)) {
			Painel.tela(sc);
		}
	}
}