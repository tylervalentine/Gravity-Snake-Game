package edu.moravian.csci215.gravitysnake

import android.graphics.PointF
import androidx.lifecycle.ViewModel
import kotlin.random.Random

/**
 * The Snake Game. Keeps track of the snake, the food, walls, the score, and
 * all of the difficultly settings (speed, starting length, length increase per
 * food, wall placement probability). Knows how to draw everything.
 *
 * NOTE: This class is complete, but you need to look over the public methods
 * to use them.
 */
class SnakeGame : ViewModel() {
    /** The width of the game, in px */
    private var width = 0
    /** The height of the game, in px */
    private var height = 0

    /**
     * If the game is over. The game is over if no game has ever been started
     * or if the snake has died and a new game has not yet started.
     */
    var isGameOver = true
        private set

    /** The snake moving around the game */
    private lateinit var snake: Snake

    /** Number of foods eaten (i.e. the score) */
    var score = 0
        private set

    /**
     * The direction the snake will move in the future in radians from -pi to pi.
     * 0 is straight right along the positive X axis and pi/2 is straight down
     * along the positive Y axis.
     */
    var movementDirection = 0.0f

    /** Initial speed of the snake, in dp/frame */
    var initialSpeed = 2.5f

    /** How much the speed increases each time a food is eaten */
    var speedIncreasePerFood = 0.0f

    /** Speed of the snake, in dp/frame */
    private val currentSpeed: Float
        get() = initialSpeed + score * speedIncreasePerFood

    /** Number of pieces the snake is at the beginning of each game */
    var startingLength = 25

    /** Number of pieces to add to the snake each time a food is eaten */
    var lengthIncreasePerFood = 8

    /** Probability to place a new wall each frame */
    var wallPlacementProbability = 0.005f

    /** Locations of all of the walls, each in px (mutable) */
    private val walls: MutableList<PointF> = ArrayList()

    /** List of all of the current wall locations */
    val wallLocations: List<PointF>
        get() = walls

    /** Location of the current food, in px */
    var foodLocation = PointF(0f, 0f)
        private set

    /** List of all of the current body locations */
    val bodyLocations: List<PointF>
        get() = snake.body

    /** The current head location */
    val headLocation: PointF
        get() = snake.head

    /**
     * Set the factor for converting dp measurements to px. This is the size of
     * 1 dp in pixels. For example, FOOD_SIZE_DP will always be multiplied by
     * this value.
     */
    var dpToPxFactor = 1f

    /**
     * @return true if the game has not yet been started ever
     */
    val hasNotStarted: Boolean
        get() = width == 0 || height == 0

    /**
     * Start the game. Can also be used to start a new game if one has already begun.
     * @param width the width of the playing area in px
     * @param height the height of the playing area in px
     */
    fun startGame(width: Int, height: Int) {
        require(width > 0 && height > 0) { "bad width or height" }
        this.width = width
        this.height = height
        snake = Snake(
            PointF(width / 2f, height / 2f),
            startingLength, dpToPxFactor
        )
        score = 0
        walls.clear()
        moveFood()
        isGameOver = false
    }

    /**
     * Update the game. This moves the snake, checks if the game is over (snake
     * hits itself, goes out of bounds, or hits a wall), checks if the snake
     * got the food, and possibly adds a new random wall piece.
     * @return true if the game is still going, false if the game is over
     */
    fun update(): Boolean {
        if (isGameOver) { return false }

        // Move the snake
        snake.move(movementDirection, currentSpeed * dpToPxFactor) // NOTE: this does not take into account the frame rate

        // Check if the snake has hit itself, gone out-of-bounds, or hit any of the walls
        if (snake.headIntersectsSelf() || snake.headIsOutOfBounds(width, height) ||
            snake.headIntersectsAnyItem(walls, WALL_SIZE_DP * dpToPxFactor)) {
            isGameOver = true
            return false
        }

        // Check if the snake has "eaten" the food
        if (snake.headIntersectsItem(foodLocation, FOOD_SIZE_DP * dpToPxFactor)) {
            snake.increaseLength(lengthIncreasePerFood)
            moveFood()
            score++
        }

        // Every so often add a new wall
        if (Random.nextFloat() < wallPlacementProbability) {
            addWall()
        }
        return true
    }

    /**
     * "Touch" the game at a particular point. If the snake is touched
     * anywhere, the game is over. If the food is touched, it moves. If a wall
     * is touched, it is removed.
     * @param pt the touched point
     * @return true if the game is still going, false if the game is now over
     */
    fun touched(pt: PointF): Boolean {
        if (isGameOver) { return false }

        // Game over if the snake is touched
        if (snake.bodyIntersectsItem(pt, TOUCH_SIZE_DP)) {
            isGameOver = true
            return false
        }

        // Move the food if touched
        if (withinRange(pt, foodLocation, (FOOD_SIZE_DP + TOUCH_SIZE_DP) * dpToPxFactor)) {
            moveFood()
        }

        // Remove all walls within range of the touched point
        val dist = (WALL_SIZE_DP + TOUCH_SIZE_DP) * dpToPxFactor
        walls.removeIf { wall: PointF -> withinRange(wall, pt, dist) }
        return true
    }

    /** Move the food to a new random location.  */
    private fun moveFood() {
        foodLocation = randomPoint(FOOD_SIZE_DP * dpToPxFactor)
    }

    /** Add a new random wall to the game.  */
    private fun addWall() {
        walls.add(randomPoint(WALL_SIZE_DP * dpToPxFactor))
    }

    /**
     * Create a new random point that lies completely within the bounds of the
     * world and is not near the snake head.
     * @param size the size of the item, in px
     */
    private fun randomPoint(size: Float): PointF {
        var pt: PointF
        do {
            pt = PointF(
                Random.nextFloat() * (width - 2 * size) + size,
                Random.nextFloat() * (height - 2 * size) + size
            )
        } while (snake.bodyIntersectsItem(pt, 2 * size))
        return pt
    }

    companion object {
        /** Radius of each food item in dp  */
        const val FOOD_SIZE_DP = 15f

        /** Radius of each wall item in dp  */
        const val WALL_SIZE_DP = 10f

        /** Touch "radius" in dp  */
        const val TOUCH_SIZE_DP = 5f
    }
}
