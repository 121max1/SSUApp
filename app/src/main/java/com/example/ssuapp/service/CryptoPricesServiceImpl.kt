package com.example.ssuapp.service

import com.example.ssuapp.common.Constants
import com.example.ssuapp.model.Cryptocurrency
import com.example.ssuapp.service.Interfaces.CryptoBalancesService
import com.example.ssuapp.service.Interfaces.CryptoPricesService
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class CryptoPricesServiceImpl : CryptoPricesService {
    override suspend fun getCryptoPrice(type: Cryptocurrency): Double {

        val symbol : String = getTradingSymbol(type)

        val apiUrl = Constants.BINANCE_API_URL +  "/api/v3/avgPrice?symbol=$symbol"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(apiUrl)
            .get()
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val responseBody = response.body?.string()
            val jsonResponse = responseBody?.let { JSONObject(it) }
            val price = jsonResponse?.optDouble("price")

            return price ?: 0.0
        } else {
            throw Exception("Request failed: ${response.code} ${response.message}")
        }
    }

    fun getTradingSymbol(type: Cryptocurrency) : String {
        return when (type) {
            Cryptocurrency.BITCOIN -> "BTCUSDT"
            Cryptocurrency.ETHEREUM -> "ETHUSDT"
            Cryptocurrency.LITECOIN -> "LTCUSDT"
        }
    }
}