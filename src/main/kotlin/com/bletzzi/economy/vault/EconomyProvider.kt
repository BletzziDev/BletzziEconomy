package com.bletzzi.economy.vault

import com.bletzzi.economy.EconomyPlugin
import com.bletzzi.economy.utils.NumberFormat
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.UUID

class EconomyProvider(val plugin: EconomyPlugin) : Economy {

    fun get(uuid: UUID): Double {
        return plugin.userRepository.select(uuid, true)?.balance ?: 0.0
    }

    fun has(uuid: UUID, value: Double): Boolean {
        return get(uuid) >= value
    }

    fun set(uuid: UUID, value: Double) {
        plugin.transactionController.performSetTransaction(uuid, value)
    }

    fun add(uuid: UUID, value: Double) {
        plugin.transactionController.performAddTransaction(uuid, value)
    }

    fun remove(uuid: UUID, value: Double) {
        plugin.transactionController.performRemoveTransaction(uuid, value)
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getName(): String {
        return "BletzziEconomy"
    }

    override fun hasBankSupport(): Boolean {
        return false
    }

    override fun fractionalDigits(): Int {
        return 1
    }

    override fun format(p0: Double): String {
        return NumberFormat.format(p0)
    }

    override fun currencyNamePlural(): String {
        return ""
    }

    override fun currencyNameSingular(): String {
        return ""
    }

    override fun hasAccount(p0: String?): Boolean {
        return true
    }

    override fun hasAccount(p0: OfflinePlayer?): Boolean {
        return true
    }

    override fun hasAccount(p0: String?, p1: String?): Boolean {
        return true
    }

    override fun hasAccount(p0: OfflinePlayer?, p1: String?): Boolean {
        return true
    }

    @Deprecated("Offline player string getter is deprecated")
    override fun getBalance(p0: String?): Double {
        Bukkit.getOfflinePlayer(p0)?.let { return get(it.uniqueId) }
        return 0.0
    }

    override fun getBalance(p0: OfflinePlayer?): Double {
        p0?.let { return get(p0.uniqueId) }
        return 0.0
    }

    @Deprecated("Offline player string getter is deprecated")
    override fun getBalance(p0: String?, p1: String?): Double {
        return getBalance(Bukkit.getOfflinePlayer(p0))
    }

    override fun getBalance(p0: OfflinePlayer?, p1: String?): Double {
        return getBalance(p0)
    }

    @Deprecated("Offline player string getter is deprecated")
    override fun has(p0: String?, p1: Double): Boolean {
        return has(Bukkit.getOfflinePlayer(p0), p1)
    }

    override fun has(p0: OfflinePlayer?, p1: Double): Boolean {
        p0?.let { return has(p0.uniqueId, p1) }
        return false
    }

    @Deprecated("Offline player string getter is deprecated")
    override fun has(p0: String?, p1: String?, p2: Double): Boolean {
        return has(Bukkit.getOfflinePlayer(p0), p2)
    }

    override fun has(p0: OfflinePlayer?, p1: String?, p2: Double): Boolean {
        return has(p0, p2)
    }

    @Deprecated("Offline player string getter is deprecated")
    override fun withdrawPlayer(p0: String?, p1: Double): EconomyResponse {
        return withdrawPlayer(Bukkit.getOfflinePlayer(p0), p1)
    }

    override fun withdrawPlayer(p0: OfflinePlayer?, p1: Double): EconomyResponse {
        p0?.let {
            plugin.userRepository.select(p0.uniqueId, true)?.let {
                remove(it.uuid, p1)
                return EconomyResponse(p1, it.balance - p1, EconomyResponse.ResponseType.SUCCESS, "")
            }
        }
        return EconomyResponse(p1, 0.0, EconomyResponse.ResponseType.FAILURE, "Invalid User")
    }

    @Deprecated("Offline player string getter is deprecated")
    override fun withdrawPlayer(p0: String?, p1: String?, p2: Double): EconomyResponse {
        return withdrawPlayer(Bukkit.getOfflinePlayer(p0), p2)
    }

    override fun withdrawPlayer(p0: OfflinePlayer?, p1: String?, p2: Double): EconomyResponse {
        return withdrawPlayer(p0, p2)
    }

    @Deprecated("Offline player string getter is deprecated")
    override fun depositPlayer(p0: String?, p1: Double): EconomyResponse {
        return depositPlayer(Bukkit.getOfflinePlayer(p0), p1)
    }

    override fun depositPlayer(p0: OfflinePlayer?, p1: Double): EconomyResponse {
        p0?.let {
            plugin.userRepository.select(p0.uniqueId, true)?.let {
                add(it.uuid, p1)
                return EconomyResponse(p1, it.balance + p1, EconomyResponse.ResponseType.SUCCESS, "")
            }
        }
        return EconomyResponse(p1, 0.0, EconomyResponse.ResponseType.FAILURE, "Invalid User")
    }

    @Deprecated("Offline player string getter is deprecated")
    override fun depositPlayer(p0: String?, p1: String?, p2: Double): EconomyResponse {
        return depositPlayer(Bukkit.getOfflinePlayer(p0), p2)
    }

    override fun depositPlayer(p0: OfflinePlayer?, p1: String?, p2: Double): EconomyResponse {
        return depositPlayer(p0, p2)
    }

    override fun createBank(p0: String?, p1: String?): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented")
    }

    override fun createBank(p0: String?, p1: OfflinePlayer?): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented")
    }

    override fun deleteBank(p0: String?): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented")
    }

    override fun bankBalance(p0: String?): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented")
    }

    override fun bankHas(p0: String?, p1: Double): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented")
    }

    override fun bankWithdraw(p0: String?, p1: Double): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented")
    }

    override fun bankDeposit(p0: String?, p1: Double): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented")
    }

    override fun isBankOwner(p0: String?, p1: String?): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented")
    }

    override fun isBankOwner(p0: String?, p1: OfflinePlayer?): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented")
    }

    override fun isBankMember(p0: String?, p1: String?): EconomyResponse {
        return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented")
    }

    override fun isBankMember(p0: String?, p1: OfflinePlayer?): EconomyResponse {
       return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not implemented")
    }

    override fun getBanks(): MutableList<String> {
        return mutableListOf()
    }

    override fun createPlayerAccount(p0: String?): Boolean {
        return true
    }

    override fun createPlayerAccount(p0: OfflinePlayer?): Boolean {
        return true
    }

    override fun createPlayerAccount(p0: String?, p1: String?): Boolean {
        return true
    }

    override fun createPlayerAccount(p0: OfflinePlayer?, p1: String?): Boolean {
        return true
    }
}