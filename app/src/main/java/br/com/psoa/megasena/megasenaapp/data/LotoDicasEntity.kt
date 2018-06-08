package br.com.psoa.megasena.megasenaapp.data


/**
 *
 * https://www.lotodicas.com.br/api/mega-sena/2038
 *
 * {"numero":2038,"data":"2018-05-08","sorteio":[6,25,26,35,38,40],
 * "ganhadores":[0,28,2768],"rateio":[0,65960.7,953.18],"acumulado":"sim",
 * "valor_acumulado":26744923.97,"cidades":[],"proximo_estimativa":30000000,
 * "proximo_data":"2018-05-10"}
 *
 */
data class LotoDicasEntity(
        val numero: String,
        val data: String,
        val sorteio: String,
        val ganhadores: String,
        val rateio: String,
        val acumulado: String,
        val valor_acumulado: String,
        val cidades: String,
        val proximo_estimativa: String,
        val proximo_data: String
)