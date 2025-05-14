// Класс, реализующий метод дихотомии (деления пополам) для нахождения корня уравнения
public class DichotomyMethod {

    public static double f(double x) {
        return x - 2 + Math.exp(x); 
    }

    public static double dichotomy(double a, double b, double epsilon) {
        double c; // Переменная для хранения середины текущего интервала
        int iterations = 0; 

        if (f(a) * f(b) >= 0) {
            System.err.println("Ошибка: функция не имееет корня");
            return Double.NaN;
        }
        
        // Основной цикл метода: продолжается, пока длина интервала больше 2*epsilon
        while ((b - a) / 2 > epsilon) {
            c = (a + b) / 2; // Находим середину текущего интервала
            
            // Проверяем, не является ли середина интервала корнем
            if (f(c) == 0.0) {
                break; // Если да, выходим из цикла
            } 
            // Проверяем условие смены границы интервала
            else if (f(a) * f(c) < 0) { // Если произведение отрицательное, корень в левой половине
                b = c; // Сдвигаем правую границу в середину
            } 
            else { // Иначе корень в правой половине
                a = c; // Сдвигаем левую границу в середину
            }
            iterations++; 
        }
        
        System.out.println("iterations " + iterations);
        
        // Возвращаем приближенное значение корня (середину последнего интервала)
        return (a + b) / 2;
    }

    public static void main(String[] args) {
        double a = 3.0;
        double b = 11.0; 
        double epsilon = 0.0001; 
        double root = dichotomy(a, b, epsilon); 

        System.out.printf("answer: %.4f%n", root);
    }
}
