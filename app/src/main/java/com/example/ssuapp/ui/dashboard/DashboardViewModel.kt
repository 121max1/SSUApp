package com.example.ssuapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ssuapp.service.Interfaces.WalletsService
import com.example.ssuapp.ui.domain.CryptoBalanceAmount
import com.example.ssuapp.ui.domain.WalletModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private var walletsService: WalletsService) : ViewModel() {

    val cryptoBalances: LiveData<List<CryptoBalanceAmount>> = MutableLiveData()

    init {
        loadBalances()
    }

    fun loadBalances() {
        viewModelScope.launch {
            try {
                val walletInfoList = walletsService.getInfoAboutWallets()

                val balanceByCryptocurrency = walletInfoList.groupBy { it.cryptocurrency }

                val cryptoBalanceList =
                    balanceByCryptocurrency.map { (cryptocurrency, walletInfos) ->
                        val totalBalanceUsd =
                            walletInfos.sumOf { it.balanceUsd.toDoubleOrNull() ?: 0.0 }
                        val percentage = (totalBalanceUsd / walletInfoList.sumOf {
                            it.balanceUsd.toDoubleOrNull() ?: 0.0
                        } * 100).toInt()
                        CryptoBalanceAmount(percentage, cryptocurrency.name)
                    }

                (cryptoBalances as MutableLiveData<List<CryptoBalanceAmount>>).value = cryptoBalanceList

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}