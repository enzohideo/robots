public class Test {
  static final String CLEAR = "\u001B[0m";
  static final String YELLOW = "\u001B[33m";
  static final String GREEN = "\u001B[32m";
  static final String RED = "\u001B[31m";

  static public boolean check(String name, boolean assertion) {
    if (!assertion)
      failed(name);
    return assertion;
  }

  static public void passed(String testName) {
    System.out.printf("%sPASSED:%s %s\n", GREEN, CLEAR, testName);
  }

  static public void failed(String testName) {
    System.out.printf("%sFAILED:%s %s\n", RED, CLEAR, testName);
  }

  static public void name(String testClass) {
    System.out.printf("%s------------- %s -------------%s\n", YELLOW, testClass, CLEAR);
  }

  public static void main(String[] args) {
    TestArena.main(args);
  }
}
