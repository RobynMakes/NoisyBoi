import processing.core.PApplet;

import java.util.Random;

public class Main extends PApplet {
    /*----------------------------------------------------------------------------------------------------------------*/
    public final int SCREEN_WIDTH = 400, SCREEN_HEIGHT = 400; // Width and Height variables.
    public final int NOISE_OCT = 5;
    public final float NOISE_FALLOFF = (float) 0.5, NOISE_INCREMENT = (float) 0.01;
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

        loadPixels();

        noiseToPixels();

        updatePixels();

        /* Any debug code should be run at the end of the draw() method to insure it is visible. */
        if(SHOW_FPS) {
            textSize(16);
            text(frameRate, 10, 20);
        }
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

                /* -------------------------------------------------------------------------------------------------- */
                /* Noise manipulation code should go here.
                 * Specific noise manipulation code should be put in its own method.
                */
                /* -------------------------------------------------------------------------------------------------- */

                //noiseVal = constrainNoise(noiseVal, (float) 0.9);

                //noiseVal = absNoise(noiseVal);

                //noiseVal = roundNoise(noiseVal);

                //noiseVal = invertNoise(noiseVal);

                /* -------------------------------------------------------------------------------------------------- */

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

    /*
     * Inverts the given noise value.
     */
    float invertNoise(float n){
        n = map(n, 0, 1, -1, 1);
        n *= -1;
        n = map(n, -1, 1, 0, 1);
        return n;
    }

    /*
     * This code block makes the noise either 0 or 1 depending on its decimal value.
     *   1 if the value is 0.5 or greater.
     *   0 if the value is 0.4 or lower.
     * Good for generating caves.
     */
    float roundNoise(float n){
        if(n != 0 || n != 1) {
            if (abs(n - 0) < abs(n - 1)) { n = 1; }
            else if (abs(n - 0) > abs(n - 1)) { n = 0; }
            else { n = 1; }
        }
        return n;
    }

    /*
     * This code block start's by mapping our noise value from 0<->1 to -1<->1.
     * Then if the noise value is not equal to 0, 1, or -1 the value is inverted.
     * Finally, we take the absolute value of the calculated value and assign it as our noise value.
     * Creates veiny lines of black, but keeps the light noise.
     */
    float absNoise(float n) {
        n = map(n, 0, 1, -1, 1);
        if(n != 0 || n != 1 || n != -1){
            n *= -1;
        }
        n = abs(n);
        return n;
    }

    /*
     * Maps the noise from 0<->1 to -1<->1
     * If the noise value is less than the given value, it is inverted.
     * Constrains the noise value from 0<->1 and returns the result.
     * Creates a land and sea differentiation.
     */
    float constrainNoise(float n, float val){
        n = map(n, 0, 1, -1, 1);
        if(n > val){
            n *= -1;
        }
        n = constrain(n, 0, 1);

        return n;
    }

    /* Runs our draw() method. */
    public static void main(String[] args) {
        String[] processingArgs = {"Main"};
        Main main = new Main();
        PApplet.runSketch(processingArgs, main);
    }

}
