package br.com.psoa.megasena.megasenaapp.view


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import br.com.psoa.megasena.megasenaapp.R
import br.com.psoa.megasena.megasenaapp.ViewModel.LotoDicasViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    lateinit var lotoDicasViewModel: LotoDicasViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lotoDicasViewModel = ViewModelProviders.of(this).get(LotoDicasViewModel::class.java)

        lotoDicasViewModel.search("2038")

        lotoDicasViewModel.apiResponse.observe(this,
                Observer { a ->
                    if (a?.error == null) {
                        var end = a?.lotoDicas
                        numero.text = end?.numero
                        data.text = end?.data
                        sorteio.text = end?.sorteio
                    }
                })
        lotoDicasViewModel.isLoading.observe(this, Observer { isLoading ->
            if (isLoading!!) {
                loading.visibility = View.VISIBLE
            } else {
                loading.visibility = View.GONE
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

}
