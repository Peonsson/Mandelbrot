import org.apache.commons.math3.complex.Complex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * Writes an Image to file of the Mandelbrot set with resolution width and height
 * from min_c_re to max_c_re and min_c_im and max_c_im with max iterations max_iter.
 *
 * Created 2015-02-19.
 *
 * Written by Peonsson and roppe546
 *
 * https://github.com/Peonsson
 * https://github.com/roppe546
 *
 */

public class Mandelbrot {

    // Default values for width, height and max_iter in case user inputs
    // bad arguments.
    private static int width = 512, height = 512, max_iter = 1024;

    public static void main(String[] args) {

        // These variables hold information about which coordinates
        // the image should zoom in on. Defaulting on showing the
        // entire mandelbrot set.
        double min_c_re = -2, min_c_im = -2, max_c_re = 2, max_c_im = 2;

        // Parse command line arguments
        try {
            min_c_re = Double.parseDouble(args[0]);
            min_c_im = Double.parseDouble(args[1]);
            max_c_re = Double.parseDouble(args[2]);
            max_c_im = Double.parseDouble(args[3]);
            max_iter = Integer.parseInt(args[4]);
            width = Integer.parseInt(args[5]);
            height = Integer.parseInt(args[6]);
        }
        catch (NullPointerException | NumberFormatException ex) {
            System.out.println("Arguments were not entered correctly. Using defaults.");
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Missing arguments, expected: min_c_re min_c_im max_c_re max_c_im max_iterations width height. Using defaults.");
        }

        int[][] matrix = new int[width][height];

        // TODO: Move this logic (loop) to the web services server
        // Traverse through each cell (representing a pixel in the final image) in
        // the matrix, calculating if the different coordinates within the boundaries
        // are part of the mandelbrot set or not.
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                double c_re = min_c_re + col * (max_c_re - min_c_re) / width;
                double c_im = min_c_im + row * (max_c_im - min_c_im) / height;

                Complex c = new Complex(c_re, c_im);

                // Save number of iterations needed. The value returned represents
                // the color value used in the image.
                matrix[row][col] = (max_iter - computeIterations(c)) % 256;
            }
        }

        saveImage(matrix);
    }


    // TODO: Move this logic (method) to the web services server
    /**
     * Find how many iterations are needed to verify if complex number z is in the
     * Mandelbrot set.
     *
     * @param z     complex number
     * @return      number of iterations needed to verify if it was in the set
     */
    private static int computeIterations(Complex z) {

        double real = z.getReal();
        double imaginary = z.getImaginary();

        Complex c = new Complex(real, imaginary);

        for (int m = 0; m < max_iter; m++) {
            if (z.abs() > 2.0) {
                return m;
            }
            z = z.multiply(z).add(c);
        }
        return max_iter;
    }


    /**
     * Convert and save an integer matrix as a png image. Image is saved to the same folder
     * as where the jar file is run.
     *
     * @param matrix    the matrix to save as an image
     */
    private static void saveImage(int[][] matrix) {
        try {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            File f = new File("MyFile.png");

            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
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
}