import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import static java.lang.Math.*;

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
    public static final String otdelitel = ANSI_RED + "===================================================================" + ANSI_RESET;

    static void prettyPrint(double[] arr) {
        for (double ar : arr) System.out.printf(Locale.US, "%.6f\t\t", ar);
        System.out.println();
    }
    static void prettyPrint(double[][] arr){
        for (double[] ar : arr) {
            for (double v : ar) System.out.printf(Locale.US, "%.6f\t\t", v);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(otdelitel);
        System.out.println("Program started");
        String variant;
        while (true) {
            System.out.println(otdelitel);
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
                double acc = Double.parseDouble(scanner.nextLine());

                if ((mas = diagonal_prevalence(mas, n)) != null) {
                    System.out.println(otdelitel);
                    System.out.println(ANSI_BLUE + "Преобразованная матрица" + ANSI_RESET);
                    prettyPrint(mas);
                    System.out.println();
                    double[] x = simple_iter(mas, freemas, n, acc);
                    System.out.print("Вектор неизвестных: ");
                    prettyPrint(x);
                } else {
                    System.out.println(ANSI_RED + "Невозможно осуществеить диагональное преобладание" + ANSI_RESET);
                }

            }
            else if (variant.equals("f")) {
                try {
                    //BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("iter.txt"), Charset.forName("UTF-8")));
                    BufferedReader reader = new BufferedReader(new FileReader("./src/iter.txt"));
                    int n = Integer.parseInt(reader.readLine());
                    double[][] mas = new double[n][n];
                    double[] freemas = new double[n];

                    for (int i = 0; i < n; i++) {
                        String s = reader.readLine();
                        double [] numarr = Arrays.stream(s.split(" ")).mapToDouble(Double::parseDouble).toArray();
                        for (int j = 0; j < n; j++) {
                        mas[i][j] = numarr[j];
                        }
                        freemas[i] = numarr[n];
                    }
                    double acc = Double.parseDouble(reader.readLine());

                    if ((mas = diagonal_prevalence(mas, n)) != null) {
                        prettyPrint(mas);
                        System.out.println();
                        double[] x = simple_iter(mas, freemas, n, acc);
                        System.out.print("Вектор неизвестных: ");
                        prettyPrint(x);
                    } else {
                        System.out.println(ANSI_RED + "Невозможно осуществеить диагональное преобладание" + ANSI_RESET);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(ANSI_RED + "Невозможно извлечь данные из файла" + ANSI_RESET);

                }
            }
            else {
                System.out.println("Введены неправильные данные");
            }
        }
    }
    static double[][] diagonal_prevalence(double[][] a, int n) {
        int[] unique_max_columns = new int[n];

        //search max elements
        for (int i = 0; i < n; i++) {
            int max_j = 0;
            double row_sum = a[i][0];
            for (int j = 1; j < n; j++) {
                if (a[i][j] > a[i][max_j]) max_j = j;
                row_sum += Math.abs(a[i][j]);
            }
            if (a[i][max_j] * 2 < row_sum) return null;
            unique_max_columns[i] = max_j;
        }

        //check for duplicates
        Set<Integer> s = new HashSet<>();
        for (int i : unique_max_columns) {
            if (s.contains(i)) return null;
            s.add(i);
        }

        //replacing
        int i = 0;
        double[][] z = new double[n][n];
        for (int col_num : unique_max_columns) {
            for (int j = 0; j < n; j++) {
                z[j][i] = a[j][col_num];
            }
            i++;
        }
        /**
         * @param z преобразованный масив. Привели к нормальному виду
         */
        return z;
    }

    /**
     *
     * @param a матрица А
     * @param b свободные коэффиценты
     * @param n размер матрицы
     * @param acc точность
     * @return значение n "иксов"
     */
    static double[] simple_iter(double[][] a, double[] b, int n, double acc) {
        //массив неизвестных
        double[] x = new double[n];
        //преобразованный массив A, как в методе
        double[][] c = new double[n][n];
        //преобразованные коэффиценты
        double[] d = new double[n];

        //initialization
        for (int i = 0; i < n; i++) {
            d[i] = b[i]/a[i][i];
            x[i] = d[i];

            for (int j = 0; j < n; j++) {
                c[i][j] = -a[i][j]/a[i][i];
                c[i][i] = 0;
            }
        }

        double last_acc = acc+1;
        int iter_cout = 1;
        //если x - это текущая итерация, x1 -следующая итерация
        double[] x1 = new double[n];

        System.out.print("0. "); prettyPrint(x);
        for (; last_acc > acc; iter_cout++) {
            last_acc = -1;
            for (int i = 0; i < n; i++) {
                x1[i] = d[i];
                for (int j = 0; j < n; j++) {
                    x1[i] += c[i][j]*x[j];
                }
                last_acc = Math.max(last_acc, Math.abs(x1[i] - x[i]));
            }
            System.out.printf("%d. ", iter_cout); prettyPrint(x1);
            System.out.printf("Погрешность: %.6f\n\n",last_acc);
            x = x1.clone();
        }
        return x;
    }
}
