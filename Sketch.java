import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {

  float fltRS_X = 400;
  float fltRS_Y = 400;
  float fltRS_Speed = 5;

  PImage imgRocketShip;
  PImage[] imgPlanet;

  int numPlanets = 6;
  Planet[] planets = new Planet[10000000];
  int numDrawnPlanets = 0;

  Projectile[] projectiles = new Projectile[1000000];
  int numProjectiles = 0;

  int interval = 300;
  int lastTime = 0; 

  public void settings() {
    size(500, 500);
  }

  public void setup() {
    imgRocketShip = loadImage("RocketShip.png");
    imgRocketShip.resize(50, 50);

    imgPlanet = new PImage[numPlanets];
    for (int i = 0; i < numPlanets; i++) {
      imgPlanet[i] = loadImage("Planet" + i + ".png");
    }
  }

  public void draw() {
    background(0);
    movementRocketShip();
    drawPlanets();
    drawProjectiles();
    drawRocketShip();
  }

  public void drawPlanets() {
    for (int i = 0; i < numDrawnPlanets; i++) {
      planets[i].display();
    }
  }

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

  public void drawRocketShip() {
    float angle = atan2(mouseY - fltRS_Y, mouseX - fltRS_X);
    pushMatrix();
    translate(fltRS_X, fltRS_Y);
    rotate(angle - 10);
    imageMode(CENTER);
    image(imgRocketShip, 0, 0);
    popMatrix();
  }

  public void mousePressed() {
    if (numDrawnPlanets < planets.length) {
      planets[numDrawnPlanets] = new Planet(mouseX, mouseY);
      numDrawnPlanets++;
    }
  }

  public void movementRocketShip() {
    if (keyPressed) {
      if (key == 'w' || key == 'W') {
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

  public void removePlanet(int index) {
    // Shift the remaining planets in the array
    for (int i = index; i < numDrawnPlanets - 1; i++) {
      planets[i] = planets[i + 1];
    }
    // Decrement the count of drawn planets
    numDrawnPlanets--;
  }

  public class Planet {
    float x, y;
    PImage img;
    int health = 1;

    Planet(float x, float y) {
      this.x = x;
      this.y = y;
      int randomPlanet = floor(random(numPlanets));
      this.img = imgPlanet[randomPlanet];
    }

    public void display() {
      imageMode(CENTER);
      image(img, x, y);
    }

    public void hit() {
      health--; // Reduce planet health on hit
    }

    boolean isDestroyed() {
      return health <= 0; // Check if planet is destroyed
    }

    boolean checkCollision(Projectile p) {
      float d = dist(x, y, p.x, p.y);
      return d < img.width / 2 + 2.5; // Increase the radius a bit for better collision detection
    }
  }

  public class Projectile {
    float x, y;
    float speed = 2; // Reduce the speed
    float dx, dy;

    Projectile(float x, float y, float dx, float dy) {
      this.x = x;
      this.y = y;
      this.dx = dx;
      this.dy = dy;
    }

    public void update() {
      x += dx * speed;
      y += dy * speed;
    }
  }

  public static void main(String[] args) {
    PApplet.main("Sketch");
  }
}
