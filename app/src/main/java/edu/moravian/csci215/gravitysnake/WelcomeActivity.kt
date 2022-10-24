package edu.moravian.csci215.gravitysnake
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import edu.moravian.csci215.gravitysnake.databinding.WelcomeScreenBinding

class WelcomeActivity: AppCompatActivity() {

    private lateinit var binding: WelcomeScreenBinding

    /** the TextView for the chosen difficulty */
    private var difficultyResult: TextView? = null

    /** The snakeGameView for this app */
    private var difficultySlider: Slider? = null

    private lateinit var snakeGameView: SnakeGameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //register UI elements for the slider and the chosen difficulty
        difficultyResult = findViewById(R.id.difficultyResult)
        difficultySlider = findViewById(R.id.difficultySlider)

        // defaults the difficulty to BEGINNER
        difficultyResult?.text = GameActivity.DifficultyFormat.findByValue(0).toString()

        // TODO: set the difficulty of the snake game view
        // add Change listener for the difficultySlider to show on TextView and change the Difficulty
        difficultySlider?.addOnChangeListener { _, value, _ ->
            difficultyResult?.text = value.toDouble().toString()

            // changes the difficulty of the game when the slider value is changed by the user
            snakeGameView.setDifficulty(value.toInt())
        }

    }

    /** Allows the screen to change to the game activity when the user clicks the start button */
    fun changeScreenView(view: View?) {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
    }
}
