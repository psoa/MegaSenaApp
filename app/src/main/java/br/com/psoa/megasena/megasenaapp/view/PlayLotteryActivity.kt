package br.com.psoa.megasena.megasenaapp.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.psoa.megasena.megasenaapp.R

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class PlayLotteryActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_play_lottery)
    }

}
