package service;

import java.util.Random;

public class NanoID {
    private static final String ALFABETO = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_-";
    private static final Random random = new Random();

    public static String gerarNanoID(int tamanho) {
        StringBuilder id = new StringBuilder(tamanho);
        for (int i = 0; i < tamanho; i++) {
            int index = random.nextInt(ALFABETO.length());
            id.append(ALFABETO.charAt(index));
        }
        return id.toString();
    }
}
