package lab1;

import java.util.function.Function;

class SquareFunction implements Function<Double, Double> {
   public Double apply(Double x) {
      return Math.cos(Math.log(x)) + 1.0 / (x * x * x);
   }
}