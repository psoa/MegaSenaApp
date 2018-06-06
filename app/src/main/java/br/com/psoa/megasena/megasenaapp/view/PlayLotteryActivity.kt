package br.com.psoa.megasena.megasenaapp.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.view.View
import br.com.psoa.megasena.megasenaapp.R
import kotlinx.android.synthetic.main.activity_play_lottery.*
import android.widget.Toast



/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class PlayLotteryActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_play_lottery)
        cancel_button.setOnClickListener({ onCancelClicked() })
        play_button.setOnClickListener({onPlayClicked()})
    }

    private fun onPlayClicked() {
        //TODO: the magic happens here
    }

    private fun onCancelClicked() {
        finish()
    }

    fun onPlayNumberClick(v: View) {
        var bt = v as AppCompatButton
        tToast("Go button ["+bt.text+"] clicked!")
    }

    private fun tToast(s: String) {
        val context = applicationContext
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(context, s, duration)
        toast.show()
    }

}
