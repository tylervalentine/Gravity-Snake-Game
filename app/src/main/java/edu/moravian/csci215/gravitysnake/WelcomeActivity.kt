package edu.moravian.csci215.gravitysnake
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import edu.moravian.csci215.gravitysnake.databinding.WelcomeScreenBinding
import java.io.IOException

class WelcomeActivity: AppCompatActivity() {

    private lateinit var binding: WelcomeScreenBinding

    private var difficultyNumber: Int = 0

    /** Media player instance */
    private var mediaPlayer: MediaPlayer? = null

    /** Audio file to play */
    private val audio = R.raw.desert

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Sets game title text
        binding.gameTitle.text = getString(R.string.game_title)

        // defaults the difficulty to BEGINNER
        binding.difficultyResult.text = GameActivity.DifficultyFormat.findByValue(0).toString()

        // defaults high score text to BEGINNER
        setHighScoreText(0)

        // Changes game difficulty based on value of slider
        binding.difficultySlider.addOnChangeListener { _, value, _ ->
            difficultyNumber = value.toInt()
            binding.difficultyResult.text = GameActivity.DifficultyFormat.findByValue(value.toInt()).toString()
            setHighScoreText(difficultyNumber)
        }

        // Setup music
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.desert)
        mediaPlayer?.start()

        binding.musicSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                mediaPlayer?.start()
            }
            else {
                mediaPlayer?.pause()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer = null
    }
    /**
     * Sets high score text based on provided difficulty number.
     * @param difficulty difficulty number
     */
    private fun setHighScoreText(difficulty: Int) {
        val sharedPref = getSharedPreferences("highscore", Context.MODE_PRIVATE) ?: return
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
