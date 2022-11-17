package com.lypaka.pixelskills.GUIs;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.google.common.reflect.TypeToken;
import com.lypaka.lypakautils.FancyText;
import com.lypaka.pixelskills.Config.ConfigGetters;
import com.lypaka.pixelskills.Config.SkillGetters;
import com.lypaka.pixelskills.Listeners.JoinListener;
import com.lypaka.pixelskills.PixelSkills;
import com.lypaka.pixelskills.PlayerAccounts.Account;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainMenu {

    public static void open (EntityPlayerMP player) throws ObjectMappingException {

        Account account = JoinListener.accountMap.get(player.getUniqueID());
        ChestTemplate template = ChestTemplate.builder(ConfigGetters.guiRows).build();
        GooeyPage page = GooeyPage.builder()
                .template(template)
                .title(FancyText.getFormattedString(ConfigGetters.displayTitle))
                .build();

        for (int i : ConfigGetters.borderSlots) {

            page.getTemplate().getSlot(i).setButton(getBorderButton());

        }

        for (Map.Entry<String, Map<String, String>> entry : ConfigGetters.skillSlots.entrySet()) {

            String skill = entry.getKey();
            Map<String, String> data = entry.getValue();
            String id = data.get("ID");
            int meta = 0;
            if (data.containsKey("Metadata")) {

                meta = Integer.parseInt(data.get("Metadata"));

            }
            String displayName = data.get("Display-Name");
            List<String> loreStrings = new ArrayList<>();
            if (!PixelSkills.configManager.getConfigNode(2, "Slots", skill, "Lore").isVirtual()) {

                loreStrings = PixelSkills.configManager.getConfigNode(2, "Slots", skill, "Lore").getList(TypeToken.of(String.class));

            }

            int slot = Integer.parseInt(data.get("Slot"));
            page.getTemplate().getSlot(slot).setButton(getSkillButton(skill, id, meta, displayName, loreStrings, account));

        }

        UIManager.openUIForcefully(player, page);

    }

    private static Button getSkillButton (String skill, String id, int meta, String displayName, List<String> loreString, Account account) {

        ItemStack item = new ItemStack(Item.getByNameOrId(id));
        if (meta > 0) {

            item.setItemDamage(meta);

        }
        if (!loreString.isEmpty()) {

            int level = account.getLevel(skill);
            double exp = account.getEXP(skill);
            double needed = 0;
            Map<String, Double> levelUpMap = SkillGetters.skillLevelUpMaps.get(skill);
            int nextLevel = level + 1;
            if (levelUpMap.containsKey("Level-" + nextLevel)) {

                needed = levelUpMap.get("Level-" + nextLevel);

            }

            NBTTagList lore = new NBTTagList();
            for (String s : loreString) {

                lore.appendTag(new NBTTagString(FancyText.getFormattedString(s
                        .replace("%level%", String.valueOf(level))
                        .replace("%current%", String.valueOf(exp))
                        .replace("%needed%", String.valueOf(needed))
                )));

            }

            item.getOrCreateSubCompound("display").setTag("Lore", lore);

        }

        item.setStackDisplayName(FancyText.getFormattedString(displayName));
        return GooeyButton.builder().display(item).build();

    }

    private static Button getBorderButton() {

        ItemStack item = new ItemStack(Item.getByNameOrId(ConfigGetters.borderID));
        if (ConfigGetters.borderMeta > 0) {

            item.setItemDamage(ConfigGetters.borderMeta);

        }
        item.setStackDisplayName(FancyText.getFormattedString(""));
        return GooeyButton.builder().display(item).build();

    }

}
