package com.lypaka.pixelskills.PlayerAccounts;

import com.lypaka.lypakautils.FancyText;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.SkillRegistry.Skill;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MessageSystem {

    public static void sendBossBarEXP (EntityPlayerMP player, Skill skill) {

        Account account = JoinListener.accountMap.get(player.getUniqueID());
        Map<String, Double> levelUpMap = SkillGetters.skillLevelUpMaps.get(skill.getSkillName());
        int currentLevel = account.getLevel(skill);
        int nextLevel = currentLevel + 1;
        if (levelUpMap.containsKey("Level-" + nextLevel)) {

            BossInfoServer bar = new BossInfoServer(
                    FancyText.getFormattedText(ConfigGetters.bossBarEXP.replace("%skill%", skill.getSettings().getDisplayName())),
                    getColorFromName(ConfigGetters.bossBarColor),
                    BossInfo.Overlay.PROGRESS
            );
            double needed = levelUpMap.get("Level-" + nextLevel);
            double current = account.getEXP(skill);
            float percent = (float) (current / needed);
            bar.setPercent(percent);
            bar.addPlayer(player);
            bar.setVisible(true);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {

                    bar.setVisible(false);
                    bar.removePlayer(player);

                }

            }, 2000);

        }

    }

    public static void sendBossBarLevelUp (EntityPlayerMP player, Skill skill) {

        BossInfoServer bar = new BossInfoServer(
                FancyText.getFormattedText(ConfigGetters.bossBarLevelUp.replace("%skill%", skill.getSettings().getDisplayName())),
                getColorFromName(ConfigGetters.bossBarColor),
                BossInfo.Overlay.PROGRESS
        );
        bar.setPercent(1.0f);
        bar.addPlayer(player);
        bar.setVisible(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {

                bar.setVisible(false);
                bar.removePlayer(player);

            }

        }, 2000);

    }

    public static void sendChatEXP (EntityPlayerMP player, Skill skill, double exp) {

        player.sendMessage(FancyText.getFormattedText(ConfigGetters.chatEXP
                .replace("%skill%", skill.getSettings().getDisplayName())
                .replace("%exp%", String.valueOf(exp))
        ));

    }

    public static void sendChatLevelUp (EntityPlayerMP player, Skill skill, int level) {

        player.sendMessage(FancyText.getFormattedText(ConfigGetters.chatLevelUp
                .replace("%skill%", skill.getSettings().getDisplayName())
                .replace("%level%", String.valueOf(level))
        ));

    }

    private static BossInfo.Color getColorFromName (String name) {

        switch (name.toLowerCase()) {

            case "pink":
                return BossInfo.Color.PINK;

            case "blue":
                return BossInfo.Color.BLUE;

            case "red":
                return BossInfo.Color.RED;

            case "green":
                return BossInfo.Color.GREEN;

            case "yellow":
                return BossInfo.Color.YELLOW;

            case "purple":
                return BossInfo.Color.PURPLE;

            default:
                return BossInfo.Color.WHITE;

        }

    }

}
