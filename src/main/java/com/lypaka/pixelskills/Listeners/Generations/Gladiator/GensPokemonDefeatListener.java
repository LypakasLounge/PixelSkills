package com.lypaka.pixelskills.Listeners.Generations.Gladiator;

import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.pixelskills.API.SkillEXPEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.pixelmongenerations.api.events.BeatWildPixelmonEvent;
import com.pixelmongenerations.common.entity.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class GensPokemonDefeatListener {

    @SubscribeEvent
    public void onPokemonDefeat (BeatWildPixelmonEvent event) {

        EntityPlayerMP player = event.getPlayer();
        EntityPixelmon pokemon = event.getWildPokemon().getPokemon().pokemon;

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
        if (SkillGetters.gladiatorPokemonModifiers.containsKey("Alphas") && pokemon.isAlpha()) {

            double mod = SkillGetters.gladiatorPokemonModifiers.get("Alphas");
            if (mod > 0) {

                exp = exp + mod;

            }

        } else if (SkillGetters.gladiatorPokemonModifiers.containsKey("Bosses")) {

            double mod = SkillGetters.gladiatorPokemonModifiers.get("Bosses");
            if (mod > 0) {

                boolean found = false;
                if (Loader.isModLoaded("betterbosses")) {

                    if (pokemon.getTags().stream().anyMatch(s -> s.contains("BossPokemon:Tier-"))) {

                        found = true;
                        exp = exp + mod;

                    }

                }
                if (!found) {

                    if (Loader.isModLoaded("betterpixelmonspawner")) {

                        if (pokemon.getTags().stream().anyMatch(s -> s.contains("PixelmonDefaultBoss"))) {

                            found = true;
                            exp = exp + mod;

                        }

                    }

                }
                if (!found) {

                    if (pokemon.isBossPokemon()) {

                        exp = exp + mod;

                    }

                }

            }

        } else if (SkillGetters.gladiatorPokemonModifiers.containsKey("Totems") && pokemon.isTotem()) {

            double mod = SkillGetters.gladiatorPokemonModifiers.get("Totems");
            if (mod > 0) {

                exp = exp + mod;

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
