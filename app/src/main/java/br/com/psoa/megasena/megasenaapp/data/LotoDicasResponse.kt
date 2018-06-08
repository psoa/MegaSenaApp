package br.com.psoa.megasena.megasenaapp.data

class LotoDicasResponse {

    var lotoDicas: LotoDicasEntity? = null
    var error: Throwable? = null

    constructor(lotoDicas: LotoDicasEntity?) {
        this.lotoDicas = lotoDicas
    }

    constructor(error: Throwable) {
        this.error = error
    }


}