package com.keithmackay.games.androidgames.corners;

/**
 * Created by Keith MacKay on 8/9/2016.
 *
 * Moving all game-based events to a single class
 */
public abstract class GameEventHandler {
    public abstract void onScoreChange(int increment);
}
