import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wb {
    private static final Pattern PATTERN = Pattern.compile("\\d+");

    public static String sendWb(String wbId) {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        String number = null;

        try {
            String url = "https://weibo.com/" + wbId + "?refer_flag=1001030103_";
            driver.get(url);

            Thread.sleep(10000);

            WebElement element = new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.visibilityOfElementLocated(By.className("container")));

            String content = element.getText();
            String[] lines = content.split("\n");

            if (lines.length >= 2) {
                String firstLine = lines[0];
                Matcher matcher = PATTERN.matcher(firstLine);

                if (matcher.find()) {
                    number = matcher.group();
                    System.out.println("该微博用户的作品数为：" + number);

                    String secondLine = lines[1];
                    File file = new File("D:\\" + secondLine + "的微博作品数");

                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    try (FileWriter fw = new FileWriter(file)) {
                        fw.write("微博作品数为" + number);
                        fw.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return number;
    }
}