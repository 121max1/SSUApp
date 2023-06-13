package com.example.ssuapp.service

import com.example.ssuapp.common.Constants
import com.example.ssuapp.model.Cryptocurrency
import com.example.ssuapp.service.Interfaces.CryptoBalancesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CryptoBalancesServiceImpl : CryptoBalancesService {

    override suspend fun getCryptoBalance(type: Cryptocurrency, address: String): Double {
        return when (type) {
            Cryptocurrency.BITCOIN -> getBitcoinAmount(address)
            Cryptocurrency.ETHEREUM -> getEthereumAmount(address)
            Cryptocurrency.LITECOIN -> getLitecoinBalance(address)
        }
    }

    private suspend fun getBitcoinAmount(address: String): Double = withContext(Dispatchers.IO) {
        val apiUrl = "https://api.blockcypher.com/v1/btc/main/addrs/$address"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(apiUrl)
            .get()
            .build()

        val response = client.newCall(request).await()

        val responseBody = response.body?.string()
        val jsonResponse = responseBody?.let { JSONObject(it) }
        val result = jsonResponse?.optString("balance")

        val balanceBitcoin = result!!.toDouble()

        balanceBitcoin / 100000000
    }

    private suspend fun getEthereumAmount(address: String): Double = withContext(Dispatchers.IO) {
        val apiUrl = Constants.ETHERSCAN_API_URL + "/api?module=account&action=balance&address=$address&tag=latest&apikey=${Constants.ETHERSCAN_API_KEY}"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(apiUrl)
            .get()
            .build()

        val response = client.newCall(request).await()
        val responseBody = response.body?.string()
        val jsonResponse = responseBody?.let { JSONObject(it) }
        val result = jsonResponse?.optString("result")

        val balanceInWei = result?.toBigIntegerOrNull(16)

        if (balanceInWei != null) {
            val balanceInEther = balanceInWei.toDouble() / 1e22

            balanceInEther
        } else {
            throw Exception("Error retrieving wallet balance.")
        }
    }

    private suspend fun getLitecoinBalance(address: String): Double = withContext(Dispatchers.IO) {
        val apiUrl = Constants.BLOCKCYPHER_API_URL + "/v1/ltc/main/addrs/$address/balance"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(apiUrl)
            .get()
            .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            val responseBody = response.body?.string()
            val jsonResponse = responseBody?.let { JSONObject(it) }
            val balance = jsonResponse!!.optDouble("balance")

            balance/100000000
        } else {
            throw Exception("Request failed: ${response.code} ${response.message}")
        }
    }

    private suspend fun Call.await(): Response = suspendCoroutine { continuation ->
        enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                continuation.resume(response)
            }

            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }
        })
    }
}