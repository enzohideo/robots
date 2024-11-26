class XORSwap {
  public static void main(String[] args) {
    int[] list = {1, 2, 3, 5, 6, 7};

    for (int i = 0; i < list.length / 2; ++i) {
      int j = list.length - i - 1;
      list[i] = list[i] ^ list[j];
      list[j] = list[i] ^ list[j];
      list[i] = list[i] ^ list[j];
    }

    for (int value : list) {
      System.out.printf("%d\n", value);
    }
  }
}
