package com.unosquere.microsoft;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class home {


    private WebDriver driver;

    @Parameters({"browser"})
    @BeforeMethod(alwaysRun = true)
    //Here We can put the browser that we want to use, for this testing I just put chrome, however we can just give the path of the driver
    private void setUp(@Optional("browser") String browser) {

        switch (browser) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver");
                driver = new ChromeDriver();
                break;

            //
            case "firefox":
                System.setProperty("webdriver.chrome.driver", "src/main/resources/firefox");
                driver = new ChromeDriver();
                break;

            default:
                System.out.println("Do not hoe to start " + browser + ", starting chrome instead");
                driver = new ChromeDriver();
                break;
        }
        driver.manage().window().maximize();

        // sleep(3000);
    }

    //
    //@Test(priority = 1, groups = {""})
    public void menuItems() {


        //    open page
        String url = "https://www.microsoft.com/en-us/";
        driver.get(url);

        sleep(2000);

        //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"l1_support\"]");
        //Find menu items elements

        List<WebElement> itemList = driver.findElements(By.xpath("//nav[@id=\"uhf-g-nav\"]/ul/li[starts-with(@class,'single-link')][not(@class='single-link js-nav-menu uhf-menu-item exp-hide-item-from-nav')]"));
        List<String> actualItems = new ArrayList<>();
        //System.out.println(itemList.size());

        for (int i = 0; i < itemList.size(); i++) {
            //loading text of each element in to array all_elements_text
            actualItems.add(itemList.get(i).getText());

            //System.out.println(itemList.get(i).getText());
        }

        //excepected results
        List<String> excpectedItems = Arrays.asList("Microsoft 365", "Office", "Windows", "Surface", "Xbox", "Deals", "Support");

        //verifications


        //Elements are contained in the expected result
        for (int i = 0; i < excpectedItems.size(); i++) {
            Assert.assertTrue(actualItems.contains(excpectedItems.get(i)), "The next" + excpectedItems.get(i));
        }

        //The same size y both List
        Assert.assertTrue(actualItems.size() == excpectedItems.size(), "There is not the same quantity: " + "Exp- " + excpectedItems.size() + "Act-" + actualItems.size());

    }


    @Test(priority = 2, groups = {""})
    public void windowsButton() {

        //    open page
        String url = "https://www.microsoft.com/en-us/";
        driver.get(url);

        //general wait
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // enter Windows button
        WebElement windowsButton = driver.findElement(By.xpath("//a[@id=\"shellmenu_2\"]"));
        wait.until(ExpectedConditions.elementToBeClickable(windowsButton));
        windowsButton.click();

        //Verifications
        String actualURL = driver.getCurrentUrl();
        Assert.assertEquals(actualURL, "https://www.microsoft.com/en-us/windows/?r=1" );

    }


     @AfterMethod(alwaysRun = true)
    private void downBrow() {
        driver.quit();
    }



    private void sleep(long m) {
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
