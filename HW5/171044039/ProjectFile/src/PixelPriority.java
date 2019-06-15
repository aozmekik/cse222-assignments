
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

/**
 * Represents the pixel priority extractor/handler.
 * With a given digital image with width and height,
 * constructs max priority queues for color pixels.
 * Enters the pixels one by one into priority queues,
 * then extracts them according to different priority
 * schemes(ordering relations).
 * Supports 3 color comparison methods: 1- Lexicographical (LEX)
 *                                      2- Euclidean norm (EUC)
 *                                      3- Bitmix (BMX)
 *
 * @see PriorityQueue
 * @author Ahmed Semih Özmekik
 */
public class PixelPriority
{
    private RGBColor[][] img2D = null; // 2D array representation of image with RGB vectors.
    private int width = 0; // Width of the image.
    private int height = 0; // Height of the image.
    private int xcoor = 0, ycoor = 0; // [x,y] coordinates in the image.

    // Flag for to ease thread communications.
    private static volatile boolean isCompleted = false;

    /* Priority Queues defined and created for each priority scheme. */
    private final PriorityQueue<RGBColor> queueLEX = new PriorityQueue<>(new LEXComparator());
    private final PriorityQueue<RGBColor> queueEUC = new PriorityQueue<>(new EUCComparator());
    private final PriorityQueue<RGBColor> queueBMX = new PriorityQueue<>(new BMXComparator());

    /* Singleton Design Pattern */
    private static PixelPriority instance = new PixelPriority();

    public static PixelPriority getInstance() { return instance; }

    private PixelPriority() {/* intentionally left blank */}

    /**
     * Sets image for singleton. File is the name of the image.
     * @param file path to to image.
     * @throws IOException
     */
    public void setFile(String file) throws IOException
    {

        // Get the image from given path and represent as 2D array.
        BufferedImage img = ImageIO.read(new File(file));
        width = img.getWidth();
        height = img.getHeight();
        img2D = new RGBColor[height][width];
        for(int y=0;y<height;++y){
            for (int x=0;x<width;++x)
                img2D[y][x] = new RGBColor(img.getRGB(x,y));
        }
    }

    /**
     * Starts the produce-consume stream.
     */
    public void start()
    {
        Thread thread1 = new Thread(this::producePixel); // producer of each queue.
        thread1.start();

    }

    /**
     * Run() method of the Thread1 which simulates producer.
     * Thread1 first, read the first 100 pixels from image and
     * insert to each queue. After that, start 3 other threads which
     * simulate consumer and then continue to produce pixels into queues.
     */
    private void producePixel()
    {

        /* Read the first 100 pixels from image and insert to each 3 queue. */
        insertPixels(100);

        /* After the first 100 pixels are inserted, create and start 3 threads. */
        Thread thread2 = new PixelConsumer(queueLEX, // consumer of queueLEX.
                "Thread2-PQLEX");
        Thread thread3 = new PixelConsumer(queueEUC, // consumer of queueEUC.
                "Thread3-PQEUC");
        Thread thread4 = new PixelConsumer(queueBMX, // consumer of queueBMX.
                "Thread4-PQBMX");

        thread2.start();
        thread3.start();
        thread4.start();

        /* Thread 1 continues to inserting remaining pixels. */
        insertPixels();

    }

    /**
     * Represents the consumer thread for 3 threads.
     * For each thread there is a unique queue. And this
     * specific queue used as intrinsic locks for synchronization.
     * There is one producer thread needs to be synchronized with 3 other
     * consumer threads which doesn't necessarily need to synchronized
     * with each other. Usage of unique queues as intrinsic locks achieves this.
     */
    private static class PixelConsumer extends Thread
    {
        private final PriorityQueue<RGBColor> queue;
        private final String threadName;

        private PixelConsumer(PriorityQueue<RGBColor> queue, String threadName)
        {
            this.queue = queue;
            this.threadName = threadName;
        }

        @Override
        public void run() {
            synchronized (queue) {
                while (!isCompleted) {
                    // Consumer waits for consumer if the queue is empty.
                    while (queue.isEmpty() && !isCompleted) {
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (isCompleted)
                        break;
                    RGBColor color = queue.remove();
                    System.out.println(threadName + ": " + color);

                }
            }
            // Consumer is free to consume all, since producer has finished it's job.
            while(!queue.isEmpty()){
                RGBColor color = queue.remove();
                System.out.println(threadName + ": " + color);
            }
        }
    }

    /**
     * Inserts all the pixels to each queue. (x,y) coordinates will continue from
     * where they left off. insertQueues() method and overloads are very specified
     * methods for Thread1's job. Method's inner workings synchronized with 3 other
     * threads.
     */
    private void insertPixels()
    {
        for( ; ycoor<height;++ycoor){
            for ( ; xcoor<width;++xcoor){
                RGBColor color = img2D[ycoor][xcoor];
                System.out.println("Thread1:" + color);

                synchronized (queueLEX) {
                    queueLEX.offer(color);
                    queueLEX.notify();
                }
                synchronized (queueEUC) {
                    queueEUC.offer(color);
                    queueEUC.notify();
                }
                synchronized (queueBMX) {
                    queueBMX.offer(color);
                    queueBMX.notify();
                }
            }

            xcoor = 0;
        }
        synchronized (queueLEX) {isCompleted = true; queueLEX.notify();}
        synchronized (queueEUC) {isCompleted = true; queueEUC.notify();}
        synchronized (queueBMX) {isCompleted = true; queueBMX.notify();}

    }

