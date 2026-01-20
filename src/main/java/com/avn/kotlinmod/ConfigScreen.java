package com.avn.autoattackshield;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreen {

    public static Screen create(Screen parent) {

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Settings"));

        ConfigEntryBuilder entry = builder.entryBuilder();
        var cat = builder.getOrCreateCategory(Text.literal("General"));

        // ON/OFF toggle
        cat.addEntry(
                entry.startBooleanToggle(Text.literal("Enable"), Config.enabled)
                        .setSaveConsumer(val -> Config.enabled = val)
                        .setDefaultValue(true)
                        .build()
        );

        // Delay slider (0–20 ticks)
        cat.addEntry(
                entry.startIntSlider(Text.literal("Delay (ticks)"), Config.delayTicks, 0, 20)
                        .setSaveConsumer(val -> Config.delayTicks = val)
                        .setDefaultValue(5)
                        .build()
        );

        return builder.build();
    }
}
