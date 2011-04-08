package org.movealong.script;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Singleton;
import org.movealong.config.PropertiesModule;
import org.movealong.config.Setting;
import org.movealong.jumpstart.deploy.Deployable;
import org.movealong.jumpstart.deploy.Dispatcher;
import org.movealong.jumpstart.deploy.JumpstartModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: inkblot
 * Date: Apr 4, 2011
 * Time: 7:41:56 AM
 */
@Singleton
public class Main extends Thread {
    private static transient final Logger logger = LoggerFactory.getLogger(Main.class);
    private final Deployable dispatcher;
    private final Thread shutdownHook;

    @Inject
    public Main(@Dispatcher Deployable dispatcher, ShutdownHook shutdownHook) {
        setName("Blerg-Main");
        this.dispatcher = dispatcher;
        this.shutdownHook = shutdownHook;
    }

    @Override
    public void run() {
        logger.info("Registering shutdown hook");
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        logger.info("Deploying");
        dispatcher.deploy();
    }

    public static void main(String[] argv) {
        Setting scriptModuleSetting = new PropertiesModule("script.properties").get("script.module");
        try {
            Module scriptModule = (Module) scriptModuleSetting.classValue().newInstance();
            Guice.createInjector(new JumpstartModule(), scriptModule).getInstance(Main.class).start();
        } catch (InstantiationException e) {
            throw new ScriptException("Could not instantiate script module: " + scriptModuleSetting.value(), e);
        } catch (IllegalAccessException e) {
            throw new ScriptException("Could not access script module constructor: " + scriptModuleSetting.value(), e);
        } catch (ClassNotFoundException e) {
            throw new ScriptException("Script module class not found: " + scriptModuleSetting.value(), e);
        } catch (ClassCastException e) {
            throw new ScriptException("Class is not a Guice module: " + scriptModuleSetting.value(), e);
        }
    }

}
