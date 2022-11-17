package com.lypaka.pixelskills.Listeners.Generations.Breeder;

import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.pixelskills.API.SkillEXPEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.pixelmongenerations.api.events.BreedEvent;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GensMakeEggListener {

    @SubscribeEvent
    public void onMakeEgg (BreedEvent.MakeEggEvent event) {

        EntityPlayerMP player = (EntityPlayerMP) event.getParentOne().getOwner();
        if (player == null) return;
        EntityPixelmon egg = event.getEgg();
        EntityPixelmon parent1 = event.getParentOne();
        EntityPixelmon parent2 = event.getParentTwo();

        if (!ConfigGetters.isSkillEnabled("Breeder")) return;
        Skill skill = PixelSkills.skillConfigManager.get("Breeder");
        if (!PermissionHandler.hasPermission(player, skill.getAccessPermission())) {

            if (!skill.getAccessPermission().equals("")) return;

        }

        if (SkillGetters.makingOffspringBlacklist.contains(egg.getPokemonName())) return;
        if (SkillGetters.makingParentBlacklist.contains(parent1.getPokemonName()) || SkillGetters.makingParentBlacklist.contains(parent2.getPokemonName())) return;

        double exp = skill.getSettings().getEXPPerTask();
        exp = exp + SkillGetters.breederMakingModifier;

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
