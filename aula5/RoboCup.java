import lejos.nxt.*;
import lejos.util.*;
import lejos.robotics.navigation.*;
import lejos.robotics.mapping.*;
import lejos.robotics.pathfinding.*;
import lejos.robotics.localization.*;
import lejos.geom.*;

enum Wall {
  FREE,
  BLOCKED,
  OPEN,
  CLOSED
}

public class RoboCup {
  static DifferentialPilot pilot;
  static Navigator navigator;
  static OdometryPoseProvider poseProvider;

  static Map map;
  static Path path;
  static ShortestPathFinder pathFinder;

  private static void initPilot() {
    pilot = new DifferentialPilot(5.6f, 11.2f, Motor.A, Motor.B, false);
    pilot.setTravelSpeed(15);
    pilot.setRotateSpeed(30);

    navigator = new Navigator(pilot);
    poseProvider = new OdometryPoseProvider(pilot);
  }

  private static void initMap() {
    map = new Map();
  }

  private static void goTo(int[] destination) {
    float[] coords = map.setDestination(destination);
    LineMap lineMap = map.getLineMap();

    //try {
    //  lineMap.createSVGFile("./map.svg");
    //} catch (Exception e) {
    //  System.out.println(e);
    //}

    pathFinder = new ShortestPathFinder(lineMap);

    LCD.clear();
    LCD.drawString("Calculating shortest path", 0, 0);

    try {
      path = pathFinder.findRoute(
        poseProvider.getPose(),
        new Waypoint(coords[0], coords[1])
      );
      navigator.setPath(path);
      navigator.followPath();
      navigator.waitForStop();
    } catch (Exception e) {
      LCD.clear();
      LCD.drawString("FAILED TO FIND PATH", 0, 0);
      System.out.println("COuld not find path\n");
      System.out.println(e);
    Button.waitForAnyPress();
    }
  }

  private static void goBack() {
    //ArrayList<Waypoint> newPath = new ArrayList<Waypoint>();

    //for(Waypoint point : path)
    //  newPath.add(point);
    //java.util.Collections.reverse(newPath);
    //
    //navigator.setPath(path);
    //navigator.followPath();
    //

    LCD.clear();
    LCD.drawString("Calculating shortest path", 0, 0);

    try {
      Pose pose = poseProvider.getPose();

      path = pathFinder.findRoute(
        pose,
        new Waypoint(0, pose.getY() > 80 ? (pose.getX() < 50 ? 135 : 105) : 45)
      );
      navigator.setPath(path);
      navigator.followPath();
      navigator.waitForStop();
    } catch (Exception e) {
      LCD.clear();
      LCD.drawString("FAILED TO FIND PATH", 0, 0);
      System.out.println("COuld not find path\n");
      System.out.println(e);
    Button.waitForAnyPress();
    }
  }

  public static void main(String[] args) {
    System.out.println("Running");
    ButtonCounter counter = new ButtonCounter();
    counter.count("Pressione o botão esquerdo");
    int count = counter.getLeftCount();
    //int count = 6;

    initPilot();
    initMap();

    int[] destination;
    switch(count){
     case 0:
       destination = Map.LIBRARY;
       break;
     case 1:
       destination = Map.MUSEUM;
       break;
     case 2:
       destination = Map.CITY_HALL;
       break;
     case 3:
       destination = Map.DRUGSTORE;
       break;
     case 4:
       destination = Map.SCHOOL;
       break;
     case 5:
       destination = Map.BAKERY;
       break;
     case 6:
     default:
       destination = Map.PARK;
       break;
    }

    LCD.clear();
    LCD.drawString("Going to", 0, 0);

      //System.out.println(Integer.toString(destination[0]));
    goTo(destination);

    LCD.clear();
    LCD.drawString("Going back", 0, 0);

    goBack();

    LCD.clear();
    LCD.drawString("DONE", 0, 0);

    Button.waitForAnyPress();
  }
}

class Cell {
  Wall[] walls;

  static float width = 30f;
  static float height = 30f;
  static float doorLength = 15f;
  static float thickness = 3f;
  static float margin = 11.2f;

  public Cell(Wall w1, Wall w2, Wall w3, Wall w4) {
    walls = new Wall[]{w1, w2, w3, w4};
  }

