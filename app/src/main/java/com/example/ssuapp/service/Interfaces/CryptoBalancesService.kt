package com.example.ssuapp.service.Interfaces

import com.example.ssuapp.model.Cryptocurrency

interface CryptoBalancesService {
    suspend fun getCryptoBalance(type : Cryptocurrency, address : String) : Double
}