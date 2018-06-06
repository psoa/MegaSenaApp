package br.com.psoa.megasena.megasenaapp.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.util.Log
import android.view.View
import br.com.psoa.megasena.megasenaapp.R
import kotlinx.android.synthetic.main.activity_play_lottery.*
import android.widget.Toast
import br.com.psoa.megasena.megasenaapp.data.Bet
import br.com.psoa.megasena.megasenaapp.repository.AppRepository
import br.com.psoa.megasena.megasenaapp.repository.InsertListener
import java.text.SimpleDateFormat
import java.util.*


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class PlayLotteryActivity : AppCompatActivity() {


    private val myPlayNumber: kotlin.collections.MutableList<Int> = java.util.ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_play_lottery)
        cancel_button.setOnClickListener({ onCancelClicked() })
        play_button.setOnClickListener({ onPlayClicked() })
        clean_button.setOnClickListener { onCleanClicked() }

    }

    private fun onCleanClicked() {
        if (!myPlayNumber.isEmpty()) {
            myPlayNumber.sort()
            myPlayNumber.removeAt(myPlayNumber.lastIndex)
            showNumbers()
        }
    }

    private fun onPlayClicked() {
        if (myPlayNumber.size < 6) {
            toast("You should select at least 6 numbers")
            return
        }
        val sp = getSharedPreferences("login", MODE_PRIVATE)
        val userId = sp.getString("userId", null)
        BetInsert(userId, serializeNumbers()).insert()

    }

    private fun onCancelClicked() {
        finish()
    }

    fun onPlayNumberClick(v: View) {
        var bt = v as AppCompatButton
        val n = bt.text.toString().toInt()
        if (!myPlayNumber.contains(n))
            myPlayNumber.add(n)
        showNumbers()
    }

    private fun toast(s: String) {
        val context = this@PlayLotteryActivity.applicationContext
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(context, s, duration)
        toast.show()
    }

    private fun showNumbers() {
        var numbers = ""
        myPlayNumber.sort()
        for (n in myPlayNumber) {
            numbers += " " + n.toString()
        }
        txMyPlayNumbers.text = numbers
    }

    private fun serializeNumbers() : String {
        var numbers = ""
        myPlayNumber.sort()
        for (n in myPlayNumber) {
            if (!numbers.isEmpty())
                numbers += ","
            numbers += n.toString()
        }
        return numbers
    }

    inner class BetInsert internal constructor(private val userId:String,
                                               private val betNumber:String): InsertListener {

        private val repo = AppRepository(this@PlayLotteryActivity.application)
        override fun onUserInsert(statusCode: InsertListener.InsertStatusCode) {
            if (statusCode == InsertListener.InsertStatusCode.OK) {
                //toast("Bet made")
                Log.i("PSOA", "Bet added")
                finish()
            } else {
                Log.i("PSOA", "bet not added")
            }

        }

        fun insert() {
            val sp = getSharedPreferences("lottery", MODE_PRIVATE)
            val currentLottery = sp.getString("currentLottery", "2039")
            val date = SimpleDateFormat("YYYY-mm-dd").format(Date())
            val bet = Bet(date, betNumber ,userId, currentLottery)
            repo.save(bet, this)
        }

    }

}
