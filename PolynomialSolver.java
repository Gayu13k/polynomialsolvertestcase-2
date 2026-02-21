import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.util.*;

public class PolynomialSolver {

    public static void main(String[] args) throws Exception {

        String content = new String(Files.readAllBytes(Paths.get("input.json")));

        // Extract k value
        int k = Integer.parseInt(
                content.split("\"k\"")[1]
                       .split(":")[1]
                       .split("}")[0]
                       .replaceAll("[^0-9]", "")
        );

        List<Point> points = new ArrayList<>();

        String[] parts = content.split("\"base\"");

        for (int i = 1; i < parts.length; i++) {

            String leftPart = parts[i - 1];

            // extract x
            String xStr = leftPart.substring(leftPart.lastIndexOf("\"") - 2);
            xStr = xStr.replaceAll("[^0-9]", "");
            int x = Integer.parseInt(xStr);

            // extract base
            String baseStr = parts[i].split(":")[1]
                                     .split(",")[0]
                                     .replaceAll("[^0-9]", "");
            int base = Integer.parseInt(baseStr);

            // extract value
            String value = parts[i].split("\"value\"")[1]
                                   .split(":")[1]
                                   .split("\"")[1];

            BigInteger y = new BigInteger(value, base);

            points.add(new Point(x, y));
        }

        points.sort(Comparator.comparingInt(p -> p.x));

        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {

            BigInteger term = points.get(i).y;

            for (int j = 0; j < k; j++) {

                if (i != j) {

                    BigInteger numerator =
                            BigInteger.valueOf(-points.get(j).x);

                    BigInteger denominator =
                            BigInteger.valueOf(
                                    points.get(i).x - points.get(j).x);

                    term = term.multiply(numerator)
                               .divide(denominator);
                }
            }

            result = result.add(term);
        }

        System.out.println("Constant Term (Secret) = " + result);
    }

    static class Point {
        int x;
        BigInteger y;

        Point(int x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }
}