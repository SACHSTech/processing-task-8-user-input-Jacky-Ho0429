import processing.core.PApplet;

/**
 * Interactive Nature & Stickman
 * @author Jacky Ho
 */
public class Sketch extends PApplet {

    // Stickman variables
    int stickmanX, stickmanY;
    float speed = 5;
    boolean wKey, sKey, aKey, dKey = false;

    // Timing variables
    int intInterval = 100;
    int intLastTime = 0;   

    // Color variables
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
        updateBackground();
    }

    /**
     * Main draw function that is continuously called to update and render the scene.
     */
    public void draw() {
        float cloudWidth = random(50, 100);
        float cloudHeight = random(25, 50);

        // Draws Clouds
        if (mouseY < height / 2 - height / 15 && mousePressed) {
            noStroke();
            fill(255, 255, 255);
            ellipse(mouseX, mouseY, cloudWidth, cloudHeight);
        }

        // Draw stickman and handle movement
        if (keyPressed) {
            // Change stickman speed
            if (key == 'm' || key == 'M') {
                speed = random(1, 10);
            }
        }
        drawStickman(stickmanX, stickmanY);
        handleMovement();
    }

    /**
     * Draws a stickman at the specified coordinates.
     *
     * @param x X-coordinate of the stickman
     * @param y Y-coordinate of the stickman
     */
    public void drawStickman(int x, int y) {
        strokeWeight(1);
        noStroke();
        // Background to hide old drawings
        fill(0, 100, 0);
        rect(x - 15, y - 60, 35, 120);
        
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

    /**
     * Handles stickman movement based on key inputs.
     */
    public void handleMovement() {
        if (wKey) {
            if (stickmanY - speed >= height / 2 + 60) {
            stickmanY -= speed;
            }
        }
        if (sKey) {
            if (stickmanY + speed < height - 50) {
            stickmanY += speed;
            }
        }
        if (aKey) {
            if (stickmanX - speed > 15) {
            stickmanX -= speed;
            }
        }
        if (dKey) {
            if (stickmanX + speed < width - 15) {
            stickmanX += speed;
            }
        }
    }

    /**
     * Handles mouse wheel events to draw butterfly elements.
     */
    public void mouseWheel() {
        float wingColorRed = random(255);
        float wingColorGreen = random(255);
        float wingColorBlue = random(255);

        if (mouseY < height / 2 - height / 10) {
            // Draw butterfly antennae
            strokeWeight(1);
            stroke(0);
            line(mouseX, mouseY, mouseX - 5, mouseY - 5);
            line(mouseX - 1, mouseY, mouseX + 5, mouseY - 5);

            // Draw butterfly body
            noStroke();
            fill(255, 229, 180);
            ellipse(mouseX, mouseY + 5 / 2, 5, 5);
            rect(mouseX - 5 / 2, mouseY + 5 / 2, 5, 15);
            ellipse(mouseX, mouseY + 35 / 2, 5, 5);

            // Draw butterfly wings
            stroke(0);
            fill(wingColorRed, wingColorGreen, wingColorBlue);

            // Upper wings
            pushMatrix();
            translate(mouseX + 11, mouseY + 15 / 2);
            rotate(radians(-45));
            ellipse(0, 0, 20, 25 / 2);
            popMatrix();

            pushMatrix();
            translate(mouseX - 10, mouseY + 15 / 2);
            rotate(radians(45));
            ellipse(0, 0, 20, 25 / 2);
            popMatrix();

            // Lower wings
            pushMatrix();
            translate(mouseX + 11, mouseY + 25 / 2);
            rotate(radians(-90));
            ellipse(0, 0, 15, 25 / 2);
            popMatrix();

            pushMatrix();
            translate(mouseX - 10, mouseY + 25 / 2);
            rotate(radians(90));
            ellipse(0, 0, 15, 25 / 2);
            popMatrix();

            // Duplicated to remove inner stroke
            noStroke();
            pushMatrix();
            translate(mouseX + 11, mouseY + 15 / 2);
            rotate(radians(-45));
            ellipse(0, 0, 20, 25 / 2);
            popMatrix();

            pushMatrix();
            translate(mouseX - 10, mouseY + 15 / 2);
            rotate(radians(45));
            ellipse(0, 0, 20, 25 / 2);
            popMatrix();
        }
    }

    /**
     * Called when the mouse is dragged. Draws grass at the current mouse position.
     */
    public void mouseDragged() {
        if (mouseY > height / 2) {
            // Check if the time interval has passed since the last grass drawing
            if (millis() - intLastTime > intInterval) {
                // Draws Grass
                noStroke();
                for (int i = 0; i < width; i += 5) {
                    float grassHeight = random(5, 15);
                    fill(0, 128, 0);
                    triangle(mouseX - 1, mouseY, mouseX + 2, mouseY - grassHeight, mouseX + 5, mouseY);
                }

                // Update the last drawing time
                intLastTime = millis();
            }
        }
    }

    /**
     * Called when the mouse is clicked. Draws a flower at the mouse position.
     */
    public void mouseClicked() {
        if (mouseY > height / 2 - height / 10) {
            // Flower Stem
            stroke(0, 128, 0);
            strokeWeight(4);
            line(mouseX, mouseY, mouseX, mouseY + width / 10);

            // Flower Petals
            int numPetals = 6;
            float petalColorRed = random(255);
            float petalColorGreen = random(255);
            float petalColorBlue = random(255);
            float petalSize = random(width / 50, width / 20);
            float angleIncrement = TWO_PI / numPetals;

            noStroke();
            fill(petalColorRed, petalColorGreen, petalColorBlue);

            // Draw multiple petals around the center of the flower
            for (float angle = 0; angle < TWO_PI; angle += angleIncrement) {
                float petalX = mouseX + cos(angle) * 15;
                float petalY = mouseY + sin(angle) * 15;
                ellipse(petalX, petalY, petalSize, petalSize);
            }

            // Center of Flower
            fill(255, 255, 0);
            noStroke();
            ellipse(mouseX, mouseY, petalSize, petalSize);
        }
    }

    /**
     * Called when a key is pressed. Adjusts the sky color based on the arrow keys and move stick figure with WASD.
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

    /**
     * Called when a key is released. Adjusts the sky color based on the arrow keys.
     */
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
