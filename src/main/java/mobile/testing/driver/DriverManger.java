package mobile.testing.driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import mobile.testing.configuration.AddressConfigurator;
import mobile.testing.configuration.CapabilitiesConfigurator;
import mobile.testing.configuration.ConfigurationReader;
import mobile.testing.configuration.EnvironmentType;
import org.apache.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static java.lang.String.format;

public class DriverManger {

    private static final Logger LOG = (Logger) LogManager.getRootLogger();
    private static final EnvironmentType ENVIRONMENT_TYPE = EnvironmentType.valueOf(ConfigurationReader.get().env().toUpperCase());
    private static AppiumDriver<MobileElement> driver;

    private DriverManger() {
    }

    public static AppiumDriver<MobileElement> getDriver() {
        if(driver == null) {
            driver = createDriver();
        }
        return driver;
    }

    public static AppiumDriver<MobileElement> createDriver() {
        switch (ENVIRONMENT_TYPE) {
            case LOCAL:
                driver = new AndroidDriver<MobileElement>(AddressConfigurator.getAppiumDriverLocalService(ConfigurationReader.get().appiumPort()),
                        CapabilitiesConfigurator.getLocalCapabilities());
                break;
            default:
                throw new IllegalArgumentException(format("Unexpected environment value: %s", ENVIRONMENT_TYPE));
        }
        LOG.info("Driver is created");
        LOG.info("Environment type is {}", ENVIRONMENT_TYPE);
        return driver;
    }

    public static void  closeDriver() {
        Optional.ofNullable(getDriver()).ifPresent(driverInstance -> {
            driverInstance.quit();
            driver = null;
            LOG.info("Driver is closed");
        });
    }

    public static void closeAppium() {
        AddressConfigurator.stopService();
    }

    public static void closeEmulator() {
        try {
            Runtime.getRuntime().exec(format("adb -s %s emu kill", ConfigurationReader.get().udid()));
            LOG.info("AVD is closed");
        } catch (IOException e) {
            LOG.info("AVD was not closed, massage: {}", e.getMessage());
        }
    }
}


