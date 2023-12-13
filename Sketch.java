import processing.core.PApplet;
import java.util.ArrayList;

public class Sketch extends PApplet {

    int stickmanX, stickmanY;
    float speed = 5;

    boolean wKey, sKey, aKey, dKey;

    // ArrayLists to keep track of elements
    ArrayList<float[]> clouds;
    ArrayList<float[]> flowers;
    ArrayList<float[]> butterflies;
    ArrayList<float[]> grass;

    // Time interval for drawing grass
    int intInterval = 100;
    // Last time the grass was drawn
    int intLastTime = 0;

    // Initial sky color
    int skyColor = color(0, 0, 255);

    /**
     * Sets up the canvas size.
     */
    public void settings() {
        size(500, 500);
    }

    /**
     * Sets up the initial sky color and background.
     */
    public void setup() {
        stickmanX = width / 2;
        stickmanY = height / 2 + 60;

        // Initialize ArrayLists
        clouds = new ArrayList<>();
        flowers = new ArrayList<>();
        butterflies = new ArrayList<>();
        grass = new ArrayList<>();
    }

    public void draw() {
        updateBackground();
        // Draws Clouds
        if (mouseY < height / 2 - height / 15 && mousePressed) {
            float[] cloud = { mouseX, mouseY };
            clouds.add(cloud);

            noStroke();
            fill(255, 255, 255);
            ellipse(mouseX, mouseY, 80, 40);
        }

        // Redraws Clouds
        for (float[] cloud : clouds) {
            noStroke();
            fill(255, 255, 255);
            ellipse(cloud[0], cloud[1], 80, 40);
        }

        // Draw Stickman
        drawStickman(stickmanX, stickmanY);

        // Handle Stickman Movement
        handleMovement();

        // Draw Grass
        for (float[] grassBlade : grass) {
            noStroke();
            fill(0, 128, 0);
            triangle(grassBlade[0], grassBlade[1], grassBlade[0] + 2, grassBlade[1] - grassBlade[2], grassBlade[0] + 5, grassBlade[1]);
        }

        // Draw Flowers
        for (float[] flower : flowers) {
            drawFlower(flower[0], flower[1]);
        }


        // Draw Butterfly
        for (float[] butterfly : butterflies) {
            drawButterfly(butterfly[0], butterfly[1]);
        }
    }

    public void drawStickman(int x, int y) {
        strokeWeight(1);
        stroke(0);
        fill(255);
        // Body
        line(x, y - 30, x, y + 30);
        // Head
        ellipse(x, y - 40, 20, 20);
        // Arms
        line(x - 10, y, x + 10, y);
        // Legs
        line(x, y + 30, x - 10, y + 50);
        line(x, y + 30, x + 10, y + 50);
    }

    public void handleMovement() {
        if (wKey) {
            // Check if moving up won't go above the grass
            if (stickmanY - speed >= height / 2 - 50) {
                stickmanY -= speed;
            }
        }
        if (sKey) {
            // Check if moving down won't go below the canvas
            if (stickmanY + speed <= height - 50) {
                stickmanY += speed;
            }
        }
        if (aKey) {
            // Check if moving left won't go out the canvas
            if (stickmanX - speed > 15) {
                stickmanX -= speed;
            }
        }
        if (dKey) {
            // Check if moving down won't go below the canvas
            if (stickmanX + speed < width - 15) {
                stickmanX += speed;
            }
        }
    }

    public void drawFlower(float x, float y) {
        // Flower Stem
        stroke(0, 128, 0);
        strokeWeight(4);
        line(x, y, x, y + width / 10);

        // Flower Petals
        int numPetals = 6;
        float petalSize = 15;  // Fixed petal size

        noStroke();
        fill(255, 100, 100);

        // Draw multiple petals around the center of the flower
        for (float angle = 0; angle < TWO_PI; angle += TWO_PI / numPetals) {
            float petalX = x + cos(angle) * 15;
            float petalY = y + sin(angle) * 15;
            ellipse(petalX, petalY, petalSize, petalSize);
        }

        // Center of Flower
        fill(255, 255, 0);
        noStroke();
        ellipse(x, y, petalSize, petalSize);
    }