  public void closeAllDoors() {
    for (int i = 0; i < 4; ++i) {
      if (walls[i] != Wall.OPEN) continue;
      walls[i] = Wall.CLOSED;
    }
  }

  public void openAllDoors() {
    for (int i = 0; i < 4; ++i) {
      if (walls[i] != Wall.CLOSED) continue;
      walls[i] = Wall.OPEN;
    }
  }

  public double[][] createRectangle(double[] scale, double rotate, double[] translate) {
    double[][] rectangle = {
      {-.5, -.5},
      { .5, -.5},
      { .5,  .5},
      {-.5,  .5},
      {-.5, -.5},
    };

    double c = Math.cos(rotate);
    double s = Math.sin(rotate);

    translate[0] += (translate[0] < 0) ? 0.2f : -0.2f;
    translate[1] += (translate[1] < 0) ? 0.2f : -0.2f;

    for (int i = 0; i < rectangle.length; ++i) {
      double[] p = rectangle[i];
      p[0] = c * scale[0] * p[0] - s * scale[1] * p[1] + translate[0];
      p[1] = s * scale[0] * p[0] + c * scale[1] * p[1] + translate[1];
    }

    //java.awt.geom.Rectangle2D.Double rectangle = new java.awt.geom.Rectangle2D.Double(-.5, -.5, 1., 1.);

    //java.awt.geom.PathIterator it = rectangle.getPathIterator(
    //  new java.awt.geom.AffineTransform(
    //     c * scale[0], s * scale[0],
    //    -s * scale[1], c * scale[1],
    //    translate[0], translate[1]
    //  )
    //);

    //int i = 0; while (!it.isDone()) {
    //  double[] coords = new double[6];

    //  switch(it.currentSegment(coords)) {
    //    case java.awt.geom.PathIterator.SEG_MOVETO:
    //    case java.awt.geom.PathIterator.SEG_LINETO:
    //      lines[i++] = coords;
    //      //System.out.println(String.format("%f %f", lines[i-1][0], lines[i-1][1]));
    //      break;
    //    case java.awt.geom.PathIterator.SEG_CLOSE:
    //    default:
    //  }

    //  it.next();
    //}

    return rectangle;
  }

  //public void extractRectangleLines(List<Line> lines, double[][] rectangle, double tx, double ty) {
  public int extractRectangleLines(Line[] lines, int line_index, double[][] rectangle, double tx, double ty) {
    for (int j = 1; j < rectangle.length; ++j) {
      double[] p1 = rectangle[j - 1];
      double[] p2 = rectangle[j];
      if (p1[0] * p1[0] < 50 || p1[1] * p1[1] < 50 || p2[0] * p2[0] < 50 || p2[1] * p2[1] < 50)
        continue;
      lines[line_index++] = new lejos.geom.Line(
        (float) (p1[0] + tx),
        (float) (p1[1] + ty),
        (float) (p2[0] + tx),
        (float) (p2[1] + ty)
      );
    }
    return line_index;
  }

  public Line[] getLines(float[] position) {
    float x = position[0];
    float y = position[1];

    Line[] lines = new Line[50];
    int line_index = 0;

    for (int i = 0; i < 4; ++i) {
      Wall wall = walls[i];
      boolean even = (i % 2) == 0;
      boolean first = (i < 2);

      switch(wall) {
        case OPEN:
          double[][] rectangle = createRectangle(
            new double[]{
              (even ? thickness : (width - doorLength) * .5) + margin,
              (even ? (height - doorLength) * .5 : thickness) + margin
            },
            0,
            new double[]{
              even ? (first ? 1 : -1) * (width - thickness) * .5 : 0,
              even ? 0 : (first ? 1 : -1) * (height - thickness) * .5
            }
          );
          line_index = extractRectangleLines(
            lines,
            line_index,
            rectangle,
            x + (even ? 0 : width * .5 - (width - doorLength) * .25),
            y + (even ? height * .5 - (height - doorLength) * .25 : 0)
          );
          line_index = extractRectangleLines(
            lines,
            line_index,
            rectangle,
            x + (even ? 0 : - width * .5 + (width - doorLength) * .25),
            y + (even ? - height * .5 + (height - doorLength) * .25 : 0)
          );
          break;
        case BLOCKED:
        case CLOSED:
          line_index = extractRectangleLines(
            lines,
            line_index,
            createRectangle(
              new double[]{
                (even ? thickness : width) + margin,
                (even ? height : thickness) + margin
              },
              0,
              new double[]{
                even ? (first ? 1 : -1) * (width - thickness) * .5 : 0,
                even ? 0 : (first ? 1 : -1) * (height - thickness) * .5
              }
            ),
            x,
            y
          );
          break;
        case FREE:
        default:
      }
    }

    Line[] final_lines = new Line[line_index];

    for (int i = 0; i < line_index; ++i) {
      final_lines[i] = lines[i];
    }

    return final_lines; //lines.toArray(new Line[0]);
  }
}

