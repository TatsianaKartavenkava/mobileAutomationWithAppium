package mobile.testing;

import mobile.testing.driver.DriverManger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;

public class BaseTest {

    @BeforeClass
    public void createSession() {
        DriverManger.getDriver();
    }

    @AfterMethod
    public void resetApp() {
        DriverManger.getDriver().resetApp();
    }

    @AfterClass
    public void closeSession() {
        DriverManger.closeDriver();
        DriverManger.closeAppium();
        DriverManger.closeEmulator();
    }
}
