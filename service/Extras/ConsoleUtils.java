package service.Extras;

//classe limpa a tela do console gerando assim um programa mais limpo
public class ConsoleUtils {
    public static void limparConsole() {
        try {
            String sistema = System.getProperty("os.name").toLowerCase();

            if (sistema.contains("win")) {
                // Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Linux e macOS
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (Exception e) {
            // Se não conseguir limpar, imprime várias quebras de linha
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
}