    public void drawButterfly(float x, float y) {
        // Draw butterfly antennae
        strokeWeight(1);
        stroke(0);
        line(x, y, x - 5, y - 5);
        line(x - 1, y, x + 5, y - 5);

        // Draw butterfly body
        noStroke();
        fill(255, 229, 180);
        ellipse(x, y + 5 / 2, 5, 5);
        rect(x - 5 / 2, y + 5 / 2, 5, 15);
        ellipse(x, y + 35 / 2, 5, 5);

        // Draw butterfly wings
        fill(100, 200, 100);

        // Upper wings
        pushMatrix();
        translate(x + 11, y + 15 / 2);
        rotate(radians(-45));
        ellipse(0, 0, 20, 25 / 2);
        popMatrix();

        pushMatrix();
        translate(x - 10, y + 15 / 2);
        rotate(radians(45));
        ellipse(0, 0, 20, 25 / 2);
        popMatrix();

        // Lower wings
        pushMatrix();
        translate(x + 11, y + 25 / 2);
        rotate(radians(-90));
        ellipse(0, 0, 15, 25 / 2);
        popMatrix();

        pushMatrix();
        translate(x - 10, y + 25 / 2);
        rotate(radians(90));
        ellipse(0, 0, 15, 25 / 2);
        popMatrix();

        // Duplicated to remove inner stroke
        noStroke();
        pushMatrix();
        translate(x + 11, y + 15 / 2);
        rotate(radians(-45));
        ellipse(0, 0, 20, 25 / 2);
        popMatrix();

        pushMatrix();
        translate(x - 10, y + 15 / 2);
        rotate(radians(45));
        ellipse(0, 0, 20, 25 / 2);
        popMatrix();
    }

    /**
     * Called when the mouse is dragged. Draws grass at the current mouse position.
     */
    public void mouseDragged() {
        if (mouseY > height / 2) {
            // Check if the time interval has passed since the last grass drawing
            if (millis() - intLastTime > intInterval) {
                // Draws Grass
                float[] grassBlade = { mouseX - 1, mouseY, random(5, 15) };
                grass.add(grassBlade);

                // Update the last drawing time
                intLastTime = millis();
            }
        }
    }

    /**
     * Called when the mouse wheel is scrolled. Draws a butterfly at the mouse position.
     */
    public void mouseWheel() {
        float[] butterfly = { mouseX, mouseY };
        butterflies.add(butterfly);
    }

    /**
     * Called when the mouse is clicked. Draws a flower at the mouse position.
     */
    public void mouseClicked() {
        if (mouseY > height / 2 - height / 10) {
            float[] flower = { mouseX, mouseY };
            flowers.add(flower);
        }
    }

    /**
     * Called when a key is pressed. Adjusts the sky color based on the arrow keys.
     */
    public void keyPressed() {
        if (keyCode == LEFT) {
            // Pressing left arrow key brightens the sky
            adjustSky(10);
        } else if (keyCode == RIGHT) {
            // Pressing right arrow key darkens the sky
            adjustSky(-10);
        }

        if (key == 'w' || key == 'W') {
            wKey = true;
        } else if (key == 's' || key == 'S') {
            sKey = true;
        } else if (key == 'a' || key == 'A') {
            aKey = true;
        } else if (key == 'd' || key == 'D') {
            dKey = true;
        }
    }

    public void keyReleased() {
        if (key == 'w' || key == 'W') {
            wKey = false;
        } else if (key == 's' || key == 'S') {
            sKey = false;
        } else if (key == 'a' || key == 'A') {
            aKey = false;
        } else if (key == 'd' || key == 'D') {
            dKey = false;
        }
    }

    /**
     * Adjusts the sky color based on the given adjustment.
     *
     * @param adjustment The amount to adjust the blue component of the sky color.
     */
    private void adjustSky(int adjustment) {
        float blueValue = blue(skyColor);
        skyColor = color(red(skyColor), green(skyColor), blueValue);
        blueValue = constrain(blueValue + adjustment, 0, 200);
        skyColor = color(red(skyColor), green(skyColor), blueValue);

        updateBackground();
    }

    /**
     * Updates the background with the current sky color and draws the ground.
     */
    private void updateBackground() {
        background(skyColor);

        // Ground
        noStroke();
        fill(0, 100, 0);
        rect(0, height / 2, width, height);
    }
}
