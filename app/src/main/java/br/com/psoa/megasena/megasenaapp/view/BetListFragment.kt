package br.com.psoa.megasena.megasenaapp.view

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.psoa.megasena.megasenaapp.R
import br.com.psoa.megasena.megasenaapp.adapter.BetListRecyclerViewAdapter
import br.com.psoa.megasena.megasenaapp.data.Bet
import br.com.psoa.megasena.megasenaapp.data.User
import br.com.psoa.megasena.megasenaapp.repository.AppRepository
import br.com.psoa.megasena.megasenaapp.repository.UserBetLoadListener
import br.com.psoa.megasena.megasenaapp.repository.UserLoadListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.bet_item_list.*


class BetListFragment : Fragment() {

    private var columnCount = 1

    private var listener: OnBetListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        LoadUserBetTask().execute()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bet_item_list, container, false)

        // Set the adapter
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = BetInteractionListener(context)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    private fun setUpRecyclerView(data: List<Bet>) {

        val rvBet = bet_list

        if (rvBet is RecyclerView) {
            with(rvBet) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = BetListRecyclerViewAdapter(data, listener)
            }
        }


    }

    interface OnBetListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Bet?)
    }


    //TODO: Rip out this to a new class or ViewModel maybe
    inner class BetInteractionListener internal constructor(context: Context) :
            OnBetListFragmentInteractionListener {

        override fun onListFragmentInteraction(item: Bet?) {
            Log.i("TIAGO", "A item was selected " + item?.numbers)
        }

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    inner class LoadUserBetTask internal constructor() :
            AsyncTask<Void, Void, Void>(), UserBetLoadListener {

        override fun onUserBetLoad(bets: List<Bet>?) {
            if (bets != null) {
                setUpRecyclerView(bets)
            } else {
                Log.i("TIAGO", "No itens found")
            }
        }

        private val repo = AppRepository(this@BetListFragment.activity!!.application)


        override fun doInBackground(vararg params: Void): Void? {
            val sp = this@BetListFragment.activity!!.getSharedPreferences(
                    "login",
                    AppCompatActivity.MODE_PRIVATE)
            val userId = sp.getString("userId", null)

            repo.load(userId, this)
            return null
        }

        override fun onCancelled() {
//            mAuthTask = null
//            showProgress(false)
        }
    }


    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                BetListFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}
