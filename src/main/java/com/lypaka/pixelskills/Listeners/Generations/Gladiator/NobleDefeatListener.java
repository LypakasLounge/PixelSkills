package com.lypaka.pixelskills.Listeners.Generations.Gladiator;

import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.pixelskills.API.SkillEXPEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.pixelmongenerations.api.events.battles.DefeatNobleEvent;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class NobleDefeatListener {

    @SubscribeEvent
    public void onNobleDefeat (DefeatNobleEvent event) {

        EntityPlayerMP player = event.getPlayer();
        EntityPixelmon pokemon = event.getPokemon();

        if (!ConfigGetters.isSkillEnabled("Gladiator")) return;
        Skill skill = PixelSkills.skillConfigManager.get("Gladiator");
        if (!PermissionHandler.hasPermission(player, skill.getAccessPermission())) {

            if (!skill.getAccessPermission().equals("")) return;

        }

        if (SkillGetters.gladiatorPokemonBlacklist.contains(pokemon.getPokemonName())) return;

        Map<String, Boolean> map = SkillGetters.gladiatorTaskMap;
        if (map.containsKey("Defeating-Noble-Pokemon")) {

            if (!map.get("Defeating-Noble-Pokemon")) return;

        }

        double exp = skill.getSettings().getEXPPerTask();
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
