package com.lypaka.pixelskills.Commands;

import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.JoinListener;
import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SetLevelCommand extends CommandBase {

    @Override
    public String getName() {

        return "setlevel";

    }

    @Override
    public List<String> getAliases() {

        List<String> a = new ArrayList<>();
        a.add("setlvl");
        return a;

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/pskills setlevel <player> <skill> <level>";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (!PermissionHandler.hasPermission(player, "pixelskills.command.admin")) {

                player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"));
                return;

            }

        }

        if (args.length < 4) {

            sender.sendMessage(FancyText.getFormattedText(getUsage(sender)));
            return;

        }

        String playerArg = args[1];
        String skillArg = args[2];
        int level = Integer.parseInt(args[3]);
        EntityPlayerMP target = null;
        Skill skill = null;

        for (Map.Entry<UUID, EntityPlayerMP> entry : JoinListener.playerMap.entrySet()) {

            if (entry.getValue().getName().equalsIgnoreCase(playerArg)) {

                target = entry.getValue();
                break;

            }

        }

        if (target == null) {

            sender.sendMessage(FancyText.getFormattedText("&cInvalid player name!"));
            return;

        }

        for (Map.Entry<String, Skill> entry : PixelSkills.skillConfigManager.entrySet()) {

            if (skillArg.equalsIgnoreCase(entry.getKey())) {

                skill = entry.getValue();
                break;

            }

        }

        if (skill == null) {

            sender.sendMessage(FancyText.getFormattedText("&cInvalid skill name!"));
            return;

        }

        if (!com.lypaka.pixelskills.Listeners.JoinListener.accountMap.containsKey(target.getUniqueID())) {

            sender.sendMessage(FancyText.getFormattedText("&c" + target.getName() + " does not have an account, and this is not a good thing!"));
            return;

        }
        Account account = com.lypaka.pixelskills.Listeners.JoinListener.accountMap.get(target.getUniqueID());
        int maxLevel = SkillGetters.skillLevelUpMaps.get(skill.getSkillName()).size();
        if (level > maxLevel) {

            sender.sendMessage(FancyText.getFormattedText("&cCannot set level higher than max level!"));
            return;

        }
        if (level <= 0) {

            sender.sendMessage(FancyText.getFormattedText("&cMinimum level support is level 1!"));
            return;

        }

        int currentLevel = account.getLevel(skill);
        if (level == currentLevel) {

            sender.sendMessage(FancyText.getFormattedText("&cCannot set level to player's current level!"));
            return;

        }

        double neededEXP;
        if (level == 1) {

            neededEXP = 0.0;

        } else {

            neededEXP = SkillGetters.skillLevelUpMaps.get(skill.getSkillName()).get("Level-" + level);

        }
        account.setLevel(skill, level);
        account.setEXP(skill, neededEXP);
        sender.sendMessage(FancyText.getFormattedText("&aSuccessfully set " + target.getName() + "'s level in " + skill.getSkillName() + " to " + level + "!"));
        target.sendMessage(FancyText.getFormattedText("&eYour level in " + skill.getSkillName() + " was set to " + level + "."));

    }

}
