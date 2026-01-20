package com.avn.autoattackshield.modmenu;

import com.avn.autoattackshield.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return parent -> ConfigScreen.get(parent);
    }
}
