package com.lypaka.pixelskills.Listeners.Reforged.Breeder;

import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.pixelskills.API.SkillEXPEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.pixelmonmod.pixelmon.api.events.EggHatchEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReforgedHatchEggListener {

    @SubscribeEvent
    public void onHatch (EggHatchEvent event) {

        EntityPlayerMP player = event.getPlayer();
        Pokemon pokemon = event.getPokemon();

        if (!ConfigGetters.isSkillEnabled("Breeder")) return;
        Skill skill = PixelSkills.skillConfigManager.get("Breeder");
        if (!PermissionHandler.hasPermission(player, skill.getAccessPermission())) {

            if (!skill.getAccessPermission().equals("")) return;

        }

        if (SkillGetters.hatchingBlacklist.contains(pokemon.getLocalizedName())) return;

        double exp = skill.getSettings().getEXPPerTask();
        if (SkillGetters.breederHatchingModifiers.containsKey("Alolan")) {

            double mod = SkillGetters.breederHatchingModifiers.get("Alolan");
            if (mod > 0) {

                if (PixelSkills.alolans.contains(pokemon.getLocalizedName()) && pokemon.getForm() == 1) {

                    exp = exp + mod;

                }

            }

        }
        if (SkillGetters.breederHatchingModifiers.containsKey("Galarian")) {

            double mod = SkillGetters.breederHatchingModifiers.get("Galarian");
            if (mod > 0) {

                if (PixelSkills.galarians.contains(pokemon.getLocalizedName()) && pokemon.getForm() == 2) {

                    exp = exp + mod;

                }

            }

        }
        if (SkillGetters.breederHatchingModifiers.containsKey("Hisuian")) {

            double mod = SkillGetters.breederHatchingModifiers.get("Hisuian");
            if (mod > 0) {

                if (PixelSkills.hisuians.contains(pokemon.getLocalizedName()) && pokemon.getForm() == 3) {

                    exp = exp + mod;

                }

            }

        }
        if (SkillGetters.breederHatchingModifiers.containsKey("Shiny")) {

            double mod = SkillGetters.breederHatchingModifiers.get("Shiny");
            if (mod > 0) {

                if (pokemon.isShiny()) {

                    exp = exp + mod;

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
