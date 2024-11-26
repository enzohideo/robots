// nxjpcc MultiFile{,Dep}.java
// nxjpc MultiFile
//
// pc MultiFile{,Dep}.java

class MultiFile {
  public static void main(String[] args) {
    MultiFileDep world = new MultiFileDep(); String me = "world";
    System.out.printf("Hello %s\n", world.execute(me));
  }
}
