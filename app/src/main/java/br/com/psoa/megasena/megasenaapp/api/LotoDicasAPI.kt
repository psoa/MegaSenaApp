package br.com.psoa.megasena.megasenaapp.api

import br.com.psoa.megasena.megasenaapp.data.LotoDicasEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface LotoDicasAPI {


    @GET("/api/mega-sena/{numberLottery}")
    fun search(@Path("numberLottery") numberLottery: String) : Call<LotoDicasEntity>
}