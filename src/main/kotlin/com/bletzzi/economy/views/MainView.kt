package com.bletzzi.economy.views

import com.bletzzi.economy.EconomyPlugin
import com.bletzzi.economy.models.MenuItem
import com.bletzzi.economy.models.User
import com.bletzzi.economy.utils.ItemBuilder
import com.bletzzi.economy.utils.NumberFormat
import com.bletzzi.economy.utils.extensors.replace
import me.devnatan.inventoryframework.View
import me.devnatan.inventoryframework.ViewConfigBuilder
import me.devnatan.inventoryframework.context.RenderContext
import org.bukkit.inventory.ItemStack

class MainView(private val plugin: EconomyPlugin) : View() {
    private val menu = plugin.menuConfig.menus["main"]!!

    override fun onInit(config: ViewConfigBuilder) {
        config.size(menu.size).title(menu.title).cancelOnClick()
    }

    override fun onFirstRender(render: RenderContext) {
        val player = render.player
        val user = plugin.userRepository.select(player.uniqueId, true)!!

        menu.items.forEach { (key, item) ->
            if(item.slot == -1) return@forEach
            when(key) {
                "profile" -> render.slot(item.slot).renderWith { getProfileStack(user, item) }

                "payments_on" -> if(user.receiving) render.slot(item.slot).renderWith { item.stack }.onClick { click ->
                    user.receiving = false
                    render.openForPlayer(MainView::class.java)
                }
                "payments_off" -> if(!user.receiving) render.slot(item.slot).renderWith { item.stack }.onClick { click ->
                    user.receiving = true
                    render.openForPlayer(MainView::class.java)
                }

                "logs" -> render.slot(item.slot).renderWith { item.stack }.onClick { click -> player.sendMessage("Logs") }
                "send" -> render.slot(item.slot).renderWith { item.stack }.onClick { click -> player.sendMessage("Transferência") }
            }
        }
    }

    private fun getProfileStack(user: User, item: MenuItem): ItemStack {
        val stack = item.stack.clone()
        return ItemBuilder(stack)
            .lore(stack.itemMeta.lore.replace("{balance}", NumberFormat.format(user.balance)))
            .build()
    }
}