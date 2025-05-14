package lab2;

    public class GaussJordan {
    public static double[] gaussJordan(double[][] A, double[] b) {
        // Размер системы (количество уравнений)
        int n = b.length;

        // Создаём расширенную матрицу [A | b]
        double[][] augmentedMatrix = new double[n][n + 1];
        for (int i = 0; i < n; i++) {
            // Копируем строку из матрицы A
            for (int j = 0; j < n; j++) {
                augmentedMatrix[i][j] = A[i][j];
            }
            // Добавляем элемент из вектора b
            augmentedMatrix[i][n] = b[i];
        }

        // Прямой ход метода Гаусса-Жордана
        for (int i = 0; i < n; i++) {
            // === Шаг 1: Выбор главного элемента (чтобы избежать деления на 0) ===
            int maxRow = i; // Предполагаем, что текущая строка содержит максимальный элемент
            for (int k = i + 1; k < n; k++) {
                // Ищем строку, где элемент в столбце `i` больше, чем в текущей
                if (Math.abs(augmentedMatrix[k][i]) > Math.abs(augmentedMatrix[maxRow][i])) {
                    maxRow = k;
                }
            }

            // === Шаг 2: Перестановка строк ===
            // Меняем местами строку `i` и строку `maxRow`
            double[] temp = augmentedMatrix[i];
            augmentedMatrix[i] = augmentedMatrix[maxRow];
            augmentedMatrix[maxRow] = temp;

            // === Шаг 3: Нормировка строки (делаем диагональный элемент равным 1) ===
            double pivot = augmentedMatrix[i][i]; // Запоминаем ведущий элемент
            for (int j = i; j <= n; j++) {
                augmentedMatrix[i][j] /= pivot; // Делим всю строку на ведущий элемент
            }

            // === Шаг 4: Обнуление столбца во всех остальных строках ===
            for (int k = 0; k < n; k++) {
                if (k != i) { // Пропускаем текущую строку
                    double factor = augmentedMatrix[k][i]; // Коэффициент для вычитания
                    for (int j = i; j <= n; j++) {
                        // Вычитаем из строки `k` строку `i`, умноженную на `factor`
                        augmentedMatrix[k][j] -= factor * augmentedMatrix[i][j];
                    }
                }
            }
        }

        // Извлекаем решение из последнего столбца расширенной матрицы
        double[] x = new double[n];
        for (int i = 0; i < n; i++) {
            x[i] = augmentedMatrix[i][n];
        }

        return x;
    }

    public static void main(String[] args) {
        // Матрица коэффициентов A
        double[][] A = {
                {3, -1, 1},
                {3, 5, 1},
                {-10, 2, 4}
        };

        // Вектор правой части b
        double[] b = {0, 12, -1};

        // Решаем систему методом Гаусса-Жордана
        double[] x = gaussJordan(A, b);

        // Выводим решение
        System.out.println("Решение:");
        for (int i = 0; i < x.length; i++) {
            System.out.printf("x%d = %.2f\n", i + 1, x[i]);
        }
    }
}