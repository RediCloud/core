package dev.redicloud.api.paper.bootstrap;

import dev.redicloud.api.paper.CorePaperAPI;
import dev.redicloud.api.redis.codec.GsonCodec;
import dev.redicloud.libloader.boot.Bootstrap;
import dev.redicloud.libloader.boot.loaders.URLClassLoaderJarLoader;
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
            CorePaperAPI.INSTANCE.initialized();
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        CorePaperAPI.INSTANCE.shutdown();
    }
}
