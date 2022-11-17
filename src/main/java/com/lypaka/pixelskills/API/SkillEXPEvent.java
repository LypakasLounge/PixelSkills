package com.lypaka.pixelskills.API;

import com.lypaka.pixelskills.SkillRegistry.Skill;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Called when a player is set to earn EXP for a skill
 * Supports external modifications to the amount of EXP earned via use of the setEXP() method
 */
@Cancelable
public class SkillEXPEvent extends Event {

    private final EntityPlayerMP player;
    private final Skill skill;
    private double exp;

    public SkillEXPEvent (EntityPlayerMP player, Skill skill, double exp) {

        this.player = player;
        this.skill = skill;
        this.exp = exp;

    }

    public EntityPlayerMP getPlayer() {

        return this.player;

    }

    public Skill getSkill() {

        return this.skill;

    }

    public double getEXP() {

        return this.exp;

    }

    public void setEXP (double exp) {

        this.exp = exp;

    }

}
