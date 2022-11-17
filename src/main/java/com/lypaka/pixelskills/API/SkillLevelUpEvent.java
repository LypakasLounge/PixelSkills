package com.lypaka.pixelskills.API;

import com.lypaka.pixelskills.SkillRegistry.Skill;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when a player is set to level up a skill to its next level
 */
public class SkillLevelUpEvent extends Event {

    private final EntityPlayerMP player;
    private final Skill skill;
    private final int startingLevel;
    private final int endingLevel;

    public SkillLevelUpEvent (EntityPlayerMP player, Skill skill, int startingLevel, int endingLevel) {

        this.player = player;
        this.skill = skill;
        this.startingLevel = startingLevel;
        this.endingLevel = endingLevel;

    }

    public EntityPlayerMP getPlayer() {

        return this.player;

    }

    public Skill getSkill() {

        return this.skill;

    }

    public int getStartingLevel() {

        return this.startingLevel;

    }

    public int getEndingLevel() {

        return this.endingLevel;

    }

    public int getLevelsIncreased() {

        return this.endingLevel - this.startingLevel;

    }

}
