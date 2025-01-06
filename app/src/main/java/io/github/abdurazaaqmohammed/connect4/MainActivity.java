package io.github.abdurazaaqmohammed.connect4;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private final View[][] board = new View[ROWS][COLS];
    private int currentPlayer = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TableLayout tableLayout = createBoard();
        setContentView(tableLayout);
    }

    private TableLayout createBoard() {
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setStretchAllColumns(true);
        tableLayout.setShrinkAllColumns(true);
        tableLayout.setGravity(Gravity.CENTER);
        tableLayout.setPadding(16, 16, 16, 16);
        tableLayout.setBackgroundColor(Color.BLACK);

        for (int row = 0; row < ROWS; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            for (int col = 0; col < COLS; col++) {
                View cell = new View(this);
                TableRow.LayoutParams cellParams = new TableRow.LayoutParams(0, 200, 1);
                cellParams.setMargins(2, 2, 2, 2);
                cell.setLayoutParams(cellParams);
                cell.setBackgroundColor(Color.DKGRAY);

                int col1 = col;
                cell.setOnClickListener(v -> {
                    for (int r = ROWS - 1; r >= 0; r--) {
                        View curr = board[r][col1];
                        if (curr.getTag() == null) {
                            curr.setBackgroundColor(currentPlayer == 1 ? Color.parseColor("#E6676B") : Color.parseColor("#AEE8F5"));
                            curr.setTag(currentPlayer);
                            if (checkDirection(r, col1, 1, 0) ||  // Horizontal
                                    checkDirection(r, col1, 0, 1) ||  // Vertical
                                    checkDirection(r, col1, 1, 1) ||  // Diagonal
                                    checkDirection(r, col1, 1, -1)) {
                                Toast.makeText(MainActivity.this, (currentPlayer==1 ? "Red" : "Blue") + " wins!", Toast.LENGTH_LONG).show();
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        currentPlayer = 1;
                                        for (int i12 = 0; i12 < ROWS; i12++) {
                                            for (int j12 = 0; j12 < COLS; j12++) {
                                                View curr = board[i12][j12];
                                                curr.setBackgroundColor(Color.GRAY);
                                                curr.setTag(null);
                                            }
                                        }
                                    }
                                }, 2000);
                            }
                            currentPlayer = currentPlayer == 1 ? 2 : 1;
                            break;
                        }
                    }
                });
                board[row][col] = cell;
                tableRow.addView(cell);
            }

            tableLayout.addView(tableRow);
        }

        return tableLayout;
    }

    private boolean checkDirection(int row, int col, int dRow, int dCol) {
        int count = 1;
        int player = (int) board[row][col].getTag();

        count += countInDirection(row, col, dRow, dCol, player);
        count += countInDirection(row, col, -dRow, -dCol, player);

        return count >= 4;
    }

    private int countInDirection(int row, int col, int dRow, int dCol, int player) {
        int r = row + dRow;
        int c = col + dCol;
        int count = 0;

        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c].getTag() != null && board[r][c].getTag().equals(player)) {
            count++;
            r += dRow;
            c += dCol;
        }

        return count;
    }
}