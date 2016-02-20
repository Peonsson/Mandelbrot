import org.apache.commons.math3.complex.Complex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Mandelbrot {

    private static int width = 2000, height = 2000, max_n = 1048;
    private static int[][] matrix = new int[width][height];
    private static double min_c_re = -1.5, min_c_im = -0.5, max_c_re = -0.5, max_c_im = 0.5;

    public static void main(String[] args) {

        for (int row = 0; row < height; row++) { // for each row
            for (int col = 0; col < width; col++) { // for each column
                double c_re = min_c_re + col * (max_c_re - (min_c_re)) / width;
                double c_im = min_c_im + row * (max_c_im - (min_c_im)) / height;

                Complex c = new Complex(c_re, c_im);

                matrix[row][col] = (max_n - compute(c)) % 256;
            }
        }

        try {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            File f = new File("MyFile.png");

            for (int row = 0; row < height; row++) { // for each row
                for (int col = 0; col < width; col++) { // for each column
                    int color = matrix[col][row];

                    Color newColor = new Color(color);
                    img.setRGB(row, col, newColor.getRGB());
                }
            }
            ImageIO.write(img, "PNG", f);

        } catch (Exception e) {
            System.out.println("crash");
            e.printStackTrace();
        }
    }

    private static int compute(Complex z) {

        double real = z.getReal();
        double imaginary = z.getImaginary();

        Complex c = new Complex(real, imaginary);

        for (int m = 0; m < max_n; m++) { // for each pixel
            if (z.abs() > 2.0) {
                return m;
            }
            z = z.multiply(z).add(c);
        }
        return max_n;
    }
}