package net.dustrean.api.velocity.bootstrap;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.ProxyServer;
import net.dustrean.api.velocity.VelocityCoreAPI;
import net.dustrean.libloader.boot.Bootstrap;
import net.dustrean.libloader.boot.loaders.URLClassLoaderJarLoader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLClassLoader;

@Plugin(
    id = "core",
    name = "core",
    version = "1.0.0-SNAPSHOT",
    description = "Core plugin for the Dustrean API",
    authors = "Dustrean-Team"
)
public class VelocityBootstrap {

    private final ProxyServer proxyServer;

    @Inject
    public VelocityBootstrap(ProxyServer proxyServer) throws IOException, URISyntaxException {
        this.proxyServer = proxyServer;
        new Bootstrap().apply(new URLClassLoaderJarLoader((URLClassLoader) this.getClass().getClassLoader()));
    }

    @Subscribe
    public void onInit(ProxyInitializeEvent event) {
        VelocityCoreAPI.INSTANCE.init(this.proxyServer, (PluginContainer) this);
    }

    @Subscribe
    public void onShutdown(ProxyShutdownEvent event) {
        VelocityCoreAPI.INSTANCE.shutdown();
    }

}
