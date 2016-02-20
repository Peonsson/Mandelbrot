import org.apache.commons.math3.complex.Complex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
    Writes an Image to file of the Mandelbrot set with resolution width and height
    from min_c_re to max_c_re and min_c_im and max_c_im with max iterations max_iter.

    Created 2015-02-19.

    Written by Peonsson and roppe546

    https://github.com/Peonsson
    https://github.com/roppe546
 */

public class Mandelbrot {

    private static int width = 512, height = 512, max_iter = 1024;

    public static void main(String[] args) {


        // These variables hold information about which coordinates
        // the image should zoom in on. Defaulting on showing the
        // entire mandelbrot set.
        double min_c_re = -2, min_c_im = -2, max_c_re = 2, max_c_im = 2;

        // Parse command line arguments
        try {
            min_c_re = Double.parseDouble(args[1]);
            min_c_im = Double.parseDouble(args[2]);
            max_c_re = Double.parseDouble(args[3]);
            max_c_im = Double.parseDouble(args[4]);
            max_iter = Integer.parseInt(args[5]);
            width = Integer.parseInt(args[6]);
            height = Integer.parseInt(args[7]);
        }
        catch (NullPointerException | NumberFormatException ex) {
            System.out.println("Arguments were not entered correctly. Using defaults.");
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Missing arguments, expected: min_c_re min_c_im max_c_re max_c_im max_iterations width height. Using defaults.");
        }

        int[][] matrix = new int[width][height];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                double c_re = min_c_re + col * (max_c_re - (min_c_re)) / width;
                double c_im = min_c_im + row * (max_c_im - (min_c_im)) / height;

                Complex c = new Complex(c_re, c_im);

                matrix[row][col] = (max_iter - compute(c)) % 256;
            }
        }

        // Save image
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int compute(Complex z) {

        double real = z.getReal();
        double imaginary = z.getImaginary();

        Complex c = new Complex(real, imaginary);

        for (int m = 0; m < max_iter; m++) { // for each pixel
            if (z.abs() > 2.0) {
                return m;
            }
            z = z.multiply(z).add(c);
        }
        return max_iter;
    }
}