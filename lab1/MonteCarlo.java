package lab1;
import java.util.Random;
import java.util.function.Function;

public class MonteCarlo {
   
   public static double monteCarlo(double a, double b, int n, Function<Double, Double> F) {
      Random r = new Random();
      double sum = 0.0;

      for(int i = 0; i < n; ++i) {
         double x = a + (b - a) * r.nextDouble();
         sum += (Double)F.apply(x);
      }

      return (b - a) * (sum / (double)n);
   }

   public static void main(String[] args) {
      double a = 1.16;
      double b = 2.72;
      int n = 100000;
      Function<Double, Double> F = new SquareFunction();
      double result = monteCarlo(a, b, n, F);
      System.out.println("Значение интеграла: " + result);
   }
}
