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
            JavascriptExecutor js = (JavascriptExecutor) driver;
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
                    String textAlert = "";
                    if (!expectTitle.equals(resultTitleTest)) {
                        resultWrite.write("Username:: "
                                +validToUndefined(userName) + " - password:: "
                                + validToUndefined(password) + " -> Test case Failed!\n");
                        System.out.println("Login test failed!");
                        WebElement errorContainer = driver.findElement(By.className("msg_error"));
                        WebElement errorMessage = errorContainer.findElement(By.xpath(".//li"));
                        textAlert = errorMessage.getText().equals(lineOutput) ? errorMessage.getText() : lineOutput;
                    } else {
                        textAlert =  lineOutput;
                        resultWrite.write("Username:: "
                                +validToUndefined(userName)
                                + " - password:: " + validToUndefined(password)
                                + " -> Test case Passed!\n");
                        Thread.sleep(500);
                    }
                    js.executeScript("alert('" + textAlert + "')");
                    Thread.sleep(1000);
                    Alert alert = driver.switchTo().alert();
                    alert.accept();
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