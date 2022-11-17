package com.lypaka.pixelskills.Listeners.Reforged.Collector;

import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.pixelskills.API.SkillEXPEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.pixelmonmod.pixelmon.api.events.CaptureEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReforgedCaptureListener {

    @SubscribeEvent
    public void onCapture (CaptureEvent.SuccessfulCapture event) {

        EntityPlayerMP player = event.player;
        EntityPixelmon pokemon = event.getPokemon();

        if (!ConfigGetters.isSkillEnabled("Collector")) return;
        Skill skill = PixelSkills.skillConfigManager.get("Collector");
        if (!PermissionHandler.hasPermission(player, skill.getAccessPermission())) {

            if (!skill.getAccessPermission().equals("")) return;

        }

        if (SkillGetters.collectorBlackList.contains(pokemon.getPokemonName())) return;

        double exp = skill.getSettings().getEXPPerTask();
        if (SkillGetters.collectorWhiteList.containsKey(pokemon.getPokemonName())) {

            exp = SkillGetters.collectorWhiteList.get(pokemon.getPokemonName());

        }

        if (pokemon.getPokemonData().isShiny() && SkillGetters.collectorModifiers.containsKey("Shiny")) {

            exp = exp + SkillGetters.collectorModifiers.get("Shiny");

        } else if (SkillGetters.collectorModifiers.containsKey("Legendaries")) {

            if (EnumSpecies.legendaries.contains(pokemon.getSpecies()) || EnumSpecies.ultrabeasts.contains(pokemon.getSpecies())) {

                exp = exp + SkillGetters.collectorModifiers.get("Legendaries");

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
