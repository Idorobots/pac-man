package pac.man.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

public class Board {

    public enum Type {
        PASSABLE, BLOCKED
    }

    /**
     * Rozmiar wyświetlanej płytki (w pikselach).
     */
    private static final int TILE_SIZE = 20;

    private Type[][] grid;

    public Board(Rect dimension) {
        grid = new Type[dimension.height()][dimension.width()];

        for (int y = 0; y < dimension.height(); y++)
            for (int x = 0; x < dimension.width(); x++)
                grid[y][x] = Type.PASSABLE;
    }

    public Board(Resources resources, int id) {

        Bitmap bmp = BitmapFactory.decodeResource(resources, id);

        grid = new Type[bmp.getHeight()][bmp.getWidth()];

        for (int y = 0; y < getDimension().height(); ++y) {
            for (int x = 0; x < getDimension().width(); ++x) {

                int color = bmp.getPixel(x, y);

                if (color == Color.BLACK)
                    grid[y][x] = Type.BLOCKED;
                else
                    grid[y][x] = Type.PASSABLE;
            }
        }
    }
    
    public Board(Type[][] grid) {
        assert grid != null;

        this.grid = grid;
    }

    public boolean isOnBoard(Point p) {
        return p.x >= 0 && p.y >= 0 && p.x < grid[0].length && p.y < grid.length;
    }

    public Rect getDimension() {
        return new Rect(0, 0, grid[0].length, grid.length);
    }

    public void setCell(Point p, Type cell) {
        assert isOnBoard(p);
        assert cell != null;

        grid[p.y][p.x] = cell;
    }

    public Type getCell(Point p) {
        assert isOnBoard(p);

        return grid[p.y][p.x];
    }

    public void draw(Canvas canvas) {
        Paint p = new Paint();
        Rect r = new Rect();

        for (int y = 0; y < getDimension().height(); y++)
            for (int x = 0; x < getDimension().width(); x++) {

                r = new Rect(x * TILE_SIZE, y * TILE_SIZE, (x + 1) * TILE_SIZE, (y + 1) * TILE_SIZE);

                switch (grid[y][x]) {
                case PASSABLE:
                    p.setColor(Color.BLACK);
                    break;
                case BLOCKED:
                    p.setColor(Color.GRAY);
                    break;
                }

                canvas.drawRect(r, p);
            }
    }
}