    /**
     * Inserts "N" many pixel to 3 queues. "N" being totalPixel parameter.
     * This overload does not consist sync work since there is no need for
     * first N pixels.
     * @param totalPixel is the number of pixels to be inserted to each queue.
     */
    private void insertPixels(int totalPixel)
    {
        int pixel = 0; // read pixel number.
        while(pixel<totalPixel){
            RGBColor color = img2D[ycoor][xcoor];
            System.out.println("Thread1: " + color);
            queueLEX.offer(color);
            queueEUC.offer(color);
            queueBMX.offer(color);

            ++pixel;
            ++xcoor;
            if (xcoor == width) {
                xcoor = 0;
                ++ycoor;
            }
        }
    }


    /**
     * Represents the RGB(Red, Green, Blue) vector.
     * I could have used Color Class from java.awt instead of writing one,
     * yet for to avoid overhead and keep simplicity, I wrote mine vector
     * representation.
     */
    private class RGBColor implements Comparable<RGBColor>
    {
        private final int RED;
        private final int GREEN;
        private final int BLUE;
        private final int RGB; // keeps the RGB value.

        /**
         * Parses the pixel value to RGB vector.
         * @param pixel RGB value.
         */
        private RGBColor(int pixel)
        {
            RGB = pixel;
            RED = (pixel >> 16) & 0xff;
            GREEN = (pixel >> 8) & 0xff;
            BLUE = (pixel) & 0xff;
        }

        /**
         * Lexicographical comparison implementation.
         * @param other is other RGBColor to compare with.
         * @return positive if this object is greater, otherwise negative and 0 for equality.
         */
        @Override
        public int compareTo(RGBColor other)
        {
            return Integer.compare(RGB, other.RGB);
        }

        /**
         * Returns the RGBColor string representation: [R,G,B]
         * @return the RGBColor string.
         */
        @Override
        public String toString() {
            return String.format("[%d,%d,%d]", RED, GREEN, BLUE);
        }
    }

    /**
     * Represents the Comparator which follows the Lexicographical
     * comparison method.
     */
    private class LEXComparator implements Comparator<RGBColor>
    {
        @Override
        public int compare(RGBColor c1, RGBColor c2)
        {
            return c1.compareTo(c2);
        }
    }

    /**
     * Represents the Comparator which follows the Euclidean norm based
     * comparison method.
     */
    private class EUCComparator implements Comparator<RGBColor>
    {
        @Override
        public int compare(RGBColor c1, RGBColor c2)
        {
            // Get the Euclidean norms and compare them.
            double norm1 = findNorm(c1);
            double norm2 = findNorm(c2);

            return Double.compare(norm1, norm2);
        }

        /**
         * Finds the norm of the given RGB Vector.
         * @param c is the RGB Vector.
         * @return the Euclidean norm of the vector.
         */
        private double findNorm(RGBColor c)
        {
            return Math.sqrt(c.RED*c.RED + c.GREEN*c.GREEN + c.BLUE*c.BLUE);
        }
    }

    /**
     * Represents the Comparator which follows the Bitmix
     * comparison method.
     */
    private class BMXComparator implements Comparator<RGBColor>
    {
        @Override
        public int compare(RGBColor c1, RGBColor c2)
        {
            // Get the bit mixed colors and compare the.
            int bmxColor1 = bitmix(c1);
            int bmxColor2 = bitmix(c2);

            return Integer.compare(bmxColor1, bmxColor2);
        }
    }

    /**
     * Mixes the bit according specified scheme down below.
     * Returns the coded number.
     *                  Mixing Scheme: (RX is one decimal digit)
     *                                  RED = R1R2R3...
     *                                  GREEN = G1G2G3...
     *                                  BLUE = B1B2B3...
     *                                  MIXED = R1G1B1R2G2B2...
     * @param color is the RGB vector.
     * @return the coded number.
     */
    private int bitmix(RGBColor color)
    {
        int result = 0;
        int power = 7; // Power starts from 7 (RGB uses 8 bit, 0-255)
        for(int i=23;i>=0;i-=3){

            // Get the digit from number via bit wise "&" operator.
            int digitBinary = (int)Math.pow(2,power--);
            int red = color.RED & digitBinary;
            int green = color.GREEN & digitBinary;
            int blue = color.BLUE & digitBinary;

            digitBinary = (int)Math.pow(2, i);
            result += (red != 0) ? digitBinary:0;
            result += (green != 0) ? (digitBinary/2):0;
            result += (blue != 0) ? (digitBinary/4):0;

        }
        return result;
    }


}
