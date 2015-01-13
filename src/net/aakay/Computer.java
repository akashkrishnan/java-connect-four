package net.aakay;

import java.util.List;

public class Computer extends Player {

  private final int depth;
  private int p;

  public Computer(int depth) {
    this.depth = depth;
  }

  @Override
  public int getMove(Board b) {
    this.p = b.currentPlayer();
    List<Integer> moves = b.validMoves();
    int M = moves.get(0);
    int v = Integer.MIN_VALUE;
    for (int m : moves) {
      b.makeMove(m);
      int nv = this.minimax(b, this.depth - 1, v, Integer.MAX_VALUE);
      b.undoMove();
      if (nv > v) {
        v = nv;
        M = m;
      }
    }
    return M;
  }

  private int minimax(Board b, int d, int min, int max) {
    List<Integer> moves = b.validMoves();
    if (b.currentPlayer() == 0 || d == 0) return b.score(this.p);
    if (b.currentPlayer() == 1) {
      int v = min;
      for (int m : moves) {
        b.makeMove(m);
        int nv = this.minimax(b, d - 1, v, max);
        b.undoMove();
        if (nv > v) v = nv;
        if (v > max) return max;
      }
      return v;
    } else if (b.currentPlayer() == 2) {
      int v = max;
      for (int m : moves) {
        b.makeMove(m);
        int nv = this.minimax(b, d - 1, min, v);
        b.undoMove();
        if (nv < v) v = nv;
        if (v < min) return min;
      }
      return v;
    }
    throw new RuntimeException("Implementation error.");
  }

}
