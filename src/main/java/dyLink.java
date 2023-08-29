import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.io.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dyLink {
    private static final Pattern PATTERN = Pattern.compile("\\d+");

    public static void sendDyLink(String link) throws UnsupportedEncodingException, InterruptedException {
        System.setOut(new PrintStream(System.out, true, "UTF-8"));
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        String num = null;

        try {
            String url = link;
            driver.get(url);

            WebElement element = new WebDriverWait(driver, 20)
                    .until(ExpectedConditions.visibilityOfElementLocated(By.className("J6IbfgzH")));
            String content = element.getText();
            System.out.println("该抖音用户的作品数为：" + content);

            WebElement elementName = new WebDriverWait(driver, 20)
                    .until(ExpectedConditions.visibilityOfElementLocated(By.className("Nu66P_ba")));
            String contentName = elementName.getText();
            num = String.valueOf(content);

            File file = new File("D:\\" + contentName + "抖音作品数");
            if (file.exists()) {
                System.out.println("文件已存在");
                String filePath = "D:\\" + contentName + "抖音作品数";
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    line = reader.readLine();
                    Matcher matcher = PATTERN.matcher(line);
                    if (matcher.find()) {
                        String number = matcher.group();
                        if (!number.equals(content)) {
                            AtomicInteger numBeeps = new AtomicInteger(5);
                            int delay = 1;

                            ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
                            Runnable beepTask = () -> {
                                Toolkit.getDefaultToolkit().beep();
                                numBeeps.getAndDecrement();

                                if (numBeeps.get() <= 0) {
                                    executor.shutdown();
                                }
                            };
                            executor.scheduleAtFixedRate(beepTask, 0, delay, TimeUnit.SECONDS);
                        }
                        System.out.println("提取到的数字为：" + number);
                    } else {
                        System.out.println("未找到匹配的数字");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try (FileWriter fw = new FileWriter(file)) {
                fw.write("抖音作品数为：" + content);
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } finally {
            driver.quit();
        }
    }
}