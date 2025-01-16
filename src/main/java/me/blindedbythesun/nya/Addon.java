package me.blindedbythesun.nya;

import me.blindedbythesun.nya.modules.ActualBlink;
import me.blindedbythesun.nya.modules.AntiBlyat;
import me.blindedbythesun.nya.modules.AutoMeow;
import me.blindedbythesun.nya.modules.Mimic;
import me.blindedbythesun.nya.modules.notifications.Notifications;
import me.blindedbythesun.nya.modules.efly.ElytraFlyPlus;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class Addon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("nya");
    public static final Notifications notifications = new Notifications();

    @Override
    public void onInitialize() {
        LOG.info("Loading addon");

        // Modules
        Modules.get().add(new ElytraFlyPlus());
        Modules.get().add(new ActualBlink());
        Modules.get().add(new AutoMeow());
        Modules.get().add(notifications);
        Modules.get().add(new Mimic());
        Modules.get().add(new AntiBlyat());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "me.blindedbythesun.nya";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("blindedbythesun", "nya");
    }
}
