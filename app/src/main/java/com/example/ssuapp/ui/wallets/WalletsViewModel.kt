package com.example.ssuapp.ui.wallets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ssuapp.R
import com.example.ssuapp.model.Cryptocurrency
import com.example.ssuapp.model.Wallet
import com.example.ssuapp.service.Interfaces.WalletsService
import com.example.ssuapp.ui.domain.WalletModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletsViewModel  @Inject constructor(private var walletsService: WalletsService) : ViewModel() {

    private val _wallets = MutableLiveData<List<WalletModel>>()
    val wallets: LiveData<List<WalletModel>>
        get() = _wallets

    init {
        loadWallets()
    }

    fun loadWallets() {
        viewModelScope.launch {
            try {
                val walletInfoList = walletsService.getInfoAboutWallets()
                val wallets = walletInfoList.map { walletInfo ->
                    WalletModel(
                        icon = getIconForCryptocurrency(walletInfo.cryptocurrency),
                        background = getBackgroundForCryptocurrency(walletInfo.cryptocurrency),
                        name = walletInfo.name,
                        balance = walletInfo.balance,
                        balanceUsd = walletInfo.balanceUsd,
                        cryptocurrency = getNameForCryptocurrency(walletInfo.cryptocurrency),
                        address = walletInfo.address
                    )
                }
                _wallets.postValue(wallets)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getIconForCryptocurrency(cryptocurrency: Cryptocurrency): Int {
        return when (cryptocurrency) {
            Cryptocurrency.BITCOIN -> R.drawable.btc_icon
            Cryptocurrency.ETHEREUM -> R.drawable.eth_icon
            Cryptocurrency.LITECOIN -> R.drawable.ltc_icon
        }
    }

    private fun getBackgroundForCryptocurrency(cryptocurrency: Cryptocurrency): Int {
        return when (cryptocurrency) {
            Cryptocurrency.BITCOIN -> R.drawable.btc_background
            Cryptocurrency.ETHEREUM -> R.drawable.eth_background
            Cryptocurrency.LITECOIN -> R.drawable.ltc_background
        }
    }

    private fun getNameForCryptocurrency(cryptocurrency: Cryptocurrency): String {
        return when (cryptocurrency) {
            Cryptocurrency.BITCOIN -> "Bitcoin"
            Cryptocurrency.ETHEREUM -> "Etherium"
            Cryptocurrency.LITECOIN -> "Litecoin"
        }
    }

    private fun getCryptocurrencyByName(name: String) : Cryptocurrency{
        return when (name) {
            "Bitcoin" -> Cryptocurrency.BITCOIN
            "Etherium" -> Cryptocurrency.ETHEREUM
            "Litecoin" -> Cryptocurrency.LITECOIN
            else -> Cryptocurrency.BITCOIN
        }
    }

    suspend fun addWallet(cryptocurrency: Cryptocurrency, address: String, name: String){
        walletsService.addWallet(Wallet(0, cryptocurrency, address, name));
    }

    private fun WalletModel.toWallet(): Wallet {
        return Wallet(
            cryptocurrency = getCryptocurrencyByName(this.cryptocurrency),
            address = this.address,
            name =  this.name
        )
    }
}