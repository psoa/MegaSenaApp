package br.com.psoa.megasena.megasenaapp.ViewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.psoa.megasena.megasenaapp.data.LotoDicasResponse
import br.com.psoa.megasena.megasenaapp.repository.LotoDicasRepository
import br.com.psoa.megasena.megasenaapp.repository.LotoDicasRepositoryImpl


class LotoDicasViewModel : ViewModel() {

    private val lotoDicasRepository: LotoDicasRepository
    private val mApiResponse: MediatorLiveData<LotoDicasResponse>
    val apiResponse: LiveData<LotoDicasResponse> get() = mApiResponse
    val isLoading: MutableLiveData<Boolean>

    init {
        lotoDicasRepository = LotoDicasRepositoryImpl()
        mApiResponse = MediatorLiveData()
        isLoading = MutableLiveData()

    }

    fun search(numberLottery: String): LiveData<LotoDicasResponse> {
        isLoading.postValue(true)
        mApiResponse.addSource(lotoDicasRepository.getLotoDicas(numberLottery)) { a ->
            mApiResponse.value = a
            isLoading.postValue(false)
        }

        return mApiResponse
    }

}
