package edu.moravian.csci215.gravitysnake

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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
class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Force the game to be fullscreen
        hideSystemUI()

        // TODO: set the difficulty of the snake game view

        // TODO: setup the sensors (use the gravity sensor)
    }


    // TODO: properly setup sensor event listeners (the listener is the snake game view)


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
