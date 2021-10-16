package mobile.testing.configuration;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Optional;
import org.apache.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static io.appium.java_client.service.local.flags.GeneralServerFlag.LOG_LEVEL;
import static io.appium.java_client.service.local.flags.GeneralServerFlag.SESSION_OVERRIDE;

public class AddressConfigurator {

    private static final Logger LOG = (Logger) LogManager.getRootLogger();
    private static final String ERROR_LOG_LEVEL = "error";
    private static AppiumDriverLocalService appiumDriverLocalService;
    private static final String KILL_NODE_COMMAND = "taskkill /F /IM node.exe";

    private AddressConfigurator() {
    }


    public static AppiumDriverLocalService getAppiumDriverLocalService(int port) {
        if (appiumDriverLocalService == null) startServise(port);
        return appiumDriverLocalService;
    }

    public static void startServise(int port) {
        makeAvailableIfOccupied(port);
        appiumDriverLocalService = new AppiumServiceBuilder()
                .withIPAddress(ConfigurationReader.get().appiumAddress())
                .usingPort(port)
                .withArgument(SESSION_OVERRIDE)
                .withArgument(LOG_LEVEL, ERROR_LOG_LEVEL)
                .build();
        appiumDriverLocalService.start();
        LOG.info("Appium server started on port {}", port);
    }

    public static void stopService() {
        Optional.ofNullable(appiumDriverLocalService).ifPresent(service -> {
            service.stop();
            LOG.info("Appium server stopped");
        });
    }

    public static void makeAvailableIfOccupied(int port) {
        if (!isPortFree(port)) {
            try {
                Runtime.getRuntime().exec(KILL_NODE_COMMAND);
            } catch (IOException e) {
                LOG.error("Couldn't execute runtime comand, massage: {}", e.getMessage());
            }
        }

    }

    private static boolean isPortFree(int port) {
        boolean isFree = true;
        try (ServerSocket ignored = new ServerSocket(port)) {
            LOG.info("Specified port {} is available and rready to use", port);
        } catch (Exception e) {
            isFree = false;
            LOG.warn("Specified port - {} is occupied by some process, process will be terminated", port);
        }
        return isFree;
    }
}
