package org.movealong.script;

import com.google.inject.Inject;
import org.movealong.jumpstart.deploy.Deployable;
import org.movealong.jumpstart.deploy.Dispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* Created by IntelliJ IDEA.
* User: inkblot
* Date: Apr 4, 2011
* Time: 11:15:03 PM
*/
class ShutdownHook extends Thread {
    private static transient final Logger logger = LoggerFactory.getLogger(ShutdownHook.class);
    private final Deployable dispatcher;

    @Inject
    public ShutdownHook(@Dispatcher Deployable dispatcher) {
        this.dispatcher = dispatcher;
        setName("Blerg-ShutdownHook");
    }

    @Override
    public void run() {
        logger.info("Undeploying");
        dispatcher.undeploy();
        logger.info("Done");
    }
}
