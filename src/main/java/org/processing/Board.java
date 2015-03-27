package org.processing;
/* OpenProcessing Tweak of *@*http://www.openprocessing.org/sketch/9759*@* */
/* !do not delete the line above, required for linking your tweak if you upload again */
/*
Processing Matrix
by James Patrick Gordon
Inspired by ToneMatrix by Andre' Michelle
Special thanks to Zach Breman for his expertise.

This work is released under a GNU General Public License. For more information, go to: http://www.gnu.org/licenses/gpl.html
*/
import ddf.minim.AudioSample;
import ddf.minim.Minim;
import processing.core.PApplet;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

public class Board extends PApplet {

    Minim minim;

    //Used to set how many buttons in each row and column.
//final static int X = 24, Y = 22;
    final static int X = 5, Y = 4;
    final static int buttonSize = 25;
    final static int spacing = 22;
    final static int boxStrokeWeight = 5;
    final static int s2 = boxStrokeWeight * 2;
    final static int s3 = buttonSize + s2;
    final static int boardX = s2 + (X * s3), boardY = s2 + (Y * s3);
    static Map<Integer, button> buttons = new HashMap<>();
    static Set<Integer> pressedKeys = new ConcurrentSkipListSet<>();

    //This is creating an instance of a grid of 10 X 10 buttons
    button[][] b = new button[Y][X];

    //Creates a stepX and time var so that we can step based on time.
    int stepX = 0;
    long time = 0;
    AudioSample[] sounds;
    Runnable rn;
    Thread thr1;

    @Override
    public void setup() {
        //hue();
        size(boardX, boardY);
        smooth();
        frameRate(8);
        rectMode(CENTER);
        minim = new Minim(this);
        String[] filenames = {
                "C1.mp3", "D1.mp3", "E1.mp3", "F1.mp3", "G1.mp3", "A1.mp3", "B1.mp3", "C2.mp3", "D2.mp3", "E2.mp3", "F2.mp3", "G2.mp3", "A2.mp3", "B2.mp3", "C3.mp3", "D3.mp3", "E3.mp3", "F3.mp3", "G3.mp3", "A3.mp3", "B3.mp3", "C4.mp3"};
        println("Sound files size: " + filenames.length);
        //ZB: This initiates each of the buttons in the grid.
        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                b[y][x] = new button(spacing + (x * s3), spacing + (y * s3), buttonSize, buttonSize);
                int offset = (y * X) + x;
//                int val = KeyCompression.keyForOffset(offset);
                int val = ((int) 'a') + offset;
//                String s = String.valueOf((char) val);
                buttons.put(val, b[y][x]);
                //print("'" + s + "': " + b[y][x]);
            }
        }
        sounds = new AudioSample[filenames.length];
        for (int i = 0; i < filenames.length; i++) {
            sounds[i] = minim.loadSample("/Users/tyler/grayarea/project/ProcessingMatrix/data/" + filenames[i], 1024);
        }

        stepX = 0;
        //Sets the step time to current time to play the first column at start.
        time = System.currentTimeMillis();
        rn = new Runnable() {
            public void run() {
                //noinspection InfiniteLoopStatement
                while (true) {
                    //This checks to see if the current time is greater then the time set last time the step played.
                    if (System.currentTimeMillis() > time) {
                        //Play through the sounds of the on buttons in the column
                        for (int i = 0; i < Y; i++) {
                            if (b[i][stepX].isChecked) {
                                b[i][stepX].isHit = true;
                                sounds[i + (stepX * Y)].trigger();

                            }
                            //System.out.println(stepX + " " + i);
                        }
                        //Step the column by one then set it back to 0 when it gets to the end.
                        if (++stepX == X) stepX = 0;
                        time = System.currentTimeMillis() + 500;
                    }
                }
            }
        };
        thr1 = new Thread(rn);
        thr1.start();
    }

    void decompressKeys() {
        final Collection<Integer> decompressedKeys = KeyCompression.decompressKeys(pressedKeys);
        println("Decompressed keys: " + decompressedKeys);
        updateButtons(decompressedKeys);
    }

    void updateButtons(Collection<Integer> keys) {
        for (Integer buttonVal : buttons.keySet()) {
            buttons.get(buttonVal).setChecked(keys.contains(buttonVal));
//                buttons.get(buttonVal).isChecked = keys.contains(buttonVal);
        }
    }

    @Override
    public void draw() {
        //This draws and updates each of the buttons in the grid
        decompressKeys();
        for (int y = 0; y < Y; y++) {
            for (int x = 0; x < X; x++) {
                b[y][x].draw();
//                b[y][x].update();
            }
        }


    }

    class button {
        //This sets the color for each button
        int c;
        int x, y, w, h;
        boolean isChecked;
        boolean isHit;
        boolean keyPressed;

        button(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        void draw() {
            strokeWeight(5);
            fill(c);
            rect(x, y, w, h);
        }

        void setChecked(boolean checked) {
            this.isChecked = checked;
            c = (isChecked) ? 255 : 0;

        }

        void update() {
//            if (keyPressed || mousePressed && mouseX <= x + w / 2 && mouseX >= x - w / 2 && mouseY >= y - h / 2 && mouseY <= y + h / 2) {
//                isChecked = !isChecked;
//                c = (isChecked) ? 255 : 0;
//                keyPressed = !keyPressed;
//            }

        }
    }

    @Override
    public void stop() {
        // always close Minim audio classes when you finish with them
        for (AudioSample sound : sounds) {
            sound.close();
        }
        minim.stop();

        super.stop();
    }

    @Override
    public void keyPressed() {
        println("(Key, intKey, Keycode): " + "(" + key + ", " + (int) key + ", " + keyCode + ")");
        if (key >= '(' && key < 150) {
            pressedKeys.add((int) key);
//            final button button = buttons.get((int)key);
//            if (button != null) {
//                button.isHit = true;
//                button.isChecked = true;
//                button.keyPressed = true;
//            }
        } else {
            println("Unexpected (Key, intKey, Keycode): " + "(" + key + ", " + (int) key + ", " + keyCode + ")");
        }
    }

    public void keyReleased() {
        int c = key;
        if (pressedKeys.contains(c)) {
            pressedKeys.remove(c);
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[]{"org.processing.Board"});
    }

}
