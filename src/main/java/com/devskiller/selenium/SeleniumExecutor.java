package com.devskiller.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class SeleniumExecutor implements Executor {

    private final WebDriver driver;

    public SeleniumExecutor(WebDriver driver) {
        this.driver = driver;
    }

    WebDriverWait wait;

    /// Page 1
    @Override
    public void SetLoginAndClickNext(String login){
        String text = driver.findElement(By.xpath("(//div[@class='alert alert-primary'])[1]")).getText();
        int firstIndex = text.indexOf("(")+1;
        int lastIndex = text.indexOf(")");
        System.out.println("login = " + login);
        String login2 = text.substring(firstIndex,lastIndex);
        System.out.println("login2 = " + login2);

        wait=new WebDriverWait(driver,7);
        WebElement emailInputBox = driver.findElement(By.id("emailBox"));

        //wait with JavascriptExecutor
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        js.executeScript("arguments[0].setAttribute('value', '" + login2 + "')", emailInputBox);

        wait.until(ExpectedConditions.visibilityOf(emailInputBox));
        emailInputBox.sendKeys(login);
        WebElement loginEmailButton = driver.findElement(By.xpath("//button[text()='Next'][@class='buttonLogin']"));
        wait.until(ExpectedConditions.visibilityOf(loginEmailButton));
        loginEmailButton.click();

        //click with JavascriptExecutor
//        js.executeScript("arguments[0].click();", loginEmailButton);
    }

    /// Page 2
    @Override
    public String OpenCodePageAndReturnCode() {

        WebElement openPage = driver.findElement(By.xpath("//a[text()='open page']"));

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", openPage);


        String currentWindow = driver.getWindowHandle();
        Set<String> windowHandles = driver.getWindowHandles();
        for (String windowHandle : windowHandles) {
            if(!currentWindow.equals(windowHandle)){
                driver.switchTo().window(windowHandle);
            }
        }

        wait=new WebDriverWait(driver,7);
        WebElement code = driver.findElement(By.id("code"));
        wait.until(ExpectedConditions.visibilityOf(code));
        String textOfCode = code.getAttribute("value");
        System.out.println("textOfCode = " + textOfCode);

        for (String windowHandle : windowHandles) {
            if(currentWindow.equals(windowHandle)){
                driver.switchTo().window(windowHandle);
            }
        }
        return textOfCode;
    }

    @Override
    public void SetCodeAndClickNext(String code) {
        wait=new WebDriverWait(driver,15);
        WebElement codeInputBox = driver.findElement(By.cssSelector("#codeBox"));
        wait.until(ExpectedConditions.visibilityOf(codeInputBox));
        codeInputBox.sendKeys(code);
        WebElement nextButton = driver.findElement(By.xpath("(//button[.='Next'])[2]"));
        wait.until(ExpectedConditions.visibilityOf(nextButton));
        nextButton.click();
    }

    /// Page 3
    @Override
    public void FillMaskedPasswordAndClickLogin(String password) {
        Map<Integer, String> map = new HashMap<>();
        WebElement iframe = driver.findElement(By.cssSelector(".pageLogin_Masked>iframe"));
        driver.switchTo().frame(iframe);

        //How to get password?
//        driver.switchTo().frame(0);
//        WebElement element = driver.findElement(By.xpath("//div[@class='col-sm']"));
//        String text = element.getText();
//
//        JavascriptExecutor js= (JavascriptExecutor) driver;
//        String elementText = (String) js.executeScript("return arguments[0].innerText;", element);
//
//        //  System.out.println("text = " + text);
//        System.out.println("elementText = " + elementText);
//
//        String[] s = text.split("\n");
//        String[] s1 = s[1].split(" ");
//        System.out.println("s1[2] = " + s1[2]);

        List<WebElement> passwordBoxInputList = driver.findElements(By.xpath("//input[@class='passwdField']"));
        for (int i = 0; i < password.length(); i++) {
            if (passwordBoxInputList.get(i).isEnabled()) {
                passwordBoxInputList.get(i).sendKeys(password.substring(i, i + 1));
            }
        }
        WebElement loginButton = driver.findElement(By.xpath("//button[.='Log in']"));
        loginButton.click();

    }

    @Override
    public String GetLoggedInText() {
        WebElement loggedInMessage = driver.findElement(By.id("loggedIn"));
        String text = loggedInMessage.getText();
        return text;
    }

}
