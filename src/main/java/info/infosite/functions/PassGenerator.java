package info.infosite.functions;

import java.util.Random;

public class PassGenerator {
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
     public String generate(){
         String password ="";
         Random random = new Random(System.nanoTime());
         for (int i = 0; i < 8; i++) {
             password+=CHARS.charAt(random.nextInt(61));
         }
         return password;
     }
}
