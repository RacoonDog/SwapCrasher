package io.github.racoondog.crasher.mixin;

import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.world.TickRate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(value = TickRate.class, remap = false)
public abstract class TickRateMixin {
    @Shadow private long timeGameJoined;
    @Shadow @Final private float[] tickRates;

    @Shadow
    private long timeLastTimeUpdate;

    /**
     * @author Crosby
     * @reason Update tick rate whilst server is frozen
     */
    @Overwrite
    public float getTickRate() {
        if (!Utils.canUpdate()) return 0;
        long now = System.currentTimeMillis();
        if (now - timeGameJoined < 4000) return 20;

        int numTicks = 0;
        float sumTickRates = 0f;
        for (float tickRate : tickRates) {
            if (tickRate > 0) {
                sumTickRates += tickRate;
                numTicks++;
            }
        }
        if (now - timeLastTimeUpdate > 50) {
            sumTickRates += MathHelper.clamp((now - timeLastTimeUpdate) / 1000f, 0f, 20f);
            numTicks++;
        }
        return sumTickRates / numTicks;
    }
}
