package main;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class test {
    public static void main(String[] args) throws InterruptedException {
        WebDriver driver = null;
        try {
            System.setProperty("webdriver.chrome.driver", "D:\\selenium\\chromedriver-win64\\chromedriver.exe");
            driver = new ChromeDriver();
            String inputData = "src/main/input_test/InputTestCase.txt";
            String outputData = "src/main/input_test/OutputTestCase.txt";
            String resultData = "src/main/result.txt";
            try (BufferedReader inputReader = new BufferedReader(new FileReader(inputData));
                 BufferedReader outputReader = new BufferedReader(new FileReader(outputData));
                 BufferedWriter resultWrite = new BufferedWriter(new FileWriter(resultData));
            ) {
                String lineInput;
                String lineOutput;
                int index = 0;
                while ((lineInput = inputReader.readLine()) != null &&
                        (lineOutput = outputReader.readLine()) != null
                ) {
                    index++;
                    String userName = "";
                    String password = "";
                    String[] rowInput = lineInput.split(",");
//               System.out.println(index+" Row:: "+lineInput+" length:: "+ rowInput.length);
                    if (rowInput.length == 1) {
                        userName = rowInput[0];
                    }
                    if (rowInput.length >= 2) {
                        userName = rowInput[0];
                        password = rowInput[1];
                    }
                    driver.get("https://qlht.ued.udn.vn/login");
                    WebElement inputUserName = driver.findElement(By.id("txt_Login_ten_dang_nhap"));
                    inputUserName.clear();
                    inputUserName.sendKeys(userName);
                    WebElement inputPassword = driver.findElement(By.id("pw_Login_mat_khau"));
                    inputPassword.clear();
                    inputPassword.sendKeys(password);
                    WebElement buttonLogin = driver.findElement(By.className("btn-login-form"));
                    Thread.sleep(1000);
                    buttonLogin.click();
                    String expectTitle = "Hệ thống quản lý đào tạo :: Quản lý đào tạo - TRƯỜNG ĐẠI HỌC SƯ PHẠM";
                    String resultTitleTest = driver.getTitle();
                    System.out.println(driver.getTitle());
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript("alert('" + lineOutput + "')");
                    Thread.sleep(1000);
                    Alert alert = driver.switchTo().alert();
                    alert.accept();
                    if (!expectTitle.equals(resultTitleTest)) {
                        resultWrite.write("Username:: "+validToUndefined(userName) + " - password:: " + validToUndefined(password) + "-> Failed!\n");
                        System.out.println("Login test failed!");
                    } else {
                        resultWrite.write("Username:: "+validToUndefined(userName) + " - password:: " + validToUndefined(password) + "-> Passed!\n");
                        Thread.sleep(500);
                        driver.get("https://qlht.ued.udn.vn/login");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error:: " + e.getMessage());
        } finally {
            if (driver != null) driver.quit();
        }
    }
    static String validToUndefined (String text){
        return text.isEmpty() ? "Undefined" : text;
    }
}