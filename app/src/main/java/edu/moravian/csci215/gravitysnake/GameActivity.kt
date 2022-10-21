package edu.moravian.csci215.gravitysnake

import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.slider.Slider
import edu.moravian.csci215.gravitysnake.databinding.ActivityGameBinding


/**
 * Activity that runs the actual game. Besides making sure the app is displayed
 * full-screen, this Activity sets the difficulty for the game and gets the
 * sensor for the game, adding the game view as the listener for the sensor.
 *
 * NOTE: the layout for this Activity is done for you, the Activity is forced
 * to be in portrait mode so you don't have to worry about the rotation problem,
 * and the fullscreen handling is done as well. You only need to deal with
 * setting the difficulty and the sensors.
 */

/**
 * Slider code from https://www.geeksforgeeks.org/how-to-customise-mdc-sliders-in-android
 */

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    /** The sensor manager service that gives access to sensors and listening to their events */
    private var sensorManager: SensorManager? = null
    /** The gravity sensor we are using in the app */
    private var gravitySensor: Sensor? = null
    /** The snakeGameView for this app */
    private lateinit var snakeGameView: SnakeGameView
    /** the TextView for the chosen difficulty */
    private lateinit var difficultyResult: TextView
    /** The snakeGameView for this app */
    private lateinit var difficultySlider: Slider

    /** enum class to convert the value of the difficultySlider to a difficulty setting */
    enum class DifficultyFormat(val difficultyInt: Int) {
        BEGINNER(0), EASY(25), MEDIUM(50), HARD(75), EXTREME(100);

        companion object {
            private val difficultyFormat = values().associateBy { it.difficultyInt }
            fun findByValue(difficultyInt: Int) = difficultyFormat[difficultyInt]
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Force the game to be fullscreen
        hideSystemUI()

        //register UI elements for the slider and the chosen difficulty
        difficultyResult = findViewById(R.id.difficultyResult)
        difficultySlider = findViewById(R.id.difficultySlider)
        // defaults the difficulty to BEGINNER
        difficultyResult.text = DifficultyFormat.findByValue(0).toString()

        // TODO: set the difficulty of the snake game view
        // add Change listener for the difficultySlider to show on TextView and change the Difficulty
        difficultySlider.addOnChangeListener {
                _, value, _ -> difficultyResult.text = value.toDouble().toString()
                // changes the difficulty of the game when the slider value is changed by th user
                snakeGameView.setDifficulty(value.toInt())
        }

        // TODO: setup the sensors (use the gravity sensor)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        gravitySensor = sensorManager?.getDefaultSensor(Sensor.TYPE_GRAVITY)
        snakeGameView = findViewById(R.id.snakeGameView)

    }


    // TODO: properly setup sensor event listeners (the listener is the snake game view)
    /** When the activity resumes, we start listening to the sensor. */
    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(snakeGameView, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    /** When the activity pauses, we stop listening to the sensor. */
    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(snakeGameView)
    }

    /** When the activity is destroyed, we cleanup our variables. */
    override fun onDestroy() {
        super.onDestroy()
        sensorManager = null
        gravitySensor = null
    }

    ///// Don't worry about the rest of this code - it deals with making a fullscreen app /////
    /** Hides the system UI elements for the app, making the app full-screen.  */
    private fun hideSystemUI() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        // Keep the screen on as well
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    /** When the focus of the app changes, possibly hide the system UI elements  */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }
}
