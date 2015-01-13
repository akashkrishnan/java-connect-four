package net.aakay;

public class Main {

  public static void main(String[] args) {
    Board b = new Board(6, 7, 4);
    Player p = new Computer(8);
    while (b.currentPlayer() > 0) {
      b.makeMove(p);
      System.out.println(b.score(1));
      System.out.println(b);
    }
    System.out.println("Winner: " + b.winningPlayer());
  }

}
