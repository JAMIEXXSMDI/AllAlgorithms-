package lab2;
import java.util.Arrays; // Импорт класса Arrays для работы с массивами

public class Gradient {

    // Метод для умножения матрицы на вектор
    public static double[] matrixVectorMultiply(double[][] matrix, double[] vector) {
        // Создаем массив result для хранения результата умножения
        double[] result = new double[matrix.length]; 
        
        // Внешний цикл по строкам матрицы
        for (int i = 0; i < matrix.length; i++) { 
            // Внутренний цикл по элементам вектора
            for (int j = 0; j < vector.length; j++) { 
                // Умножаем элемент матрицы на элемент вектора и добавляем к результату
                result[i] += matrix[i][j] * vector[j]; 
            }
        }
        return result; // Возвращаем результат умножения
    }

    // Метод для вычисления градиента
    public static double[] computeGradient(double[][] A, double[] B, double[] X) {
        // Умножаем матрицу A на вектор X
        double[] AX = matrixVectorMultiply(A, X); 
        
        // Создаем массив для хранения невязки (разницы между AX и B)
        double[] residual = new double[AX.length]; 
        
        // Вычисляем невязку для каждого уравнения
        for (int i = 0; i < AX.length; i++) { 
            residual[i] = AX[i] - B[i]; // AX - B
        }

        // Создаем массив для хранения градиента
        double[] gradient = new double[X.length]; 
        
        // Вычисляем градиент как A^T * residual
        for (int j = 0; j < X.length; j++) { // По каждому параметру X
            for (int i = 0; i < A.length; i++) { // По каждому уравнению
                gradient[j] += A[i][j] * residual[i]; // Суммируем произведения
            }
        }
        return gradient; // Возвращаем градиент
    }

    // Метод градиентного спуска
    public static double[] gradientDescent(double[][] A, double[] B, double learningRate, 
                                         int maxIterations, double tolerance) {
        // Определяем размерность задачи (число переменных)
        int n = A[0].length; 
        
        // Создаем начальное приближение X (заполняем нулями)
        double[] X = new double[n]; 
        
        double error = Double.POSITIVE_INFINITY; 
        int iter; 
        
        // Основной цикл градиентного спуска
        for (iter = 0; iter < maxIterations && error > tolerance; iter++) {
            // Вычисляем градиент на текущем шаге
            double[] gradient = computeGradient(A, B, X); 
            
            // Обновляем параметры X
            for (int i = 0; i < X.length; i++) { 
                X[i] -= learningRate * gradient[i]; // X = X - learningRate * gradient
            }
            
            // Вычисляем текущее значение AX
            double[] AX = matrixVectorMultiply(A, X); 
            
            // Вычисляем ошибку как евклидову норму невязки
            error = 0.0; 
            for (int i = 0; i < AX.length; i++) { 
                error += Math.pow(AX[i] - B[i], 2); // Сумма квадратов разностей
            }
            error = Math.sqrt(error); // Корень из суммы квадратов
            
            // Выводим информацию о текущей итерации
            System.out.print("Итерация " + iter + ": X = "); 
            System.out.print(Arrays.toString(X)); // Текущие значения X
            System.out.println(", Ошибка = " + error); // Текущая ошибка
        }
        
    
        if (error <= tolerance) { 
            System.out.println("Точное решение достигнуто на итерации " + (iter - 1));
        } else {
            System.out.println("Достигнуто максимальное число итераций, точное решение не найдено.");
        }
        
        return X; 
    }

    // Главный метод программы
    public static void main(String[] args) {
        // Задаем матрицу коэффициентов системы уравнений
        double[][] A = {
            {3, -1, 1},
            {3, 5, 1},
            {-100, 2, 4}
        };

        // Задаем вектор правых частей системы
        double[] B = {0, 12, -1};

        // Задаем параметры градиентного спуска:
        double learningRate = 0.01; // Скорость обучения (размер шага)
        int maxIterations = 1000;    // Максимальное число итераций
        double tolerance = 0.001;   // Допустимая погрешность

        // Вызываем метод градиентного спуска
        double[] X = gradientDescent(A, B, learningRate, maxIterations, tolerance);

        // Выводим полученное решение
        System.out.print("Решение X = ");
        System.out.println(Arrays.toString(X));
    }
}