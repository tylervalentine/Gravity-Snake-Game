package edu.moravian.csci215.gravitysnake
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import edu.moravian.csci215.gravitysnake.databinding.WelcomeScreenBinding

class WelcomeActivity: AppCompatActivity() {

    private lateinit var binding: WelcomeScreenBinding

    private var gameTitle: TextView? = null
    private  var difficultyResult: TextView? = null
    private var difficultyNumber: Int = 0
    private var difficultySlider: Slider? = null
    private var highScoreText: TextView? = null

    private var gameActivity = GameActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Sets game title text
        binding.gameTitle.text = getString(R.string.game_title)

        // defaults the difficulty to BEGINNER
        binding.difficultyResult.text = GameActivity.DifficultyFormat.findByValue(0).toString()

        // Changes game difficulty based on value of slider
        binding.difficultySlider.addOnChangeListener { _, value, _ ->
            difficultyNumber = value.toInt()
            binding.difficultyResult.text = GameActivity.DifficultyFormat.findByValue(value.toInt()).toString()
            setHighScoreText(difficultyNumber)
        }

    }

    /**
     * Sets high score text based on provided difficulty number.
     * @param difficulty difficulty number
     */
    private fun setHighScoreText(difficulty: Int) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        val highScore = sharedPref.getInt(GameActivity.DifficultyFormat.findByValue(difficulty).toString(), 0)
        binding.highScore.text = getString(R.string.high_score_text, highScore)
    }

    /** Allows the screen to change to the game activity when the user clicks the start button */
    fun changeScreenView(view: View?) {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("difficulty", difficultyNumber)
        startActivity(intent)
    }
}
