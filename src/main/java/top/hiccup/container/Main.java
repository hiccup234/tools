package top.hiccup.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Main（仿Dubbo容器启动类）
 *
 * @author wenhy
 * @date 2019/9/5
 */
public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static volatile boolean running = true;

    public static void main(String[] args) {
        final List<Container> containers = new ArrayList<Container>();
        try {
            if (args != null && args.length > 0) {

            } else {
                ServiceLoader<Container> serviceLoader = ServiceLoader.load(Container.class);
                for (Container container : serviceLoader) {
                    containers.add(container);
                }
            }
        } catch (Exception e) {
            LOGGER.error("fail to start: ", e);
            System.exit(1);
        }
        for (Container container : containers) {
            container.start();
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (Container container : containers) {
                    try {
                        container.stop();
                        LOGGER.info(container.name() + " stopped!");
                    } catch (Throwable t) {
                        LOGGER.error(t.getMessage(), t);
                    }
                    synchronized (Main.class) {
                        running = false;
                        Main.class.notify();
                    }
                }
            }
        });
        // blocked main thread
        synchronized (Main.class) {
            while (running) {
                try {
                    Main.class.wait();
                } catch (InterruptedException e) {
                    // do nothing..
                }
            }
        }
    }
}
