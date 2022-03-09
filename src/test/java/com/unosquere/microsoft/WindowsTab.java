package com.unosquere.microsoft;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WindowsTab {

    private WebDriver driver;
    private static String priceStored = "noValue";

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

    @Test(priority = 1, groups = {""})
    public void windowsItems() {


        //    open page
        String url = "https://www.microsoft.com/en-us/windows/?r=1";
        driver.get(url);

        //general wait
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // enter Windows button
        WebElement windowsButton = driver.findElement(By.xpath("//nav[@id=\"uhf-c-nav\"]"));
        wait.until(ExpectedConditions.elementToBeClickable(windowsButton));
        windowsButton.click();


        List<WebElement> windowsList = driver.findElements(By.xpath("//a[starts-with(@id, 'shellmenu')]"));

        //System.out.println(itemList.size());

        for (int i = 0; i < windowsList.size(); i++) {
            System.out.println(windowsList.get(i).getText());
        }

    }

    @Parameters({"keyProduct"})
    @Test(priority = 1, groups = {""})
    public void searchProduct(String keyProduct) {

        //    open page
        String url = "https://www.microsoft.com/en-us/windows/?r=1";
        driver.get(url);

        //general wait
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // enter Search button
        WebElement windowsButton = driver.findElement(By.xpath("//button[@id=\"search\"]"));
        wait.until(ExpectedConditions.elementToBeClickable(windowsButton));
        windowsButton.click();

        //Send information
        WebElement sarchButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"search\"][@data-bi-name=\"search\"]")));
        sarchButton.sendKeys(keyProduct);


        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"universal-header-search-auto-suggest-ul\"]/li[3]")));
        List<WebElement> productList = driver.findElements(By.xpath("//*[@id=\"universal-header-search-auto-suggest-ul\"]/li[position()<=3]/a"));
        List<String> prices;
        List<String> linkVisualElements = new ArrayList();
        ;
        String urlTemp = "";
        String mainTab = driver.getWindowHandle();
        String newTab = "";

        for (int i = 0; i < productList.size(); i++) {
            urlTemp = productList.get(i).getAttribute("href");
            linkVisualElements.add(urlTemp);
        }

        for (int j = 0; j < linkVisualElements.size(); j++) {
            ((JavascriptExecutor) driver).executeScript("window.open('" + linkVisualElements.get(j) + "')");
        }
        //((JavascriptExecutor)driver).executeScript("window.open('"+linkVisualElements.get(0)+"')");


        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        String tempPrice;
        for (int i = 1; i < tabs.size(); i++) {
            driver.switchTo().window(tabs.get(i));

            try{
                tempPrice = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"rootContainer_BuyBox\"]/section/div[1]/div[3]/div/p/span"))).getText();


            if(i==3){
                priceStored = tempPrice;

            }
                //System.out.println("seteada la variable con: "+ priceStored);
                System.out.println(tempPrice);
            }catch(Exception e) {
                System.out.println("Free");
            }

            driver.close();
        }

        driver.switchTo().window(tabs.get(0));
        System.out.println(driver.getCurrentUrl());

    }




    @Parameters({"keyProduct"})
    @Test(priority = 2, groups = {""})
    public void comparePrice (String keyProduct) {
        //    open page
        String url = "https://www.microsoft.com/en-us/windows/?r=1";
        driver.get(url);

        //general wait
        WebDriverWait wait = new WebDriverWait(driver, 10);

        // enter Search button
        WebElement windowsButton = driver.findElement(By.xpath("//button[@id=\"search\"]"));
        wait.until(ExpectedConditions.elementToBeClickable(windowsButton));
        windowsButton.click();

        //Send information
        WebElement sarchButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"search\"][@data-bi-name=\"search\"]")));
        sarchButton.sendKeys(keyProduct);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"universal-header-search-auto-suggest-ul\"]/li[1]")));
        sleep(1000);
        WebElement firstProduct = driver.findElement(By.xpath("//*[@id=\"universal-header-search-auto-suggest-ul\"]/li[1]"));

        firstProduct.click();

        String actualPrice = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"rootContainer_BuyBox\"]/section/div[1]/div[3]/div/p/span"))).getText();
        Assert.assertEquals(actualPrice,priceStored,"The prices do not match");

        driver.findElement(By.xpath("//*[@id=\"emailSup-modal\"]/div/div/div[1]/button")).click();

        //sleep(1500);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"rootContainer_BuyBox\"]/section/div[1]/div[3]/div/div/div/button")));
        WebElement addCartButton = driver.findElement(By.xpath("//*[@id=\"rootContainer_BuyBox\"]/section/div[1]/div[3]/div/div/div/button"));

        addCartButton.click();


        Assert.assertEquals(driver.getCurrentUrl(),"https://www.microsoft.com/en-us/d/visual-studio-professional-subscription/dg7gmgf0dst3?activetab=pivot:overviewtab","The url page does not matches");

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
