package com.lypaka.pixelskills.Listeners.Reforged.Breeder;

import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.pixelskills.API.SkillEXPEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.pixelmonmod.pixelmon.api.events.BreedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReforgedMakeEggListener {

    @SubscribeEvent
    public void onMakeEgg (BreedEvent.MakeEgg event) {

        EntityPlayerMP player = event.parent1.getOwnerPlayer();
        if (player == null) return;
        Pokemon egg = event.getEgg();
        Pokemon parent1 = event.parent1;
        Pokemon parent2 = event.parent2;

        if (!ConfigGetters.isSkillEnabled("Breeder")) return;
        Skill skill = PixelSkills.skillConfigManager.get("Breeder");
        if (!PermissionHandler.hasPermission(player, skill.getAccessPermission())) {

            if (!skill.getAccessPermission().equals("")) return;

        }

        if (SkillGetters.makingOffspringBlacklist.contains(egg.getLocalizedName())) return;
        if (SkillGetters.makingParentBlacklist.contains(parent1.getLocalizedName()) || SkillGetters.makingParentBlacklist.contains(parent2.getLocalizedName())) return;

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
