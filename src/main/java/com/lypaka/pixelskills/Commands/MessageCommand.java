package com.lypaka.pixelskills.Commands;

import com.lypaka.lypakautils.FancyText;
import com.lypaka.lypakautils.PermissionHandler;
import com.lypaka.pixelskills.Config.ConfigGetters;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class MessageCommand extends CommandBase {

    @Override
    public String getName() {

        return "message";

    }

    @Override
    public List<String> getAliases() {

        List<String> a = new ArrayList<>();
        a.add("msg");
        return a;

    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "/pixelskills message <off|bar|chat>";

    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (sender instanceof EntityPlayerMP) {

            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (!PermissionHandler.hasPermission(player, "pixelskills.command.message")) {

                player.sendMessage(FancyText.getFormattedText("&cYou don't have permission to use this command!"));
                return;

            }

            if (ConfigGetters.defaultMessageSystem.equalsIgnoreCase("disabled")) {

                player.sendMessage(FancyText.getFormattedText("&eThe message system is disabled, so there's no lists to add you to!"));
                return;

            }

            if (args.length < 2) {

                player.sendMessage(FancyText.getFormattedText(getUsage(player)));

            } else {

                String msgArg = args[0];
                String mode = args[1];
                ArrayList<String> listToAddTo;
                String type;
                if (mode.equalsIgnoreCase("chat")) {

                    listToAddTo = ConfigGetters.chatList;
                    type = "chat";

                } else if (mode.equalsIgnoreCase("bar")) {

                    listToAddTo = ConfigGetters.bossBarList;
                    type = "Boss Bar";

                } else {

                    listToAddTo = ConfigGetters.noneList;
                    type = "disabled";

                }

                if (!listToAddTo.contains(player.getUniqueID().toString())) {

                    removeFromOtherListsIfThere(player.getUniqueID().toString(), type);
                    listToAddTo.add(player.getUniqueID().toString());
                    player.sendMessage(FancyText.getFormattedText("&aSuccessfully updated your chat system to the &e" + type + " &amodule!"));

                } else {

                    player.sendMessage(FancyText.getFormattedText("&cYou're already added to this list!"));

                }

            }

        }

    }

    private void removeFromOtherListsIfThere (String uuid, String moduleUsed) {

        ArrayList<String> list1;
        ArrayList<String> list2;

        if (moduleUsed.equals("chat")) {

            list1 = ConfigGetters.bossBarList;
            list2 = ConfigGetters.noneList;

        } else if (moduleUsed.equals("Boss Bar")) {

            list1 = ConfigGetters.chatList;
            list2 = ConfigGetters.noneList;

        } else {

            list1 = ConfigGetters.bossBarList;
            list2 = ConfigGetters.chatList;

        }

        list1.removeIf(entry -> entry.equalsIgnoreCase(uuid));
        list2.removeIf(entry -> entry.equalsIgnoreCase(uuid));

    }

}
