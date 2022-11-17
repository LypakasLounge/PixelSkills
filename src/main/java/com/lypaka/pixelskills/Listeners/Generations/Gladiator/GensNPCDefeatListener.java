package com.lypaka.pixelskills.Listeners.Generations.Gladiator;

import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.pixelskills.API.SkillEXPEvent;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.pixelmongenerations.api.events.npc.BeatTrainerEvent;
import com.pixelmongenerations.common.entity.npcs.NPCTrainer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

public class GensNPCDefeatListener {

    @SubscribeEvent
    public void onNPCDefeat (BeatTrainerEvent event) {

        EntityPlayerMP player = event.getPlayer();
        NPCTrainer trainer = event.getTrainer();
        String worldName = trainer.world.getWorldInfo().getWorldName();
        int x = trainer.getPosition().getX();
        int y = trainer.getPosition().getY();
        int z = trainer.getPosition().getZ();
        String location = worldName + "," + x + "," + y + "," + z;

        if (!ConfigGetters.isSkillEnabled("Gladiator")) return;
        Skill skill = PixelSkills.skillConfigManager.get("Gladiator");
        if (!PermissionHandler.hasPermission(player, skill.getAccessPermission())) {

            if (!skill.getAccessPermission().equals("")) return;

        }

        Map<String, Boolean> map = SkillGetters.gladiatorTaskMap;
        if (map.containsKey("Defeating-NPC-Trainers")) {

            if (!map.get("Defeating-NPC-Trainers")) return;

        }

        if (!SkillGetters.gladiatorNPCBlacklist.isEmpty()) {

            if (SkillGetters.gladiatorNPCBlacklist.contains(location)) return;

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
