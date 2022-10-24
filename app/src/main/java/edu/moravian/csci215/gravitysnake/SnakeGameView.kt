package edu.moravian.csci215.gravitysnake

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner


/**
 * The custom View for the Snake Game. This handles the user interaction and
 * sensor information for the snake game but has none of the game logic. That
 * is all within SnakeGame and Snake.
 *
 * NOTE: This class is where most of the work is required. You must document
 * *all* methods (including methods already declared that don't have
 * documentation).
 */
class SnakeGameView constructor(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs), SensorEventListener {

    /** The metrics about the display to convert from dp and sp to px  */
    private val displayMetrics = context.resources.displayMetrics

    /** The paints and drawables used for the different parts of the game  */
    // TODO: adjust these fields as appropriate for your style (including their values, which ones there are, and if they are paints or drawables)
    private val bodyPaint = Paint().apply { color = 0xAA008800.toInt() }
    private val foodPaint = Paint().apply { color = Color.RED }
    private val wallPaint = Paint().apply { color = 0xFF964B00.toInt() }
    private val scorePaint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
        textSize = spToPx(24f) // use sp for text
        isFakeBoldText = true
    }
    private val gameOverBarPaint = Paint().apply { color = 0x886200EE.toInt() }
    init { setBackgroundColor(0xFF333333.toInt()) } // This color is automatically painted as the background, feel free to change this (and it can even be changed to any Drawable if you use setBackground() instead)

    /**
     * The snake game behind this view. SnakeGame is a ViewModel. Normally,
     * those are loaded in Activities. It is a bit trickier in a custom View.
     */
    private val snakeGame by lazy {
        ViewModelProvider(findViewTreeViewModelStoreOwner()!!)[SnakeGame::class.java].apply {
            dpToPxFactor = displayMetrics.density
        }
    }

    /** The difficulty for this game */
    private var difficulty = 0

    /**
     * Utility function to convert dp units to px units. All Canvas and Paint
     * function use numbers in px units but dp units are better for
     * inter-device support.
     * @param dp the size in dp (device-independent-pixels)
     * @return the size in px (pixels)
     */
    private fun dpToPx(dp: Float): Float = dp * displayMetrics.density

    /**
     * Utility function to convert sp units to px units. All Canvas and Paint
     * function use numbers in px units but sp units are better for
     * inter-device support, especially for text.
     * @param sp the size in sp (scalable-pixels)
     * @return the size in px (pixels)
     */
    private fun spToPx(sp: Float): Float = sp * displayMetrics.scaledDensity

    /**
     * Once the view is laid out, we know the dimensions of it and can start
     * the game with the snake in the middle (if the game hasn't already
     * started).
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        // NOTE: this function is done for you
        super.onLayout(changed, left, top, right, bottom)
        if (snakeGame.hasNotStarted) {
            snakeGame.startGame(right - left, bottom - top)
        }
        invalidate()
    }

    /**
     * Set the difficulty for the game, a value from 0 to 4. This also updates
     * the snake games properties such as starting length, wall placement
     * probability, etc.
     * @param difficulty the new difficulty for the game
     */
    fun setDifficulty(difficulty: Int) {
        this.difficulty = difficulty // need to save this for when we need to save the high score
        changeGameProperties(difficulty)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        postInvalidateOnAnimation() // automatically invalidate every frame so we get continuous playback

        // TODO: update the game and draw the contents of the view
        snakeGame.update()
        for (location in snakeGame.bodyLocations)
        {
            canvas.drawCircle(location.x, location.y, dpToPx(Snake.BODY_PIECE_SIZE_DP), bodyPaint)
        }

        canvas.drawCircle(snakeGame.foodLocation.x, snakeGame.foodLocation.y, dpToPx(SnakeGame.FOOD_SIZE_DP), foodPaint)
        for (location in snakeGame.wallLocations)
        {
            canvas.drawCircle(location.x, location.y, dpToPx(SnakeGame.WALL_SIZE_DP), wallPaint)
        }

        canvas.drawText(snakeGame.score.toString(), 0f,0f, scorePaint)

        // Make sure that drawing things utilize the SnakeGame.FOOD_SIZE_DP, SnakeGame.WALL_SIZE_DP,
        // and Snake.BODY_PIECE_SIZE_DP as the sizes of things when you draw them. You also must
        // make sure to utilize dpToPx() to convert the dp sizes to px sizes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        // TODO
    }

    /** Does nothing but must be provided.  */
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    // TODO: handle touch events

    // TODO: add more methods to reduce redundancy of code

    /**
     * Changes game properties based on difficulty provided.
     * @param difficulty number representing current difficulty of game.
     */
    private fun changeGameProperties(difficulty: Int) {
        when (difficulty) {
            1 -> {
                snakeGame.startingLength = 30
                snakeGame.lengthIncreasePerFood = 11
            }

            2 -> {
                snakeGame.startingLength = 35
                snakeGame.lengthIncreasePerFood = 14
                snakeGame.speedIncreasePerFood = 0.25f
            }

            3 -> {
                snakeGame.startingLength = 40
                snakeGame.lengthIncreasePerFood = 18
                snakeGame.speedIncreasePerFood = 0.5f
                snakeGame.wallPlacementProbability = 0.010f
            }

            4 -> {
                snakeGame.startingLength = 45
                snakeGame.lengthIncreasePerFood = 22
                snakeGame.speedIncreasePerFood = 1.0f
                snakeGame.wallPlacementProbability = 0.020f
            }
        }
    }
}
