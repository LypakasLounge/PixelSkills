package com.lypaka.pixelskills.Listeners.Generations.Darwinist;

import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.pixelskills.API.SkillEXPEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.pixelmongenerations.api.events.EvolveEvent;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GensEvolutionListener {

    @SubscribeEvent
    public void onEvolve (EvolveEvent.PostEvolve event) {

        EntityPixelmon pokemon = event.getPokemon();
        EntityPlayerMP player = event.getPlayer();

        if (!ConfigGetters.isSkillEnabled("Darwinist")) return;
        Skill skill = PixelSkills.skillConfigManager.get("Darwinist");
        if (!PermissionHandler.hasPermission(player, skill.getAccessPermission())) {

            if (!skill.getAccessPermission().equals("")) return;

        }

        if (SkillGetters.darwinistBlacklist.contains(pokemon.getPokemonName())) return;

        double exp = skill.getSettings().getEXPPerTask();
        if (SkillGetters.darwinistShinyModifier > 0) {

            exp = exp + SkillGetters.darwinistShinyModifier;

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
