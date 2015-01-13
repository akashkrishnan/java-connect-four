package net.aakay;

import java.util.ArrayList;
import java.util.List;

public class Board {

  private static int[] CHAIN_WEIGHT = {0, 1, 10, 100, 1000};
  private static char[] COINS = {'.', 'X', 'O'};

  private final int rows;
  private final int cols;
  private final int connect;
  private final int[][] board;
  private final int[] height;
  private final List<Integer> moves = new ArrayList();

  private int nMoves;
  private int cp;
  private int wp;

  public Board(int rows, int cols, int connect) {
    this.rows = rows;
    this.cols = cols;
    this.connect = connect;
    this.board = new int[this.cols][this.rows];
    this.height = new int[this.cols];
    this.cp = 1;
  }

  private Board(int rows, int cols, int connect, int[][] board) {

    this.rows = rows;
    this.cols = cols;
    this.connect = connect;
    this.board = new int[this.cols][this.rows];
    this.height = new int[this.cols];

    // Populate board & height
    for (int c = 0; c < this.cols; c++) {
      for (int r = 0; r < this.rows; r++) {
        if (board[c][r] == 0) break;
        this.board[c][r] = board[c][r];
        this.height[c]++;
        this.nMoves++;
      }
    }
    this.cp = this.nMoves % 2 + 1;

  }

  public List validMoves() {
    List<Integer> vm = new ArrayList();
    for (int c = 0; c < this.cols; c++) if (this.height[c] < this.rows) vm.add(c);
    return vm;
  }

  public void makeMove(Player p) {
    this.makeMove(p.getMove(this.copy()));
  }

  public void makeMove(int m) {
    if (this.cp == 0) throw new RuntimeException("Invalid move. Game is over.");
    if (this.height[m] >= this.rows) throw new RuntimeException("Invalid move.");
    this.board[m][this.height[m]++] = this.cp;
    this.moves.add(m);
    this.nMoves++;
    this.cp = this.cp % 2 + 1;
    this.updateStatus();
  }

  public void undoMove() {
    if (this.moves.size() == 0) throw new RuntimeException("Unable to undo move. No moves to undo.");
    int m = this.moves.remove(this.moves.size() - 1);
    this.board[m][--this.height[m]] = 0;
    this.nMoves--;
    this.cp = this.cp % 2 + 1;
  }

  private void updateStatus() {
    if (this.cp == 0) return;
    this.wp = this.score(0);
    if (this.wp != 0 || this.nMoves == this.rows * this.cols) this.cp = 0;
  }

  public int currentPlayer() {
    return this.cp;
  }

  public int winningPlayer() {
    return this.wp;
  }

  public int score(int p) {
    int op = p % 2 + 1;
    int s = 0;
    for (int c = 0; c <= this.cols - this.connect; c++) {
      for (int r = 0; r < this.rows; r++) {
        int[] counts = new int[3];
        for (int o = 0; o < this.connect; o++) counts[this.board[c + o][r]]++;
        if (p == 0) {
          if (counts[1] == this.connect) return 1;
          if (counts[2] == this.connect) return 2;
        } else {
          if (counts[0] + counts[p] == this.connect) s += Board.CHAIN_WEIGHT[counts[p]];
          if (counts[0] + counts[op] == this.connect) s -= Board.CHAIN_WEIGHT[counts[op]];
        }
      }
    }
    for (int c = 0; c < this.cols; c++) {
      for (int r = 0; r <= this.rows - this.connect; r++) {
        int[] counts = new int[3];
        for (int o = 0; o < this.connect; o++) counts[this.board[c][r + o]]++;
        if (p == 0) {
          if (counts[1] == this.connect) return 1;
          if (counts[2] == this.connect) return 2;
        } else {
          if (counts[0] + counts[p] == this.connect) s += Board.CHAIN_WEIGHT[counts[p]];
          if (counts[0] + counts[op] == this.connect) s -= Board.CHAIN_WEIGHT[counts[op]];
        }
      }
    }
    for (int c = 0; c <= this.cols - this.connect; c++) {
      for (int r = 0; r <= this.rows - this.connect; r++) {
        int[] counts = new int[3];
        for (int o = 0; o < this.connect; o++) counts[this.board[c + o][r + o]]++;
        if (p == 0) {
          if (counts[1] == this.connect) return 1;
          if (counts[2] == this.connect) return 2;
        } else {
          if (counts[0] + counts[p] == this.connect) s += Board.CHAIN_WEIGHT[counts[p]];
          if (counts[0] + counts[op] == this.connect) s -= Board.CHAIN_WEIGHT[counts[op]];
        }
      }
    }
    for (int c = 0; c <= this.cols - this.connect; c++) {
      for (int r = this.connect - 1; r < this.rows; r++) {
        int[] counts = new int[3];
        for (int o = 0; o < this.connect; o++) counts[this.board[c + o][r - o]]++;
        if (p == 0) {
          if (counts[1] == this.connect) return 1;
          if (counts[2] == this.connect) return 2;
        } else {
          if (counts[0] + counts[p] == this.connect) s += Board.CHAIN_WEIGHT[counts[p]];
          if (counts[0] + counts[op] == this.connect) s -= Board.CHAIN_WEIGHT[counts[op]];
        }
      }
    }
    return Integer.min(s, Board.CHAIN_WEIGHT[4]);
  }

  public Board copy() {
    return new Board(this.rows, this.cols, this.connect, this.board);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int r = this.rows - 1; r >= 0; r--) {
      for (int c = 0; c < this.cols; c++) sb.append(Board.COINS[this.board[c][r]] + " ");
      sb.append('\n');
    }
    return sb.toString();
  }

}
