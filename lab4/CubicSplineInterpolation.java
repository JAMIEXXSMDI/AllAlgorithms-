package lab4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class CubicSplineInterpolation {
    // Набор математических функций для интерполяции
    private static double func1(double x) { return Math.exp(x); }
    private static double func2(double x) { return Math.sinh(x); }
    private static double func3(double x) { return Math.cosh(x); }
    private static double func4(double x) { return Math.sin(x); }
    private static double func5(double x) { return Math.cos(x); }
    private static double func6(double x) { return x > 0 ? Math.log(x) : Double.NaN; } // Защита от отрицательных значений
    private static double func7(double x) { return Math.exp(-x); }

    public static void main(String[] args) {
        // Параметры интерполяции
        final double a = 1.00;  // Начало интервала
        final double b = 1.20;  // Конец интервала
        final double h = 0.04;  // Шаг между узлами
        
        // Создание равномерной сетки узлов
        List<Double> xValues = createUniformGrid(a, b, h);

        // Выбор функции пользователем
        int functionChoice = selectFunction();

        // Проверка корректности выбора для ln(x)
        if (functionChoice == 6 && a <= 0) {
            System.out.println("Ошибка: для ln(x) начальное значение должно быть > 0");
            return;
        }

        // Получение выбранной функции
        Function<Double, Double> selectedFunction = getFunction(functionChoice);
        
        // Вычисление значений функции в узлах
        List<Double> yValues = calculateFunctionValues(xValues, selectedFunction);

        // Построение сплайна
        List<List<Double>> splineCoefficients = buildCubicSpline(xValues, yValues);

        // Точки для интерполяции
        List<Double> evaluationPoints = List.of(1.03, 1.09, 1.13, 1.15, 1.17);
        
        // Вывод результатов
        printInterpolationResults(evaluationPoints, xValues, splineCoefficients, selectedFunction);
    }

    // Создание равномерной сетки узлов
    private static List<Double> createUniformGrid(double start, double end, double step) {
        List<Double> grid = new ArrayList<>();
        for (double x = start; x <= end + 1e-9; x += step) {
            grid.add(x);
        }
        return grid;
    }

    // Выбор функции пользователем
    private static int selectFunction() {
        System.out.println("Выберите функцию для интерполяции:");
        System.out.println("1. e^x");
        System.out.println("2. sinh(x)");
        System.out.println("3. cosh(x)");
        System.out.println("4. sin(x)");
        System.out.println("5. cos(x)");
        System.out.println("6. ln(x)");
        System.out.println("7. e^-x");

        Scanner scanner = new Scanner(System.in);
        try {
            int choice = scanner.nextInt();
            if (choice < 1 || choice > 7) {
                System.err.println("Ошибка: выберите число от 1 до 7");
                System.exit(1);
            }
            return choice;
        } catch (Exception e) {
            System.err.println("Ошибка: введите целое число от 1 до 7");
            System.exit(1);
        } finally {
            scanner.close();
        }
        return 0; // Недостижимый код
    }

    // Получение выбранной функции
    private static Function<Double, Double> getFunction(int choice) {
        List<Function<Double, Double>> functions = List.of(
            CubicSplineInterpolation::func1,
            CubicSplineInterpolation::func2,
            CubicSplineInterpolation::func3,
            CubicSplineInterpolation::func4,
            CubicSplineInterpolation::func5,
            CubicSplineInterpolation::func6,
            CubicSplineInterpolation::func7
        );
        return functions.get(choice - 1);
    }

    // Вычисление значений функции в узлах
    private static List<Double> calculateFunctionValues(List<Double> xValues, 
                                                     Function<Double, Double> function) {
        List<Double> yValues = new ArrayList<>();
        for (double x : xValues) {
            double value = function.apply(x);
            if (Double.isNaN(value)) {
                System.err.println("Ошибка: функция не определена для x = " + x);
                System.exit(1);
            }
            yValues.add(value);
        }
        return yValues;
    }

    // Построение кубического сплайна
    private static List<List<Double>> buildCubicSpline(List<Double> x, List<Double> y) {
        int n = x.size() - 1;
        List<Double> h = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            h.add(x.get(i + 1) - x.get(i));
        }

        // Подготовка системы уравнений для моментов
        List<Double> a = new ArrayList<>();
        List<Double> b = new ArrayList<>();
        List<Double> c = new ArrayList<>();
        List<Double> d = new ArrayList<>();

        for (int i = 1; i < n; i++) {
            a.add(h.get(i-1) / 6.0);
            b.add((h.get(i-1) + h.get(i)) / 3.0);
            c.add(h.get(i) / 6.0);
            d.add((y.get(i+1) - y.get(i)) / h.get(i) - 
                 (y.get(i) - y.get(i-1)) / h.get(i-1));
        }

        // Решение системы методом прогонки
        List<Double> m = solveTridiagonalSystem(a, b, c, d);
        
        // Добавление граничных условий (естественный сплайн)
        m.add(0, 0.0);
        m.add(0.0);

        // Вычисление коэффициентов сплайна
        List<List<Double>> coefficients = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Double> coef = new ArrayList<>();
            coef.add(y.get(i)); // a
            coef.add((y.get(i+1) - y.get(i)) / h.get(i) - 
                    h.get(i) * (m.get(i+1) + 2*m.get(i)) / 6.0); // b
            coef.add(m.get(i) / 2.0); // c
            coef.add((m.get(i+1) - m.get(i)) / (6.0 * h.get(i))); // d
            coefficients.add(coef);
        }

        return coefficients;
    }

    // Метод прогонки для трехдиагональной системы
    private static List<Double> solveTridiagonalSystem(List<Double> a, List<Double> b, 
                                                     List<Double> c, List<Double> d) {
        int n = d.size();
        double[] cp = new double[n];
        double[] dp = new double[n];
        List<Double> x = new ArrayList<>(Collections.nCopies(n, 0.0));

        // Прямой ход
        cp[0] = c.get(0) / b.get(0);
        dp[0] = d.get(0) / b.get(0);
        
        for (int i = 1; i < n; i++) {
            double denom = b.get(i) - a.get(i-1) * cp[i-1];
            cp[i] = c.get(i) / denom;
            dp[i] = (d.get(i) - a.get(i-1) * dp[i-1]) / denom;
        }

        // Обратный ход
        x.set(n-1, dp[n-1]);
        for (int i = n-2; i >= 0; i--) {
            x.set(i, dp[i] - cp[i] * x.get(i+1));
        }

        return x;
    }

    // Вычисление значения сплайна в точке
    private static double evaluateSpline(double x, List<Double> xValues, 
                                      List<List<Double>> coefficients) {
        int n = xValues.size() - 1;
        int i = 0;
        while (i < n && x > xValues.get(i + 1)) {
            i++;
        }
        
        double dx = x - xValues.get(i);
        List<Double> coef = coefficients.get(i);
        return coef.get(0) + coef.get(1)*dx + coef.get(2)*dx*dx + coef.get(3)*dx*dx*dx;
    }

    // Вывод результатов интерполяции
    private static void printInterpolationResults(List<Double> points, 
                                               List<Double> xValues,
                                               List<List<Double>> coefficients,
                                               Function<Double, Double> function) {
        System.out.println("\nРезультаты интерполяции:");
        System.out.println("x\t\tСплайн\t\tТочное\t\tПогрешность");
        System.out.println("----------------------------------------------");
        
        for (double x : points) {
            double splineValue = evaluateSpline(x, xValues, coefficients);
            double exactValue = function.apply(x);
            double error = Math.abs(splineValue - exactValue);
            
            System.out.printf("%.2f\t\t%.6f\t%.6f\t%.6f%n", 
                            x, splineValue, exactValue, error);
        }
    }
}