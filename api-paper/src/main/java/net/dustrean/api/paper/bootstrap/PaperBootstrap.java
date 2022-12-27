package net.dustrean.api.paper.bootstrap;

import net.dustrean.api.paper.CorePaperAPI;
import net.dustrean.api.redis.codec.GsonCodec;
import net.dustrean.libloader.boot.Bootstrap;
import net.dustrean.libloader.boot.loaders.URLClassLoaderJarLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;

public class PaperBootstrap extends JavaPlugin {

    @Override
    public void onEnable() {
        try {
            new Bootstrap().apply(new URLClassLoaderJarLoader((URLClassLoader) this.getClass().getClassLoader()));
            CorePaperAPI.INSTANCE.init(this);
            ((GsonCodec) CorePaperAPI.INSTANCE.getRedisConnection().redisClient.getConfig().getCodec()).getClassLoaders().add(this.getClass().getClassLoader());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        CorePaperAPI.INSTANCE.shutdown();
    }
}
