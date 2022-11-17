package com.lypaka.pixelskills.Listeners.Generations.Collector;

import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.pixelskills.API.SkillEXPEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.pixelmongenerations.api.events.CaptureEvent;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import com.pixelmongenerations.core.enums.EnumSpecies;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GensCaptureListener {

    @SubscribeEvent
    public void onCapture (CaptureEvent.SuccessfulCaptureEvent event) {

        EntityPlayerMP player = event.getPlayer();
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

        if (pokemon.isShiny() && SkillGetters.collectorModifiers.containsKey("Shiny")) {

            exp = exp + SkillGetters.collectorModifiers.get("Shiny");

        } else if (pokemon.isAlpha() && SkillGetters.collectorModifiers.containsKey("Alpha")) {

            exp = exp + SkillGetters.collectorModifiers.get("Alpha");

        } else if (SkillGetters.collectorModifiers.containsKey("Legendaries")) {

            if (EnumSpecies.legendaries.contains(pokemon.getPokemonName()) || EnumSpecies.ultrabeasts.contains(pokemon.getPokemonName())) {

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
