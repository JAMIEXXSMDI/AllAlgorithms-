// Класс для решения системы нелинейных уравнений методом Ньютона-Рафсона
public class NewtonRaphsonSystem {
    
    // Константы:
    private static final double EPSILON = 1e-6;  // Точность решения - критерий остановки, когда изменения становятся меньше этого значения
    private static final int MAX_ITER = 100;     // Максимальное число итераций - защита от бесконечного цикла

    // Первое уравнение системы: sin(y + 0.5) - x - 1 = 0
    private static double f1(double x, double y) {
        return Math.sin(y + 0.5) - x - 1;
    }

    // Второе уравнение системы: cos(x - 2) + y = 0
    private static double f2(double x, double y) {
        return Math.cos(x - 2) + y - 0;  
    }

    // Численная производная f1 по x (аппроксимация)
    private static double df1_dx(double x, double y, double h) {
        return (f1(x + h, y) - f1(x, y)) / h;
    }

    // Численная производная f1 по y 
    private static double df1_dy(double x, double y, double h) {
        return (f1(x, y + h) - f1(x, y)) / h;
    }

    // Численная производная f2 по x
    private static double df2_dx(double x, double y, double h) {
        return (f2(x + h, y) - f2(x, y)) / h;
    }

    // Численная производная f2 по y
    private static double df2_dy(double x, double y, double h) {
        return (f2(x, y + h) - f2(x, y)) / h;
    }

    // Решение системы линейных уравнений методом Гаусса с выбором ведущего элемента
    private static double[] solveLinearSystem(double[][] A, double[] B) {
        int n = B.length;  // Размер системы (в нашем случае n=2)

        // Прямой ход метода Гаусса (приведение к верхнетреугольному виду)
        for (int i = 0; i < n; i++) {
            // Поиск строки с максимальным элементом в текущем столбце (частичный поворот)
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(A[k][i]) > Math.abs(A[maxRow][i])) {
                    maxRow = k;
                }
            }

            // Перестановка строк для улучшения устойчивости (избегаем деления на малые числа)
            double[] tempRow = A[i];
            A[i] = A[maxRow];
            A[maxRow] = tempRow;
            
            double tempVal = B[i];
            B[i] = B[maxRow];
            B[maxRow] = tempVal;

            // Обнуление элементов ниже главной диагонали
            for (int k = i + 1; k < n; k++) {
                double factor = A[k][i] / A[i][i];  // Множитель для обнуления
                B[k] -= factor * B[i];             // Модификация правой части
                for (int j = i; j < n; j++) {
                    A[k][j] -= factor * A[i][j];    // Модификация матрицы
                }
            }
        }

        // Обратный ход метода Гаусса (нахождение решения)
        double[] X = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            X[i] = B[i];  // Начинаем с правой части
            for (int j = i + 1; j < n; j++) {
                X[i] -= A[i][j] * X[j];  // Вычитаем уже найденные компоненты решения
            }
            X[i] /= A[i][i];  // Деление на диагональный элемент
        }

        return X;  // Возвращаем решение системы
    }

    // Метод Ньютона-Рафсона для решения системы уравнений сводит нелинейную задачу к серии линейных (через линеаризацию)
    public static double[] newtonRaphson(double x0, double y0) {
        double x = x0, y = y0;  // Начальное приближение
        int iter = 0;           // Счетчик итераций
        double h = 1e-7;        // Шаг для численного дифференцирования

        // Основной итерационный процесс
        while (iter < MAX_ITER) {
            // 1. Вычисление матрицы Якоби. Это матрица всех частных производных первого порядка. Для системы из 2 уравнений с 2 переменными
            double[][] J = {
                {df1_dx(x, y, h), df1_dy(x, y, h)},  
                {df2_dx(x, y, h), df2_dy(x, y, h)}    
            };

            // 2. Вычисление вектора невязки (-F) это "упаковка" всех уравнений вашей системы в один математический объект.
            double[] F = {-f1(x, y), -f2(x, y)};  // Минус значения функций в текущей точке

            // 3. Решение линейной системы J*Δ = F для нахождения поправки
            double[] delta = solveLinearSystem(J, F);

            // 4. Обновление решения: x_new = x_old + Δx
            x += delta[0];
            y += delta[1];

            // 5. Проверка условия сходимости (малость поправки)
            if (Math.abs(delta[0]) < EPSILON && Math.abs(delta[1]) < EPSILON) {
                System.out.println("Решение достигнуто на итерации: " + iter);
                break;
            }

            iter++;  // Увеличиваем счетчик итераций
            
        }


        return new double[]{x, y};  // Возвращаем найденное решение
    }

    public static void main(String[] args) {
        // Начальное приближение 
        double x0 = 0.0, y0 = 0.0;
    
        // Решение системы методом Ньютона-Рафсона
        double[] solution = newtonRaphson(x0, y0);

        // Вывод решения с 6 знаками после запятой
        System.out.printf("Решение: x = %.6f, y = %.6f%n", solution[0], solution[1]);

        // Проверка точности решения путем подстановки в исходные уравнения
        double f1Value = f1(solution[0], solution[1]);
        double f2Value = f2(solution[0], solution[1]);
        
        System.out.printf("Проверка: f1(x,y) = %.6f, f2(x,y) = %.6f%n", f1Value, f2Value);

        // Проверка достижения требуемой точности
        if (Math.abs(f1Value) < EPSILON && Math.abs(f2Value) < EPSILON) {
            System.out.print("Решение корректно - значения функций близки к нулю");
        } else {
            System.out.println("Решение некорректно - точность не достигнута");
        }
    }
}