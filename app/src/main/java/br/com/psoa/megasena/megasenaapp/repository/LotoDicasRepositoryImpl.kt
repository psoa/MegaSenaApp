package br.com.psoa.megasena.megasenaapp.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import br.com.psoa.megasena.megasenaapp.api.LotoDicasAPI
import br.com.psoa.megasena.megasenaapp.data.LotoDicasEntity
import br.com.psoa.megasena.megasenaapp.data.LotoDicasResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 *
 * Retrieve information about lottery results
 *
 * Final target will be https://www.lotodicas.com.br but keep psoa.com.br for develop
 * propose (personal domain)
 *
 */
class LotoDicasRepositoryImpl : LotoDicasRepository {

    private val lotoDicasAPI: LotoDicasAPI

    init {

        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://www.psoa.com.br")
                .build()
        lotoDicasAPI = retrofit.create(LotoDicasAPI::class.java)

    }

    override fun getLotoDicas(numberLottery: String):
            LiveData<LotoDicasResponse> {
        val liveData = MutableLiveData<LotoDicasResponse>()


        lotoDicasAPI.search(numberLottery)
                .enqueue(object : Callback<LotoDicasEntity> {
                    override fun onResponse(call: Call<LotoDicasEntity>?, response: Response<LotoDicasEntity>?) {
                        liveData.value = LotoDicasResponse(response?.body())
                    }

                    override fun onFailure(call: Call<LotoDicasEntity>?, t: Throwable?) {
                        Log.e("LotoDicas", "Exception", t)
                        liveData.value = LotoDicasResponse(t!!)
                    }

                })
        return liveData
    }

}