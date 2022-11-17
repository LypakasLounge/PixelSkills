package com.lypaka.pixelskills.Listeners.Reforged.Fisherman;

import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.pixelskills.API.SkillEXPEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.pixelmonmod.pixelmon.api.events.FishingEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReforgedPixelmonFishListener {

    @SubscribeEvent
    public void onFish (FishingEvent.Reel event) {

        if (!event.isPokemon()) return;
        EntityPlayerMP player = event.player;
        EntityPixelmon pokemon = (EntityPixelmon) event.optEntity.get();

        if (!ConfigGetters.isSkillEnabled("Fisherman")) return;
        Skill skill = PixelSkills.skillConfigManager.get("Fisherman");
        if (!PermissionHandler.hasPermission(player, skill.getAccessPermission())) {

            if (!skill.getAccessPermission().equals("")) return;

        }

        if (SkillGetters.fishermanBlacklist.contains(pokemon.getPokemonName())) return;

        double exp = skill.getSettings().getEXPPerTask();
        if (SkillGetters.fishermanWhitelist.containsKey(pokemon.getPokemonName())) {

            exp = SkillGetters.fishermanWhitelist.get(pokemon.getPokemonName());

        }
        String rod = event.getRodType().name();
        if (rod.contains("old")) {

            if (SkillGetters.fishermanRodModifiers.containsKey("Old")) {

                double mod = SkillGetters.fishermanRodModifiers.get("Old");
                if (mod > 0) {

                    exp = exp + mod;

                }

            }

        } else if (rod.contains("good")) {

            if (SkillGetters.fishermanRodModifiers.containsKey("Good")) {

                double mod = SkillGetters.fishermanRodModifiers.get("Good");
                if (mod > 0) {

                    exp = exp + mod;

                }

            }

        } else if (rod.contains("super")) {

            if (SkillGetters.fishermanRodModifiers.containsKey("Super")) {

                double mod = SkillGetters.fishermanRodModifiers.get("Super");
                if (mod > 0) {

                    exp = exp + mod;

                }

            }

        }
        double shinyMod = SkillGetters.fishermanShinyModifier;
        if (shinyMod > 0) {

            exp = exp + shinyMod;

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
