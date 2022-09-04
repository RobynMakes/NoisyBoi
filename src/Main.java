import processing.core.PApplet;

import java.util.Random;

public class Main extends PApplet {
    /*----------------------------------------------------------------------------------------------------------------*/
    public final int SCREEN_WIDTH = 400, SCREEN_HEIGHT = 400; // Width and Height variables.
    public final int NOISE_OCT = 5;
    public final float NOISE_FALLOFF = (float) 0.5, NOISE_INCREMENT = (float) 0.02;
    public final float FRAME_RATE = 60; // FPS variable
    public final boolean SHOW_FPS = true; // Debug Booleans.

    public final Random RANDOM = new Random();

    public boolean pressed = false;
    /*----------------------------------------------------------------------------------------------------------------*/


    public void settings(){
        size(SCREEN_WIDTH,SCREEN_HEIGHT);
    }

    public void setup(){
        frameRate(FRAME_RATE);
    }

    public void draw(){
        background(255);
        if(SHOW_FPS) {
            textSize(16);
            text(frameRate, 10, 20);
        }
        loadPixels();
        noiseToPixels();
        updatePixels();
    }

    /*
    * It's smart to have all our noise manipulation in its own method to avoid cluttering up the draw() method.
    */
    public void noiseToPixels(){
        /*
        * Changes the noise seed to a random long value if SPACE is pressed.
        * Feel free to comment this code out if you do not want this functionality.
        * It doesn't interact with any of the other code.
        */
        if(keyPressed && !pressed) {
            pressed = true;
            background(0);
            if (key == ' ') {
                noiseSeed(RANDOM.nextLong());
            }
        } else if (!keyPressed) {
                pressed = false;
        }

        /* Defines our x offset variable. */
        float xoff = 0;

        /*
        * Sets the noise detail level to our control variables.
        * NOISE_OCT being the number of noise octaves.
        * NOISE_FALLOFF being the transparency falloff of each octave.
        */
        noiseDetail(NOISE_OCT, NOISE_FALLOFF);

        /*
        * This is where the magic happens.
        * This for loop will loop through all the pixels on the screen and change their color value to a noise value.
        */
        for(int x = 0; x < width; x++){
            xoff += NOISE_INCREMENT; // increments the xoff variable by our control variable NOISE_INCREMENT
            float yoff = 0; // Each x-value can be thought of as having a set of y-values associated with it.

            /*
            * This for loop will loop through all the y-values associated with the current x-value.
            */
            for (int y = 0; y < height; y++){
                yoff += NOISE_INCREMENT; // increments the yoff varialbe by our control variable NOISE_INCREMENT

                /*
                * I've found that when manipulating noise values, storing the current noise value in a variable is
                * preferable to typing noise(xoff,yoff); a million times.
                */
                float noiseVal = noise(xoff,yoff);

                /*
                * This code block makes the noise either 0 or 1 depending on its decimal value.
                *   1 if the value is 0.5 or greater.
                *   0 if the value is 0.4 or lower.
                * Good for generating caves.
                */
//                if(noiseVal != 0 || noiseVal != 1) {
//                    if (abs(noiseVal - 0) < abs(noiseVal - 1)) { noiseVal = 1; }
//                    else if (abs(noiseVal - 0) > abs(noiseVal - 1)) { noiseVal = 0; }
//                    else { noiseVal = 1; }
//                }

                /* Maps the current noise value from 0-1 to 0-255 */
                noiseVal = map(noiseVal, 0, 1, 0, 255);

                /* colR, colG, and colB control the RGB value of the current pixel position. */
                float colR = noiseVal;
                float colG = noiseVal;
                float colB = noiseVal;

                /* Assigns the current pixel position to the color values we've just assigned. */
                pixels[x+y*width] = color(colR,colG,colB);
            }
        }
    }

    /* Runs our draw() method. */
    public static void main(String[] args) {
        String[] processingArgs = {"Main"};
        Main main = new Main();
        PApplet.runSketch(processingArgs, main);
    }

}
