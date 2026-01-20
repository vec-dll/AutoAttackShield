package com.avn.autoattackshield;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.text.Text;

public class AutoAttackShield implements ClientModInitializer {

    public static boolean ENABLED = true;
    private boolean attacked = false;

    @Override
    public void onInitializeClient() {
        Keybinds.register();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            handleKey(client);
            onTick(client);
        });
    }

    private void handleKey(MinecraftClient client) {
        while (Keybinds.TOGGLE.wasPressed()) {
            ENABLED = !ENABLED;

            if (client.player != null) {
                client.player.sendMessage(
                        Text.literal("AutoAttackShield: " + (ENABLED ? "§aON" : "§cOFF")),
                        true
                );
            }
        }
    }

    private void onTick(MinecraftClient client) {
        if (!ENABLED) return;
        if (client.player == null || client.world == null) return;

        HitResult hit = client.crosshairTarget;
        if (!(hit instanceof EntityHitResult ehr)) {
            attacked = false;
            return;
        }

        if (!(ehr.getEntity() instanceof PlayerEntity target)) {
            attacked = false;
            return;
        }

        if (!target.isUsingItem() || !target.getActiveItem().isOf(net.minecraft.item.Items.SHIELD)) {
            attacked = false;
            return;
        }

        if (attacked) return;

        ClientPlayerEntity player = client.player;
        int axeSlot = findAxe(player);
        if (axeSlot == -1) return;

        int oldSlot = player.getInventory().selectedSlot;

        player.getInventory().selectedSlot = axeSlot;
        client.interactionManager.attackEntity(player, target);
        player.swingHand(player.getActiveHand());
        player.getInventory().selectedSlot = oldSlot;

        attacked = true;
    }

    private int findAxe(ClientPlayerEntity player) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() instanceof AxeItem) {
                return i;
            }
        }
        return -1;
    }
}
