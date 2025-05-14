package lab1;
import java.util.function.Function;

public class Simpson {

   public static double simpson(double a, double b, int n, Function<Double, Double> f) {
      if (n % 2 != 0) {
         throw new IllegalArgumentException("Количество n должно быть четным");
      } else {
         double h = (b - a) / (double)n;
         double sum = (Double)f.apply(a) + (Double)f.apply(b);

         for(int i = 1; i < n; ++i) {
            double x = a + (double)i * h;
            if (i % 2 == 0) {
               sum += 2.0 * (Double)f.apply(x);
            } else {
               sum += 4.0 * (Double)f.apply(x);
            }
         }

         return h / 3.0 * sum;
      }
   }

   public static double simpsonWithPrecision(double a, double b, Function<Double, Double> f, double precision) {
      int n = 2;

      double I2;
      double I4;
      do {
         I2 = simpson(a, b, n, f);
         I4 = simpson(a, b, 2 * n, f);
         n *= 2;
      } while(Math.abs(I2 - I4) / 15.0 >= precision);

      System.out.println("Использовано интервалов: " + n);
      return I4;
   }

   public static void main(String[] args) {
      double a = 0.6;
      double b = 7.0;
      double precision = 1.0E-4;
      Function<Double, Double> F = new SquareFunction();
      double result = simpsonWithPrecision(a, b, F, precision);
      System.out.println("Значение интеграла: " + result);
   }
}

