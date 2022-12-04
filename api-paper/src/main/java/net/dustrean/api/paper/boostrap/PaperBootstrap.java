package net.dustrean.api.paper.boostrap;

import net.dustrean.api.paper.CorePaperAPI;
import net.dustrean.libloader.boot.Bootstrap;
import net.dustrean.libloader.boot.loaders.URLClassLoaderJarLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URLClassLoader;

public class PaperBootstrap extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            Bootstrap.apply(new URLClassLoaderJarLoader((URLClassLoader) this.getClass().getClassLoader()));
            CorePaperAPI.INSTANCE.init(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        CorePaperAPI.INSTANCE.shutdown();
    }
}