class Map {
  Line[] mapLines;
  boolean[] obstaclesBitmap;
  Cell[][] cells = {
    {
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.BLOCKED , Wall.FREE    , Wall.OPEN    , Wall.OPEN    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.BLOCKED , Wall.FREE    , Wall.OPEN    , Wall.OPEN    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.OPEN    , Wall.FREE    )
    },
    {
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.BLOCKED , Wall.FREE    )
    },
    {
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.BLOCKED , Wall.OPEN    , Wall.BLOCKED , Wall.OPEN    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.OPEN    , Wall.BLOCKED , Wall.OPEN    , Wall.BLOCKED ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.OPEN    , Wall.FREE    )
    },
    {
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.BLOCKED , Wall.FREE    )
    },
    {
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.OPEN    , Wall.OPEN    , Wall.BLOCKED , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.OPEN    , Wall.OPEN    , Wall.BLOCKED , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.FREE    , Wall.FREE    ),
      new Cell(Wall.FREE    , Wall.FREE    , Wall.OPEN    , Wall.FREE    )
    }
  };
  lejos.geom.Rectangle bounds = new lejos.geom.Rectangle(0, 0, 150 + 18, 150);

  static int[] LIBRARY   = {0, 1};
  static int[] MUSEUM    = {0, 3};
  static int[] CITY_HALL = {2, 1};
  static int[] DRUGSTORE = {2, 3};
  static int[] SCHOOL  = {4, 1};
  static int[] BAKERY  = {4, 3};
  static int[] PARK  = {4, 5};

  public Map() {}

  public float[] setDestination(int[] position) {
    for (int i = 0; i < cells.length; ++i) {
      for (int j = 0; j < cells[0].length; ++j) {
        Cell cell = cells[i][j];
        if (position[0] == i && position[1] == j || position[0] < 0 && j == 5) {
          cell.openAllDoors();
        } else {
          cell.closeAllDoors();
        }
      }
    }
    return getCoord(position[0], position[1]);
  }

  public float[] getCoord(int i, int j) {
    return new float[]{
      (j + 0.5f) * Cell.width,
      ((cells.length - i - 1) + 0.5f) * Cell.height
    };
  }

  public LineMap getLineMap() {
    //List<Line> lines = new ArrayList<Line>();
    Line[] lines = new Line[200];
    int line_index = 0;

    for (int i = 0; i < cells.length; ++i) {
      for (int j = 0; j < cells[0].length; ++j) {
        Cell cell = cells[i][j];
        Line[] cellLines = cell.getLines(getCoord(i, j));
        for (int k = 0; k < cellLines.length; ++k) {
          lines[line_index++] = cellLines[k];
        }
      }
    }

    Line[] final_lines = new Line[line_index + 3];

    for (int i = 0; i < line_index; ++i) {
      final_lines[i] = lines[i];
    }

    final_lines[line_index+0] = new Line(0f, 0f, 168f, 0f);
    final_lines[line_index+1] = new Line(168f, 0f, 168f, 168f);
    final_lines[line_index+2] = new Line(168f, 168f, 0f, 168f);
    //final_lines[line_index+3] = new Line(0f, 168f, 0f, 0f);

    return new LineMap(final_lines, bounds); //lines.toArray(new Line[0]);
    //return new LineMap(lines.toArray(new Line[0]), bounds);
  }
}
