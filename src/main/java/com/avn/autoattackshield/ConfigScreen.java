package com.avn.autoattackshield;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreen {

    public static Screen get(Screen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("AutoAttackShield Settings"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory category = builder.getOrCreateCategory(Text.literal("General"));

        category.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Enable Auto Attack"), Config.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> Config.enabled = newValue)
                .build()
        );

        category.addEntry(entryBuilder
                .startIntSlider(Text.literal("Delay (ticks)"), Config.delayTicks, 0, 20)
                .setDefaultValue(5)
                .setSaveConsumer(newValue -> Config.delayTicks = newValue)
                .build()
        );

        builder.setSavingRunnable(() -> {
            // если надо — добавлю сохранение в файл JSON
        });

        return builder.build();
    }
}
