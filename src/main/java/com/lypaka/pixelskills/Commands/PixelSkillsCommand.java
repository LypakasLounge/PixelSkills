package com.lypaka.pixelskills.Commands;

import com.lypaka.pixelskills.GUIs.MainMenu;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.command.CommandTreeBase;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PixelSkillsCommand extends CommandTreeBase {

    public PixelSkillsCommand() {

        addSubcommand(new ReloadCommand());
        addSubcommand(new MessageCommand());
        addSubcommand(new SetLevelCommand());

    }

    @Override
    public String getName() {

        return "pixelskills";

    }

    @Override
    public List<String> getAliases() {

        List<String> a = new ArrayList<>();
        a.add("skills");
        a.add("pskills");
        return a;

    }

    @Override
    public List<String> getTabCompletions (MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {

        List<String> cmds = new ArrayList<>();
        if (args.length <= 1) {

            cmds.add("reload");
            cmds.add("msg");
            cmds.add("setlvl");

        } else {

            String arg = args[0];
            if (arg.equalsIgnoreCase("msg") || arg.equalsIgnoreCase("message")) {

                cmds.add("bar");
                cmds.add("chat");
                cmds.add("none");

            } else if (arg.equalsIgnoreCase("setlevel") || arg.equalsIgnoreCase("setlvl")) {

                if (args.length <= 2) {

                    return PixelSkillsCommand.getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());

                } else if (args.length <= 3) {

                    List<String> skills = new ArrayList<>();
                    for (Map.Entry<String, Skill> entry : PixelSkills.skillConfigManager.entrySet()) {

                        skills.add(entry.getValue().getSkillName());

                    }

                    return PixelSkillsCommand.getListOfStringsMatchingLastWord(args, skills);

                }

            }

        }

        return cmds;

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/pixelskills [<arg>]";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length > 0) {

            String arg = args[0];
            if (arg.equalsIgnoreCase("reload")) {

                ReloadCommand reloadCommand = new ReloadCommand();
                reloadCommand.execute(server, sender, args);

            } else if (arg.equalsIgnoreCase("msg") || arg.equalsIgnoreCase("message")) {

                MessageCommand messageCommand = new MessageCommand();
                messageCommand.execute(server, sender, args);

            } else if (arg.equalsIgnoreCase("setlvl") || arg.equalsIgnoreCase("setlevel")) {

                SetLevelCommand setLevelCommand = new SetLevelCommand();
                setLevelCommand.execute(server, sender, args);

            }

        } else {

            if (sender instanceof EntityPlayerMP) {

                EntityPlayerMP player = (EntityPlayerMP) sender;
                try {

                    MainMenu.open(player);

                } catch (ObjectMappingException e) {

                    e.printStackTrace();

                }

            }

        }

    }
}
