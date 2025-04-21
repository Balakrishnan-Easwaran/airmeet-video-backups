package com.airmeet.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;


import java.io.Console;

public class LoginPage {
    private WebDriver driver;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
    private By emailField = By.id("email");
    private By enterEmail = By.xpath("//button[@id='Continue-with-email']");
    private By passwordField = By.id("password");
    private By loginButton = By.xpath("//button[@class='sc-fzoant gtEONy']//p[@class='sc-fznWqX irWQxZ sc-fzowVh gawqHI'][normalize-space()='Login']");
    private By cookiePopupButton = By.id("onetrust-accept-btn-handler");

    public void login(String email, String password) {
        System.out.println("inside login function");
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("clicking popup");
        driver.findElement(cookiePopupButton).click();
        System.out.println("entering email");
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(enterEmail).click();
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }


    public boolean isLoginPage() {
        return driver.getCurrentUrl().contains("login");
    }
}
