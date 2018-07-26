import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class conwaysgol extends PApplet {

/* Conway's Game of Life
 Left/right arrow keys: decrement/increment square size
 'R': Start 
 'S': Stop
 'C': clear cells */

int rate = 60;

public void setup() {
  
//  fullScreen();
}

int WIDTH = 800; // if I simply call 'width' and 'height' in what follows, for some reason it does not work...
int HEIGHT = 800;
int cellSize = 10;

int matrixW = WIDTH/cellSize; // width of matrix
int matrixL = HEIGHT/cellSize; // length of matrix

int[][] matrix1 = new int[matrixW][matrixL];

int[][] matrix2 = new int[matrixW][matrixL];

int dim=2;
boolean running = false;
boolean displayHelp = true;

public void draw() {
  background(0);
  PFont font = loadFont("Courier-Bold-48.vlw");
  textFont(font);
  textSize(12);
  fill(255);
  text("SQUARE SIZE: " + dim + "x" + dim + "\n" +
    "SPEED: " + rate + " fps\n", 20, 20);
  if (displayHelp) {
    text("LMB to set up cells\n" +
      "RIGHT/LEFT arrow keys to change the square size\n" +
      "UP/DOWN arrow keys to change the speed\n" +
      "'R' to run\n" +
      "'S' to stop\n" +
      "'C' to clear grid\n" +
      "'H' to display/hide key binding list", 20, 44);
  }
  if (running) {
    frameRate(rate);
    for (int x = 0; x < matrixW; x ++) {
      for (int y = 0; y < matrixL; y ++) {
        int n = countNeighbours1(x, y);
        if ((n == 3 || n == 2) && matrix1[x][y] == 1) {
          matrix2[x][y] = 1;
        }
        if ((n<2 || n>3) && matrix1[x][y] == 1) {
          matrix2[x][y] = 0;
        }
        if (n==3 && matrix1[x][y] == 0) {
          matrix2[x][y] = 1;
        }
        if ((n>3 || n<3) && matrix1[x][y] == 0) {
          matrix2[x][y] = 0;
        }

        //We are only displaying data from one matrix1; matrix2 is the dummy variable.
      }
    }
  }
  else{
    frameRate(60);
  }

  for (int x = 0; x < matrixW; x++) {
    for (int y = 0; y < matrixL; y++) {
      if (matrix2[x][y] == 1) {
        matrix1[x][y] = 1;
      }
      if (matrix2[x][y] == 0) {
        matrix1[x][y] = 0;
      }
      updateMatrix1(x, y);
    }
  }
  if (keyPressed){
    if (keyCode == UP && rate <= 59){
      rate += 1;
    } else if (keyCode == DOWN && rate >= 6){
      rate -= 1;
    }
  }
}

public void keyReleased() {
  if (key == 'R' || key == 'r') {
    running = true;
  } else if (key == 'P' || key == 's') {
    running = false;
  } else if (key == 'C' || key == 'c') {
    clearMatrix();
  } else if (key == 'H' || key == 'h') {
    displayHelp = !displayHelp;
  } else if (key == CODED) {
    if (keyCode == RIGHT && dim < matrixW) {
      dim += 1;
    } else if (keyCode == LEFT && dim >= 2) {
      dim -= 1;
    }
  }
}

public void updateMatrix1(int x, int y) {
  if (matrix1[x][y] == 1) {
    fill(0, 255, 0, 100);
    stroke(0, 0, 100);
    strokeWeight(2);
    strokeCap(ROUND);
    rect(x*cellSize, y*cellSize, cellSize, cellSize);
  }
}

public int countNeighbours1(int x, int y) {
  return matrix1[(x+1)%matrixW][y]
    + matrix1[(x+1)%matrixW][(y+1)%matrixL]
    + matrix1[x][(y+1)%matrixL]
    + matrix1[(x+matrixW-1)%matrixW][(y+1)%matrixL]
    + matrix1[(x+matrixW-1)%matrixW][y]
    + matrix1[(x+matrixW-1)%matrixW][(y+matrixL-1)%matrixL]
    + matrix1[x][(y+matrixL-1)%matrixL]
    + matrix1[(x+1)%matrixW][(y+matrixL-1)%matrixL] ;
}

// TODO: allow for more complicated patterns in initializeMatrix

public void initializeMatrix(int x, int y) {
  if (x >= 1 && y >= 1) { 
    for (int i = 0; i < dim; i+=1) {
      for (int j = 0; j<dim; j+=1) {
        if (running) {
          matrix1[(x+i)%matrixW][(y+j)%matrixL] = 1;
        } else if (!running) {
          matrix2[(x+i)%matrixW][(y+j)%matrixL] = 1;
        }
      }
    }
  }
}

public void clearMatrix() {
  for (int x = 0; x < matrixW; x += 1) {
    for (int y = 0; y < matrixL; y += 1) {
      if (running) {
        matrix1[x][y] = 0;
      } else {
        matrix2[x][y] = 0;
      }
    }
  }
}

public void mousePressed() {
  initializeMatrix(ceil(mouseX/cellSize), ceil(mouseY/cellSize));
}

public void mouseDragged() {
  initializeMatrix(ceil(mouseX/cellSize), ceil(mouseY/cellSize));
}

public void mouseWheel(MouseEvent event) {
  if (event.getCount() == -1.0f && dim < matrixW){
    dim += 1;
  }
  else if (event.getCount() == 1.0f && dim > 1){
    dim -= 1;
  }
}
  public void settings() {  size(800, 800); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "conwaysgol" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
