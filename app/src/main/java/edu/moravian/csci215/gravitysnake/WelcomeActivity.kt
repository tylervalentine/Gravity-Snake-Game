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

    /** the TextView for the chosen difficulty and game title*/

    private var gameTitle: TextView? = null
    private  var difficultyResult: TextView? = null

    private var difficulyNumber: Int = 0
    /** The snakeGameView for this app */
    private var difficultySlider: Slider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //register UI elements for the slider, chosen difficulty, and the game title
        gameTitle = findViewById(R.id.gameTitle)
        difficultyResult = findViewById(R.id.difficultyResult)
        difficultySlider = findViewById(R.id.difficultySlider)
        gameTitle?.text = getString(R.string.game_title)

        // defaults the difficulty to BEGINNER
        difficultyResult?.text = GameActivity.DifficultyFormat.findByValue(0).toString()

        // Changes game difficulty based on value of slider
        difficultySlider?.addOnChangeListener { _, value, _ ->
            difficulyNumber = value.toInt()
            difficultyResult?.text = GameActivity.DifficultyFormat.findByValue(value.toInt()).toString()
        }

    }

    /** Allows the screen to change to the game activity when the user clicks the start button */
    fun changeScreenView(view: View?) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("difficulty", difficulyNumber)
        startActivity(intent)
    }
}
