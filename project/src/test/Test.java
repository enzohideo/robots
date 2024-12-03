import java.util.Stack;

public class Test {
  static final String CLEAR = "\u001B[0m";
  static final String YELLOW = "\u001B[33m";
  static final String GREEN = "\u001B[32m";
  static final String RED = "\u001B[31m";

  static int passedCount = 0;
  static int failedCount = 0;
  static Stack<Iteration> iterations = new Stack<Iteration>();

  static String separator = "%s%s %s\n";

  static class Iteration {
    protected boolean success = true;
    protected int passedCount = 0;
    protected int failedCount = 0;

    String name = "";

    public Iteration(String name) {
      this.name = name;
    }

    public void failed() {
      ++failedCount;
      this.success = false;
    }

    public void passed() {
      ++passedCount;
    }

    public void merge(Iteration it) {
      this.passedCount += it.passedCount;
      if (!it.success) {
        this.success = false;
        this.failedCount += it.failedCount;
      }
    }

    public boolean result() {
      if (success)
        System.out.printf(separator, GREEN, "PASSED " + passedCount + " - " + name, CLEAR);
      else
        System.out.printf(separator, RED, "FAILED " + failedCount + " - " + name, CLEAR);

      return success;
    }
  }

  static public boolean check(String name, boolean assertion) {
    if (!assertion)
      failed(name);
    else
      iterations.peek().passed();
    return assertion;
  }

  static public void passed(String testName) {
    System.out.printf("%sPASSED:%s %s\n", GREEN, CLEAR, testName);
    iterations.peek().passed();
  }

  static public void failed(String testName) {
    System.out.printf("%sFAILED:%s %s\n", RED, CLEAR, testName);
    iterations.peek().failed();
  }

  static public void start(String testName) {
    iterations.push(new Iteration(testName));
  }

  static public void end() {
    Iteration it = iterations.pop(); it.result();
    if (!iterations.isEmpty())
      iterations.peek().merge(it);
  }

  public static void main(String[] args) {
    start("All");
    TestArena.main(args);
    TestNode.main(args);
    end();
  }
}
