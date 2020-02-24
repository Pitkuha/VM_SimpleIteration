import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class Main {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void main(String[] args) {
        String otdelitel = ANSI_RED + "===================================================================" + ANSI_RESET;

        Scanner scanner = new Scanner(System.in);

        System.out.println(otdelitel);
        System.out.println("Program started");
        System.out.println(otdelitel);
        String variant;
        while (true) {
            System.out.println("Если вы хотите вводить данные с клавиатуры - введите k;");
            System.out.println("Если вы хотитее ввести данные с помощью файла - введите f;");
            System.out.println(otdelitel);
            System.out.print("Ваш выбор:");

            variant = scanner.nextLine();

            if (variant.equals("k")) {
                System.out.println(otdelitel);
                System.out.print("Введите размерность матрицы:");
                int n = Integer.parseInt(scanner.nextLine());
                double[][] mas = new double[n][n];
                double[] freemas = new double[n];

                System.out.println("Введите коэфиченты и свободные члены");
                System.out.println("Требуется ввести " + n + " строк по " + (n+1) + " значений, разделенных пробелом");
                for (int i = 0; i < n; i++) {
                    String s = scanner.nextLine();
                    double [] numArr = Arrays.stream(s.split(" ")).mapToDouble(Double::parseDouble).toArray();
                    for (int j = 0; j < n; j++) {
                        mas[i][j] = numArr[j];
                    }
                    freemas[i] = numArr[n];
                }

                System.out.print("Введите точность:");
                double t = Double.parseDouble(scanner.nextLine());

                //--------------------------------------------------------
                for (int i = 0; i < mas.length; i++) {
                    for (int j = 0; j < mas[0].length; j++) {
                        System.out.print(ANSI_YELLOW + mas[i][j] + " " + ANSI_RESET);
                    }
                    System.out.println();
                }
                System.out.println(otdelitel);
                System.out.println(t);
                //----------------------------------------------------------
            }
            else if (variant.equals("f")) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("iter.txt"), Charset.forName("UTF-8")));
                    int n = Integer.parseInt(reader.readLine());
                    System.out.println(n);
                } catch (IOException e) {
                    System.out.println(ANSI_RED + "Невозможно извлечь данные из файла" + ANSI_RESET);
                }
            }
            else {
                System.out.println("Введены неправильные данные");
                System.out.println(otdelitel);
            }
        }
    }

    public boolean diagonalDominating(double[][] mas, double[] freemas) {
        double sum = 0;
        boolean flag = true;

        for (int i = 0; i < mas.length; i++) {
            for (int j = 0; j < mas[0].length; j++) {

            }
        }
        return flag;
    }

}
