package mobile.testing.pages;

import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import mobile.testing.driver.DriverManger;
import org.openqa.selenium.support.PageFactory;

public class BasePage {

    public BasePage() {
        PageFactory.initElements(new AppiumFieldDecorator(DriverManger.getDriver()), this);

    }
}
