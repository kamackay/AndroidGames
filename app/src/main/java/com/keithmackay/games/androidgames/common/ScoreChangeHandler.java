package com.keithmackay.games.androidgames.common;

/**
 * Created by Keith on 2/17/2016.
 * Score Change Handler
 */
public abstract class ScoreChangeHandler {
    public ScoreChangeHandler(){}
    public abstract void onScoreChange(int increment);
}
