package br.com.psoa.megasena.megasenaapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import br.com.psoa.megasena.megasenaapp.view.BetListFragment.OnBetListFragmentInteractionListener
import br.com.psoa.megasena.megasenaapp.R
import br.com.psoa.megasena.megasenaapp.data.Bet


import kotlinx.android.synthetic.main.bet_item.view.*

/**
 * [RecyclerView.Adapter] that can display a [Bet] and makes a call to the
 * specified [OnBetListFragmentInteractionListener].
 */
class BetListRecyclerViewAdapter(
        private val mValues: List<Bet>,
        private val mListener: OnBetListFragmentInteractionListener?)
    : RecyclerView.Adapter<BetListRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Bet
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.bet_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.lottery
        holder.mContentView.text = item.numbers

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }
}
