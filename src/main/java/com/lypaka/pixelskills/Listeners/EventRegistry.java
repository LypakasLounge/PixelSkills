package com.lypaka.pixelskills.Listeners;

import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.Generations.Breeder.GensHatchEggListener;
import com.lypaka.pixelskills.Listeners.Generations.Breeder.GensMakeEggListener;
import com.lypaka.pixelskills.Listeners.Generations.Collector.GensCaptureListener;
import com.lypaka.pixelskills.Listeners.Generations.Darwinist.GensEvolutionListener;
import com.lypaka.pixelskills.Listeners.Generations.Fisherman.BPSFishListener;
import com.lypaka.pixelskills.Listeners.Generations.Fisherman.GensPixelmonFishListener;
import com.lypaka.pixelskills.Listeners.Generations.Gladiator.GensNPCDefeatListener;
import com.lypaka.pixelskills.Listeners.Generations.Gladiator.GensPokemonDefeatListener;
import com.lypaka.pixelskills.Listeners.Generations.Gladiator.KillPokemonListener;
import com.lypaka.pixelskills.Listeners.Generations.Gladiator.NobleDefeatListener;
import com.lypaka.pixelskills.Listeners.Reforged.Breeder.ReforgedHatchEggListener;
import com.lypaka.pixelskills.Listeners.Reforged.Breeder.ReforgedMakeEggListener;
import com.lypaka.pixelskills.Listeners.Reforged.Collector.ReforgedCaptureListener;
import com.lypaka.pixelskills.Listeners.Reforged.Darwinist.ReforgedEvolutionListener;
import com.lypaka.pixelskills.Listeners.Reforged.Fisherman.ReforgedPixelmonFishListener;
import com.lypaka.pixelskills.Listeners.Reforged.Gladiator.ReforgedNPCDefeatListener;
import com.lypaka.pixelskills.Listeners.Reforged.Gladiator.ReforgedPokemonDefeatListener;
import com.lypaka.pixelskills.PixelSkills;
import com.pixelmongenerations.core.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

import java.util.Map;

public class EventRegistry {

    public static void registerGenerationsEvents() {

        MinecraftForge.EVENT_BUS.register(new GensHatchEggListener());
        MinecraftForge.EVENT_BUS.register(new GensMakeEggListener());
        MinecraftForge.EVENT_BUS.register(new GensCaptureListener());
        MinecraftForge.EVENT_BUS.register(new GensEvolutionListener());
        if (Loader.isModLoaded("betterpixelmonspawner")) {

            MinecraftForge.EVENT_BUS.register(new BPSFishListener());

        } else {

            MinecraftForge.EVENT_BUS.register(new GensPixelmonFishListener());

        }
        MinecraftForge.EVENT_BUS.register(new NobleDefeatListener());
        MinecraftForge.EVENT_BUS.register(new GensPokemonDefeatListener());
        MinecraftForge.EVENT_BUS.register(new GensNPCDefeatListener());
        Map<String, Boolean> map = SkillGetters.gladiatorTaskMap;
        if (map.containsKey("Killing-Pokemon")) {

            if (map.get("Killing-Pokemon")) {

                if (PixelmonConfig.canPokemonBeHit) {

                    MinecraftForge.EVENT_BUS.register(new KillPokemonListener());

                } else {

                    PixelSkills.logger.warn("Detected to use Killing-Pokemon task in the Gladiator skill, but \"canPokemonBeHit\" in the pixelmon.hocon file is set to false! It needs to be set to true (and a server restart) to use this task!");

                }

            }

        }

    }

    public static void registerReforgedEvents() {

        Pixelmon.EVENT_BUS.register(new ReforgedHatchEggListener());
        Pixelmon.EVENT_BUS.register(new ReforgedMakeEggListener());
        Pixelmon.EVENT_BUS.register(new ReforgedCaptureListener());
        Pixelmon.EVENT_BUS.register(new ReforgedEvolutionListener());
        Pixelmon.EVENT_BUS.register(new ReforgedPixelmonFishListener());
        Pixelmon.EVENT_BUS.register(new ReforgedNPCDefeatListener());
        Pixelmon.EVENT_BUS.register(new ReforgedPokemonDefeatListener());

    }

}
