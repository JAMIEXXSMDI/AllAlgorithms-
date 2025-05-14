// Класс, реализующий метод Ньютона для нахождения корня уравнения
public class NewTon {

    // Функция, корень которой ищем: f(x) = x - 2 + e^x
    public static double f(double x) {
        return x - 2 + Math.exp(x);
    }

    // Численное вычисление производной с помощью аппроксимации, где h - это очень маленькое число.
    public static double numericalDerivative(double x, double h) {
        return (f(x + h) - f(x)) / h;
    }

    // x0 - начальное приближение
    public static double newtonMethod(double x0, double epsilon) {
        double x = x0;  // Текущее приближение
        double delta;    // Разница между текущим и следующим приближением
        int iterations = 0;  // Счетчик итераций

        do {
            double fx = f(x);  // Значение функции в текущей точке
            double dfx = numericalDerivative(x, 1e-5);  // Численная производная

            // Если производная близка к нулю → метод может дать ошибку
            if (dfx == 0) {
                System.err.println("Производная равна нулю. Метод не может быть применен.");
                return Double.NaN; 
            }

            delta = fx / dfx;  // Вычисляем поправку (по формуле Ньютона)
            x = x - delta;     // Обновляем приближение
            iterations++;     // Увеличиваем счетчик итераций

        } while (Math.abs(delta) >= epsilon);  // Пока поправка больше требуемой точности

        System.out.println("iterations: " + iterations);  // Выводим количество итераций
        return x;  // Возвращаем найденный корень
    }

    // Главный метод программы
    public static void main(String[] args) {
        double x0 = 1.0;      // Начальное приближение
        double epsilon = 0.0001;  // Требуемая точность

        double root = newtonMethod(x0, epsilon);  

        // Проверяем, был ли найден корень (не NaN)
        if (!Double.isNaN(root)) {
            System.out.printf("root: %.6f%n", root);  // Выводим корень с точностью до 6 знаков
        } else {
            System.out.println("error");  // Если метод не сработал
        }
    }
}
