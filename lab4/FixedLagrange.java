package lab4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FixedLagrange {

    // Метод для вычисления базисного полинома Лагранжа l_j(x)
    public static List<Double> computeBasisPolynomial(List<Double> x, int j) {
        int n = x.size();
        // Создаем список коэффициентов полинома (изначально все нули)
        List<Double> coefficients = new ArrayList<>(Collections.nCopies(n, 0.0));
        // Начинаем с полинома 1 (коэффициент при x^0 = 1)
        coefficients.set(0, 1.0);

        // Построение полинома через произведение (x - x_m) для всех m != j
        for (int m = 0; m < n; ++m) {
            if (m != j) {
                // Сохраняем текущие коэффициенты во временный список
                List<Double> temp = new ArrayList<>(coefficients);
                
                // Умножение текущего полинома на (x - x_m) с использованием схемы Горнера
                for (int k = 1; k < n; ++k) {
                    coefficients.set(k, temp.get(k - 1) - x.get(m) * temp.get(k));
                }
                coefficients.set(0, -x.get(m) * temp.get(0));
                
                // Деление на (x_j - x_m) для нормализации
                double denominator = x.get(j) - x.get(m);
                for (int k = 0; k < n; ++k) {
                    coefficients.set(k, coefficients.get(k) / denominator);
                }
            }
        }
        return coefficients;
    }

    // Метод для красивого вывода полинома
    public static void printPolynomial(List<Double> coefficients) {
        int n = coefficients.size();
        // Переворачиваем список для удобства (от старшей степени к младшей)
        List<Double> reversed = new ArrayList<>(coefficients);
        Collections.reverse(reversed);
        
        System.out.print("L(x) = ");
        boolean firstTerm = true;
        
        // Перебор всех коэффициентов полинома
        for (int i = 0; i < n; ++i) {
            double coeff = reversed.get(i);
            // Пропускаем очень маленькие коэффициенты (почти нули)
            if (Math.abs(coeff) > 1e-10) {
                // Обработка знака перед членом
                if (!firstTerm) {
                    System.out.print(coeff >= 0 ? " + " : " - ");
                } else if (coeff < 0) {
                    System.out.print("-");
                }
                
                // Вывод абсолютного значения коэффициента
                double absCoeff = Math.abs(coeff);
                // Не выводим 1 перед x^n (кроме свободного члена)
                if (Math.abs(absCoeff - 1.0) > 1e-10 || i == n-1) {
                    System.out.print(roundCoefficient(absCoeff));
                }
                
                // Вывод переменной x и степени
                if (i < n-1) {
                    System.out.print("x");
                    if (i < n-2) {
                        System.out.print("^" + (n-1-i));
                    }
                }
                firstTerm = false;
            }
        }
        System.out.println();
    }

    // Вспомогательный метод для округления коэффициентов
    private static String roundCoefficient(double coeff) {
        // Округление до 5 знаков после запятой
        double rounded = Math.round(coeff * 100000) / 100000.0;
        // Убираем .0 для целых чисел
        return rounded == (long)rounded ? String.format("%d", (long)rounded) : String.format("%.5f", rounded);
    }

    // Метод для вычисления значения полинома в точке x (схема Горнера)
    public static double evaluatePolynomial(List<Double> coefficients, double x) {
        double result = 0.0;
        for (int i = coefficients.size() - 1; i >= 0; i--) {
            result = result * x + coefficients.get(i);
        }
        return result;
    }

    public static void main(String[] args) {
        // Узлы интерполяции
        List<Double> x = List.of(1.0, 2.0, 3.0, 4.0, 5.0);
        // Значения функции в узлах
        List<Double> y = List.of(0.25, 0.0, 0.07, 0.0, 0.04);
        int n = x.size();
        // Финальные коэффициенты интерполяционного полинома
        List<Double> finalCoefficients = new ArrayList<>(Collections.nCopies(n, 0.0));

        // Вычисляем интерполяционный полином как сумму базисных полиномов
        for (int j = 0; j < n; ++j) {
            List<Double> basisPoly = computeBasisPolynomial(x, j);
            // Умножаем каждый базисный полином на y_j и складываем
            for (int k = 0; k < n; ++k) {
                finalCoefficients.set(k, finalCoefficients.get(k) + y.get(j) * basisPoly.get(k));
            }
        }

        // Выводим полученный полином
        printPolynomial(finalCoefficients);

        // Проверка - вычисляем значения полинома в узлах интерполяции
        System.out.println("\nПроверка:");
        for (int i = 0; i < n; i++) {
            double val = evaluatePolynomial(finalCoefficients, x.get(i));
            System.out.printf("L(%.1f) = %.10f", x.get(i), val);
        }

        double L_at_0 = evaluatePolynomial(finalCoefficients, 1.0);
        System.out.printf("\nL(0) = %.2f", L_at_0);
    }
}
