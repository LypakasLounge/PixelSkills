package com.lypaka.pixelskills.Listeners;

import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JoinListener {

    public static Map<UUID, Account> accountMap = new HashMap<>();

    @SubscribeEvent
    public void onJoin (PlayerEvent.PlayerLoggedInEvent event) {

        EntityPlayerMP player = (EntityPlayerMP) event.player;
        PixelSkills.playerConfigManager.loadPlayer(player.getUniqueID());
        Account account;
        if (!accountMap.containsKey(player.getUniqueID())) {

            account = new Account(player);
            account.load();

        }

    }

    @SubscribeEvent
    public void onLeave (PlayerEvent.PlayerLoggedOutEvent event) {

        Account account = accountMap.get(event.player.getUniqueID());
        if (account == null) return; // in the event a player joins the server and gets immediately kicked (due to like wrong Pixelmon mod or whatever) and this is not here, server crashes - don't remove it
        account.save();
        accountMap.entrySet().removeIf(entry -> entry.getKey().toString().equals(event.player.getUniqueID().toString()));

    }

}
