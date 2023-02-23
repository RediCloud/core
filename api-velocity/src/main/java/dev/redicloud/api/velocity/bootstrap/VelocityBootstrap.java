package dev.redicloud.api.velocity.bootstrap;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import dev.redicloud.api.redis.codec.GsonCodec;
import dev.redicloud.api.velocity.VelocityCoreAPI;
import dev.redicloud.libloader.boot.Bootstrap;
import dev.redicloud.libloader.boot.loaders.URLClassLoaderJarLoader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;

@Plugin(id = "core", name = "core", version = "1.0.0-SNAPSHOT", description = "Core plugin for the RC-API", authors = "RediCloud-Team")
public class VelocityBootstrap {

    private final ProxyServer proxyServer;

    @Inject
    public VelocityBootstrap(ProxyServer proxyServer) throws IOException, URISyntaxException {
        this.proxyServer = proxyServer;
        new Bootstrap().apply(new URLClassLoaderJarLoader((URLClassLoader) this.getClass().getClassLoader()));
    }

    @Subscribe
    public void onInit(ProxyInitializeEvent event) {
        VelocityCoreAPI.INSTANCE.init(this.proxyServer, this);
        ((GsonCodec) VelocityCoreAPI.INSTANCE.getRedisConnection().redisClient.getConfig().getCodec()).getClassLoaders().add(this.getClass().getClassLoader());
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        VelocityCoreAPI.INSTANCE.shutdown();
    }

}
