package com.avn.autoattackshield;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class ExampleMod implements ClientModInitializer {

    private boolean waiting = false;
    private int timer = 0;

    @Override
    public void onInitializeClient() {

        Keybinds.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Keybinds.onTick(client);
            onTick(client);
        });
    }

    private void onTick(MinecraftClient client) {
        if (!Config.enabled) return;
        if (client.player == null) return;
        if (client.world == null) return;

        HitResult hit = client.crosshairTarget;
        if (!(hit instanceof EntityHitResult ehr)) {
            reset();
            return;
        }

        if (!(ehr.getEntity() instanceof PlayerEntity target)) {
            reset();
            return;
        }

        if (!target.isUsingItem() || target.getActiveItem().getItem() != Items.SHIELD) {
            reset();
            return;
        }

        if (!waiting) {
            waiting = true;
            timer = Config.delayTicks;
            return;
        }

        if (timer > 0) {
            timer--;
            return;
        }

        attack(client.player, target, client);
        reset();
    }

    private void reset() {
        waiting = false;
        timer = 0;
    }

    private void attack(ClientPlayerEntity player, PlayerEntity target, MinecraftClient client) {
        int oldSlot = player.getInventory().selectedSlot;

        int axeSlot = findAxe(player);
        if (axeSlot == -1) return;

        player.getInventory().selectedSlot = axeSlot;
        client.interactionManager.attackEntity(player, target);
        player.swingHand(player.getActiveHand());
        player.getInventory().selectedSlot = oldSlot;
    }

    private int findAxe(ClientPlayerEntity player) {
        for (int i = 0; i < 9; i++) {
            if (player.getInventory().getStack(i).getItem() instanceof AxeItem) {
                return i;
            }
        }
        return -1;
    }
}
