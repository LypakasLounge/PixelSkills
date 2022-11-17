package com.lypaka.pixelskills.Listeners.Reforged.Gladiator;

import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.pixelskills.API.SkillEXPEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class ReforgedPokemonDefeatListener {

    @SubscribeEvent
    public void onPokemonDefeat (BeatWildPixelmonEvent event) {

        EntityPlayerMP player = event.player;
        EntityPixelmon pokemon = event.wpp.controlledPokemon.get(0).entity;

        if (!ConfigGetters.isSkillEnabled("Gladiator")) return;
        Skill skill = PixelSkills.skillConfigManager.get("Gladiator");
        if (!PermissionHandler.hasPermission(player, skill.getAccessPermission())) {

            if (!skill.getAccessPermission().equals("")) return;

        }

        if (SkillGetters.gladiatorPokemonBlacklist.contains(pokemon.getPokemonName())) return;

        Map<String, Boolean> map = SkillGetters.gladiatorTaskMap;
        if (map.containsKey("Defeating-Wild-Pokemon")) {

            if (!map.get("Defeating-Wild-Pokemon")) return;

        }

        double exp = skill.getSettings().getEXPPerTask();
        if (SkillGetters.gladiatorPokemonModifiers.containsKey("Bosses")) {

            double mod = SkillGetters.gladiatorPokemonModifiers.get("Bosses");
            if (mod > 0) {

                boolean found = false;
                if (!found) {

                    if (pokemon.isBossPokemon()) {

                        exp = exp + mod;

                    }

                }

            }

        }

        if (exp > 0) {

            SkillEXPEvent expEvent = new SkillEXPEvent(player, skill, exp);
            MinecraftForge.EVENT_BUS.post(expEvent);
            Account account = JoinListener.accountMap.get(player.getUniqueID());
            if (!expEvent.isCanceled()) {

                account.awardEXP(skill, expEvent.getEXP());

            }

        }

    }

}
