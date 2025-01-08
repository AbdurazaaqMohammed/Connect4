package io.github.abdurazaaqmohammed.connect4;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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

        TableLayout tableLayout1 = new TableLayout(this);
        tableLayout1.setStretchAllColumns(true);
        tableLayout1.setShrinkAllColumns(true);
        tableLayout1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tableLayout1.setGravity(Gravity.CENTER);
        tableLayout1.setPadding(16, 16, 16, 16);
        tableLayout1.setBackgroundColor(Color.BLACK);
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.BLACK); // Background color
        border.setStroke(5, Color.parseColor("#E6676B")); // Border width and color
        border.setCornerRadius(16);
        tableLayout1.setBackgroundDrawable(border);

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
                            border.setStroke(5, Color.parseColor(currentPlayer == 1 ? "#AEE8F5" : "#E6676B")); // Border width and color
                            tableLayout1.setBackgroundDrawable(border);

                            GradientDrawable gradientDrawable = new GradientDrawable();
                            gradientDrawable.setShape(GradientDrawable.OVAL);
                            gradientDrawable.setColor(Color.parseColor(currentPlayer == 1 ? "#E6676B" : "#AEE8F5"));
                            LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{new ColorDrawable(Color.DKGRAY), gradientDrawable});
                            curr.setBackgroundDrawable(layerDrawable);
                            curr.setTag(currentPlayer);
                            if (checkDirection(r, col1, 1, 0) ||  // Horizontal
                                    checkDirection(r, col1, 0, 1) ||  // Vertical
                                    checkDirection(r, col1, 1, 1) ||  // Diagonal
                                    checkDirection(r, col1, 1, -1)) {
                                Toast.makeText(this, (currentPlayer == 1 ? "Red" : "Blue") + " wins!", Toast.LENGTH_LONG).show();
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(() -> {
                                            border.setStroke(5, Color.parseColor("#E6676B")); // Border width and color
                                            currentPlayer = 1;
                                            for (int i = 0; i < ROWS; i++) {
                                                for (int j = 0; j < COLS; j++) {
                                                    View curr1 = board[i][j];
                                                    curr1.setBackgroundColor(Color.DKGRAY);
                                                    curr1.setTag(null);
                                                }
                                            }
                                        });
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

            tableLayout1.addView(tableRow);
        }

        tableLayout1.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int screenWidth = tableLayout1.getWidth();
            int screenHeight = tableLayout1.getHeight();
            int size = Math.min(screenWidth / COLS, screenHeight / ROWS);

            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    View cell = board[row][col];
                    TableRow.LayoutParams params = (TableRow.LayoutParams) cell.getLayoutParams();
                    params.width = size;
                    params.height = size;
                    cell.setLayoutParams(params);
                }
            }
        });

        setContentView(tableLayout1);
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

        while (r >= 0 && r < ROWS && c >= 0 && c < COLS &&
                board[r][c].getTag() != null && board[r][c].getTag().equals(player)) {
            count++;
            r += dRow;
            c += dCol;
        }

        return count;
    }
}