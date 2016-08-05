package com.keithmackay.games.androidgames._2048;

import android.view.MotionEvent;
import android.view.View;

public abstract class SwipeListener implements View.OnTouchListener {
    static int swipeLength = 250;
    private float downX, downY;

    /**
     * Set the length of the swipe listener
     *
     * @param len the index of the length option that was chosen
     */
    public void setSwipeLength(int len) {
        switch (len) {
            case 0:
                swipeLength = 100;
                return;
            case 1:
                swipeLength = 150;
                return;
            case 2:
                swipeLength = 200;
                return;
            default:
                swipeLength = 250;
                return;
            case 4:
                swipeLength = 300;
                return;
            case 5:
                swipeLength = 350;
        }
    }

    public abstract void onSwipe(Details details);

    /**
     * Called when a touch event is dispatched to a view. This allows listeners to
     * get a chance to respond before the target view.
     *
     * @param v     The view the touch event has been dispatched to.
     * @param event The MotionEvent object containing full information about
     *              the event.
     * @return True if the listener has consumed the event, false otherwise.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                float upX = event.getX(), upY = event.getY();
                float deltaX = downX - upX;
                float deltaY = downY - upY;
                // swipe horizontal?
                if (Math.abs(deltaX) > swipeLength) {
                    // left or right
                    if (deltaX < 0) {
                        onSwipe(new Details(Direction.Right, Math.abs(deltaX)));
                        return true;
                    } else if (deltaX > 0) {
                        onSwipe(new Details(Direction.Left, Math.abs(deltaX)));
                        return true;
                    }
                }
                if (Math.abs(deltaY) > swipeLength) {
                    // top or down
                    if (deltaY < 0) {
                        onSwipe(new Details(Direction.Down, Math.abs(deltaY)));
                        return true;
                    } else if (deltaY > 0) {
                        onSwipe(new Details(Direction.Up, Math.abs(deltaY)));
                        return true;
                    }
                }
                return true;
        }
        return false;
    }

    public enum Direction {Up, Down, Left, Right}

    /**
     * Details about a swipe event
     */
    public class Details {
        /**
         * The direction of the swipe event
         */
        private Direction dir;
        /**
         * The keycode of the event
         */
        private int k;
        /**
         * The distance of the swipe event
         */
        private float dist;
        /**
         * Whether or not the distance is stored in this event
         */
        private boolean hasDist;

        /**
         * Constructor to be used if the distance is to be stored
         *
         * @param direction the direction of the swipe event
         * @param distance  the distance of the swipe event
         */
        public Details(Direction direction, float distance) {
            dir = direction;
            dist = distance;
            hasDist = true;
        }

        /**
         * Constructor to be used if the distance is not to be stored
         *
         * @param direction the direction of the swipe event
         */
        public Details(Direction direction) {
            dir = direction;
            hasDist = false;
        }

        /**
         * Checker to make sure that this details class has the distance for the swipe
         *
         * @return true if the distance for this swipe was stored
         */
        public boolean hasDistance() {
            return hasDist;
        }

        /**
         * Getter for the distance of the swipe
         *
         * @return the distance of the swipe. 0 if there is no distance stored
         */
        public float getDistance() {
            if (hasDist) return dist;
            else return 0;
        }

        /**
         * Getter for the direction of the swipe event
         *
         * @return the direction of the swipe event
         */
        public Direction getDirection() {
            return dir;
        }
    }
}