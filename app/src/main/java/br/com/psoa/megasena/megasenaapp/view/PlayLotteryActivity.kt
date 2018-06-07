package br.com.psoa.megasena.megasenaapp.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.AppCompatButton
import android.view.View
import br.com.psoa.megasena.megasenaapp.R
import kotlinx.android.synthetic.main.activity_play_lottery.*
import android.widget.Toast
import br.com.psoa.megasena.megasenaapp.data.Bet
import br.com.psoa.megasena.megasenaapp.repository.AppRepository
import br.com.psoa.megasena.megasenaapp.repository.DeleteListener
import br.com.psoa.megasena.megasenaapp.repository.SaveListener
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

        play_button.setOnClickListener({ onPlayClicked() })
        cancel_button.setOnClickListener({ onCancelClicked() })
        play_button.setOnClickListener({ onPlayClicked() })
        clean_button.setOnClickListener { onCleanClicked() }

        val isEdit = intent.getBooleanExtra("isEdit", false)
        if (isEdit) {
            play_button.text = getString(R.string.edit_button)
            cancel_button.text = getString(R.string.delete_button)
            val numbers = intent.getStringExtra("numbers")
            myPlayNumber += numbers.split(",").map { it.toInt() }
            showNumbers()
        }
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
        BetEdit(userId, serializeNumbers()).save()

    }

    private fun onCancelClicked() {
        val isEdit = intent.getBooleanExtra("isEdit", false)
        if (isEdit) {
            val sp = getSharedPreferences("login", MODE_PRIVATE)
            val userId = sp.getString("userId", null)
            BetEdit(userId, serializeNumbers()).delete()
        } else {
            finish()
        }
    }

    fun onPlayNumberClick(v: View) {
        var bt = v as AppCompatButton
        val n = bt.text.toString().toInt()
        if (!myPlayNumber.contains(n))
            myPlayNumber += n
        showNumbers()
    }

    private fun toast(msg: String) {
        val context = this@PlayLotteryActivity.applicationContext
        val duration = Toast.LENGTH_LONG
        val toast = Toast.makeText(context, msg, duration)
        runOnUiThread { toast.show() }
    }

    private fun showNumbers() {
        var numbers = ""
        myPlayNumber.sort()
        for (n in myPlayNumber) {
            numbers += " " + n.toString()
        }
        txMyPlayNumbers.text = numbers
    }

    private fun serializeNumbers(): String {
        var numbers = ""
        myPlayNumber.sort()
        for (n in myPlayNumber) {
            if (!numbers.isEmpty())
                numbers += ","
            numbers += n.toString()
        }
        return numbers
    }

    inner class BetEdit internal constructor(private val userId: String,
                                             private val betNumber: String) : SaveListener, DeleteListener {

        private val repo = AppRepository(this@PlayLotteryActivity.application)
        override fun onSave(statusCode: SaveListener.SaveStatusCode) {
            if (statusCode == SaveListener.SaveStatusCode.OK) {
                setResult(1)
                finish()
            } else {
                toast("Unable to save the bet")
            }

        }

        override fun onDelete(statusCode: DeleteListener.DeleteStatusCode) {
            if (statusCode == DeleteListener.DeleteStatusCode.OK) {
                setResult(1)
                finish()
            } else {
                toast("Unable to delete the bet")
            }

        }

        fun save() {
            val sp = getSharedPreferences("lottery", MODE_PRIVATE)
            val currentLottery = sp.getString("currentLottery", "2039")
            val date = SimpleDateFormat("YYYY-mm-dd").format(Date())

            val isEdit = intent.getBooleanExtra("isEdit", false)
            var bet =
                    if (isEdit)
                        Bet(
                                intent.getStringExtra("betId"),
                                date,
                                betNumber,
                                userId,
                                currentLottery
                        )
                    else
                        Bet(
                                date,
                                betNumber,
                                userId,
                                currentLottery
                        )

            repo.save(
                    bet,
                    this
            )
        }

        fun delete() {
            val sp = getSharedPreferences("lottery", MODE_PRIVATE)
            val currentLottery = sp.getString("currentLottery", "2039")
            val date = intent.getStringExtra("dateOfBet")

            var bet =
                    Bet(
                            intent.getStringExtra("betId"),
                            date,
                            betNumber,
                            userId,
                            currentLottery
                    )

            repo.delete(
                    bet,
                    this
            )
        }

    }

}
