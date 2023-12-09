import processing.core.PApplet;
import processing.core.PImage;

/**
 * This class represents a simple game where a rocket ship can move towards the mouse, shoot projectiles,
 * and destroy randomly generated planets.
 */
public class Sketch extends PApplet {

  // Rocket Ship variables
  float fltRS_X = 400;
  float fltRS_Y = 400;
  float fltRS_Speed = 5;

  // Image variables
  PImage imgRocketShip;
  PImage[] imgPlanet;

  // Planet variables
  int numPlanets = 6;
  Planet[] planets = new Planet[10000000];
  int numDrawnPlanets = 0;

  // Projectile variables
  Projectile[] projectiles = new Projectile[1000000];
  int numProjectiles = 0;

  // Shooting interval
  int interval = 300;
  int lastTime = 0;

  // Star variables
  Star[] stars = new Star[100]; // Adjust the array size based on your needs
  int numStars = 0;


  public void settings() {
    size(500, 500);
  }

  public void setup() {
    // Load Rocket Ship image
    imgRocketShip = loadImage("RocketShip.png");
    imgRocketShip.resize(50, 50);

    // Load Planet images
    imgPlanet = new PImage[numPlanets];
    for (int i = 0; i < numPlanets; i++) {
      imgPlanet[i] = loadImage("Planet" + i + ".png");
    }
  }

  public void draw() {
    // Runs all methods
    background(0);
    movementRocketShip();
    drawPlanets();
    drawProjectiles();
    drawRocketShip();
    drawStars();
  }

  /**
   * Draws all the planets on the screen.
   */
  public void drawPlanets() {
    for (int i = 0; i < numDrawnPlanets; i++) {
      planets[i].display();
    }
  }

  /**
   * Draws all the projectiles on the screen, checks for collisions with planets,
   * and updates the planet health accordingly.
   */
  public void drawProjectiles() {
    for (int i = 0; i < numProjectiles; i++) {
      if (projectiles[i] != null) {
        projectiles[i].update();
        ellipse(projectiles[i].x, projectiles[i].y, 5, 5);

        // Check for collision with planets
        for (int j = 0; j < numDrawnPlanets; j++) {
          if (planets[j].checkCollision(projectiles[i])) {
            projectiles[i] = null; // Remove the projectile
            planets[j].hit(); // Reduce planet health
            if (planets[j].isDestroyed()) {
              removePlanet(j); // Remove the planet if health is zero
            }
            break;
          }
        }
      }
    }
  }

  /**
   * Draws the rocket ship at the current mouse position.
   */
  public void drawRocketShip() {
    float angle = atan2(mouseY - fltRS_Y, mouseX - fltRS_X);
    pushMatrix();
    translate(fltRS_X, fltRS_Y);
    rotate(angle - 10);
    imageMode(CENTER);
    image(imgRocketShip, 0, 0);
    popMatrix();
  }

  /**
   * Spawns a new planet at the mouse position when the mouse is pressed.
   */
  public void mousePressed() {
    if (numDrawnPlanets < planets.length) {
      planets[numDrawnPlanets] = new Planet(mouseX, mouseY);
      numDrawnPlanets++;
    }
  }

  /**
   * Handles the controls of the rocket ship based on user input.
   */
  public void movementRocketShip() {
    if (keyPressed) {
      if (key == 'w' || key == 'W' || keyCode == UP) { // W or Up Arrow to move in direction of mouse
        float angle = atan2(mouseY - fltRS_Y, mouseX - fltRS_X);
        float dx = fltRS_Speed * cos(angle);
        float dy = fltRS_Speed * sin(angle);
        fltRS_X += dx;
        fltRS_Y += dy;
      } else if (key == ' ' || key == ' ') { // Spacebar to shoot
        if (numProjectiles < projectiles.length) {
          float angle = atan2(mouseY - fltRS_Y, mouseX - fltRS_X);
          float dx = fltRS_Speed * cos(angle);
          float dy = fltRS_Speed * sin(angle);
          projectiles[numProjectiles] = new Projectile(fltRS_X, fltRS_Y, dx, dy);

          if (millis() - lastTime > interval) {
            numProjectiles++;
            lastTime = millis();
          }
        }
      }
    }
  }

  /**
   * Removes a planet from the array, shifting the remaining planets accordingly.
   *
   * @param index The index of the planet to be removed.
   */
  public void removePlanet(int index) {
    for (int i = index; i < numDrawnPlanets - 1; i++) {
      planets[i] = planets[i + 1];
    }
    numDrawnPlanets--;
  }

  /**
   * Inner class representing a planet in the game.
   */
  public class Planet {
    float x, y;
    PImage img;
    int health = 1;

    /**
     * Constructor for the Planet class.
     *
     * @param x The x-coordinate of the planet.
     * @param y The y-coordinate of the planet.
     */
    Planet(float x, float y) {
      this.x = x;
      this.y = y;
      int randomPlanet = floor(random(numPlanets));
      this.img = imgPlanet[randomPlanet];
    }

    /**
     * Displays the planet on the screen.
     */
    public void display() {
      imageMode(CENTER);
      image(img, x, y);
    }

    /**
     * Reduces the health of the planet when hit by a projectile.
     */
    public void hit() {
      health--;
    }

    /**
     * Checks if the planet is destroyed (health <= 0).
     *
     * @return True if the planet is destroyed, false otherwise.
     */
    boolean isDestroyed() {
      return health <= 0;
    }

    /**
     * Checks for collision with a projectile.
     *
     * @param p The projectile to check for collision.
     * @return True if a collision occurs, false otherwise.
     */
    boolean checkCollision(Projectile p) {
      float d = dist(x, y, p.x, p.y);
      return d < img.width / 2 + 2.5;
    }
  }

  /**
   * Inner class representing a projectile in the game.
   */
  public class Projectile {
    float x, y;
    float speed = 2; // Reduce the speed
    float dx, dy;

    /**
     * Constructor for the Projectile class.
     *
     * @param x  The initial x-coordinate of the projectile.
     * @param y  The initial y-coordinate of the projectile.
     * @param dx The x-component of the projectile's velocity.
     * @param dy The y-component of the projectile's velocity.
     */
    Projectile(float x, float y, float dx, float dy) {
      this.x = x;
      this.y = y;
      this.dx = dx;
      this.dy = dy;
    }

    /**
     * Updates the position of the projectile based on its velocity.
     */
    public void update() {
      x += dx * speed;
      y += dy * speed;
    }
  }

  /**
   * Runs the methods spawnStar if key 'o' is pressed
   */
  public void keyPressed() {
    if (key == 'o' || key == 'O') {
      spawnStar();
    }
  }

  /**
   * Sets position of newly made star
   */
  public void spawnStar() {
    if (numStars < stars.length) {
      float x = random(width);
      float y = random(height);
      float size = random(5, 20); // Adjust the size range based on your needs
      stars[numStars++] = new Star(x, y, size);
    }
  }

  /**
   * Displays each generated star
   */
  public void drawStars() {
    for (int i = 0; i < numStars; i++) {
      stars[i].display();
    }
  }

  /**
   * The Star class represents a star in the game.
   * Each star is defined by its x and y coordinates and size.
   */
  public class Star {
    float x, y;
    float size;

    Star(float x, float y, float size) {
      this.x = x;
      this.y = y;
      this.size = size;
    }

    public void display() {
      fill(255); // Set star color to white
      noStroke();
      ellipse(x, y, size, size);
    }
  }
}
