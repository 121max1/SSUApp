package com.example.ssuapp.service.Interfaces

import com.example.ssuapp.model.Cryptocurrency

interface CryptoPricesService {
    suspend fun getCryptoPrice(type : Cryptocurrency) : Double
}