package io.github.racoondog.crasher;

import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class Crasher extends MeteorAddon {
    @Override
    public void onInitialize() {
        Modules.get().add(new CrasherModule());
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("RacoonDog", "SwapCrasher");
    }

    @Override
    public String getPackage() {
        return "io.github.racoondog.crasher";
    }
}
