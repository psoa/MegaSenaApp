package br.com.psoa.megasena.megasenaapp.ViewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import br.com.psoa.megasena.megasenaapp.data.LotoDicasResponse
import br.com.psoa.megasena.megasenaapp.repository.LotoDicasRepository
import br.com.psoa.megasena.megasenaapp.repository.LotoDicasRepositoryImpl


class LotoDicasViewModel : ViewModel() {

    private val _lotoDicasRepository: LotoDicasRepository
    private val _apiResponse: MediatorLiveData<LotoDicasResponse>

    val apiResponse: LiveData<LotoDicasResponse> get() = _apiResponse
    val isLoading: MutableLiveData<Boolean>

    init {
        _lotoDicasRepository = LotoDicasRepositoryImpl()
        _apiResponse = MediatorLiveData()
        isLoading = MutableLiveData()

    }

    fun search(numberLottery: String): LiveData<LotoDicasResponse> {
        isLoading.postValue(true)
        _apiResponse.addSource(_lotoDicasRepository.getLotoDicas(numberLottery)) { a ->
            _apiResponse.value = a
            isLoading.postValue(false)
        }

        return _apiResponse
    }

}
