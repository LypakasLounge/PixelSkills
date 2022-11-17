package com.lypaka.pixelskills.API;

import com.lypaka.pixelskills.SkillRegistry.Skill;
import com.lypaka.pixelskills.SkillRegistry.SkillReward;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

/**
 * Called when a player is about to be rewarded with skill rewards
 * Supports external customization via use of the setCommands() method
 */
@Cancelable
public class SkillRewardEvent extends Event {

    private final EntityPlayerMP player;
    private final Skill skill;
    private final SkillReward reward;
    private List<String> commands;

    public SkillRewardEvent (EntityPlayerMP player, Skill skill, SkillReward reward) {

        this.player = player;
        this.skill = skill;
        this.reward = reward;
        this.commands = reward.getCommands();

    }

    public EntityPlayerMP getPlayer() {

        return this.player;

    }

    public Skill getSkill() {

        return this.skill;

    }

    public SkillReward getReward() {

        return this.reward;

    }

    public List<String> getCommands() {

        return this.commands;

    }

    public void setCommands (List<String> commands) {

        this.commands = commands;

    }

}
