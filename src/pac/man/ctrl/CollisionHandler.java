package pac.man.ctrl;

import android.graphics.Rect;
import android.graphics.Canvas;

import pac.man.model.Character;

public abstract class CollisionHandler {
    public abstract void handle(long dt, Canvas canvas, Rect with, Character who);
}