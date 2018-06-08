package br.com.psoa.megasena.megasenaapp.repository

import android.arch.lifecycle.LiveData
import br.com.psoa.megasena.megasenaapp.data.LotoDicasResponse

interface LotoDicasRepository {
    fun getLotoDicas (numberLottery: String): LiveData<LotoDicasResponse>;
}