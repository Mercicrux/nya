package me.sun.unnamed;

import me.sun.unnamed.modules.ActualBlink;
import me.sun.unnamed.modules.AutoMeow;
import me.sun.unnamed.modules.efly.ElytraFlyPlus;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class Addon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Unnamed");

    @Override
    public void onInitialize() {
        LOG.info("Loading addon");

        // Modules
        Modules.get().add(new ElytraFlyPlus());
        Modules.get().add(new ActualBlink());
        Modules.get().add(new AutoMeow());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "me.sun.unnamed";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("MeteorDevelopment", "meteor-addon-template");
    }
}
