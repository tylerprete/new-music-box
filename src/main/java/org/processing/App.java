package org.processing;

import processing.core.PApplet;

/**
 * Hello world!
 *
 */
public class App extends PApplet
{
    @Override
    public void setup() {
        size(500, 500);
        println("Hello World!");
    }

    public static void main( String[] args )
    {
        PApplet.main(new String[]{"org.processing.App"});
    }
}
