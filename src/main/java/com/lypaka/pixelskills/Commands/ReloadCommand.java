package com.lypaka.pixelskills.Commands;

import com.lypaka.lypakautils.ConfigurationLoaders.BasicConfigManager;
import com.lypaka.lypakautils.ConfigurationLoaders.ConfigUtils;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

public class ReloadCommand extends CommandBase {

    @Override
    public String getName() {

        return "reload";

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/pixelskills reload";

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

        try {

            PixelSkills.configManager.load();
            ConfigGetters.load();
            PixelSkills.skillConfigManager = new HashMap<>();
            for (String skillString : ConfigGetters.enabledSkills) {

                String lowerSkill = skillString.toLowerCase();
                String[] skillFiles = new String[]{lowerSkill + "Settings.conf", lowerSkill + "LevelUps.conf", lowerSkill + "Rewards.conf"};
                Path skillDir = ConfigUtils.checkDir(PixelSkills.dir.resolve(skillString));
                BasicConfigManager bcm = new BasicConfigManager(skillFiles, skillDir, PixelSkills.class, PixelSkills.MOD_NAME, PixelSkills.MOD_ID, PixelSkills.logger);
                bcm.init();
                Skill skill = new Skill(skillString, PixelSkills.configManager.getConfigNode(0, "Skills", skillString, "Access-Permission").getString(), bcm);
                PixelSkills.skillConfigManager.put(skillString, skill);

            }

            SkillGetters.load();
            sender.sendMessage(FancyText.getFormattedText("&aSuccessfully reloaded PixelSkills configuration!"));

        } catch (ObjectMappingException | IOException e) {

            e.printStackTrace();

        }

    }

}
