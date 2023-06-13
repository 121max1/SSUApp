package com.example.ssuapp.service

import androidx.lifecycle.LiveData
import com.example.ssuapp.model.Cryptocurrency
import com.example.ssuapp.model.Wallet
import com.example.ssuapp.model.WalletInfo
import com.example.ssuapp.repository.WalletRepository
import com.example.ssuapp.service.Interfaces.CryptoBalancesService
import com.example.ssuapp.service.Interfaces.CryptoPricesService
import com.example.ssuapp.service.Interfaces.WalletsService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class WalletServiceImpl @Inject constructor(
    private val walletRepository: WalletRepository,
    private val cryptoBalancesService: CryptoBalancesService,
    private val cryptoPricesService: CryptoPricesService
) : WalletsService {

    override suspend fun getInfoAboutWallets(): List<WalletInfo> {
        val wallets = walletRepository.getWallets()

        val walletsInfo : MutableList<WalletInfo> = ArrayList()

        for(wallet in wallets){
            val balance = withContext(Dispatchers.IO) {
                cryptoBalancesService.getCryptoBalance(wallet.cryptocurrency, wallet.address)
            }
            val price = withContext(Dispatchers.IO) {
                cryptoPricesService.getCryptoPrice(wallet.cryptocurrency)
            }
            val walletInfo = WalletInfo(wallet.id,
                wallet.cryptocurrency,
                wallet.address,
                DecimalFormat("#.###").format(balance) ,
                DecimalFormat("#.###").format(balance * price),
                wallet.name)
            walletsInfo.add(walletInfo)
        }

        return walletsInfo
    }

    override suspend fun addWallet(wallet: Wallet) {
        walletRepository.addWallet(wallet);
    }

    override suspend fun deleteWallet(wallet: Wallet) {
        walletRepository.deleteWallet(wallet)
    }

    override suspend fun getPriceByCryptocurrency(cryptocurrency: Cryptocurrency, wallet: String) : Double{
        val balance = cryptoBalancesService.getCryptoBalance(cryptocurrency, wallet)
        val price = cryptoPricesService.getCryptoPrice(cryptocurrency)

        val result = BigDecimal(balance * price).setScale(2, RoundingMode.FLOOR)
        return result.toDouble()
    }

}