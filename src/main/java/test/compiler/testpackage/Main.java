package test.compiler.testpackage;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello Compiler!!");
        Scanner scanner = new Scanner(System.in);
        String str1 = scanner.nextLine();
        String str2 = scanner.nextLine();
        Integer int1 = scanner.nextInt();
        Integer int2 = scanner.nextInt();
        System.out.println(str1+str2);
        System.out.println(int1+int1*int2);
        scanner.close();
    }
}
