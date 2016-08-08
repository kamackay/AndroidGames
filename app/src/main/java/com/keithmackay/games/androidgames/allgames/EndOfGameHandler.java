package com.keithmackay.games.androidgames.allgames;

/**
 * Created by Keith MacKay on 8/8/2016.
 */
public abstract class EndOfGameHandler {
    public EndOfGameHandler() {
    }

    public abstract void gameOver(Type type);

    public enum Type {Win, Lose}
}
