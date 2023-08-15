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
                    }else if(file.exists()){
                        System.out.println("文件已存在");
                        String filePath = "D:\\" + secondLine + "的微博作品数";
                        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                            String line;
                            line = reader.readLine();
                            Matcher matcher1 = PATTERN.matcher(line);
                            if (matcher1.find()) {
                                String number1 = matcher1.group();
                                if(!number1.equals(number)){
                                    // 设置警报声音的次数
                                    AtomicInteger numBeeps = new AtomicInteger(5); // 设置警报声音的次数
                                    int delay = 1; // 设置每次警报声音间隔的延迟时间（单位：秒）
                                    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
                                    Runnable beepTask = () -> {
                                        Toolkit.getDefaultToolkit().beep();
                                        numBeeps.getAndDecrement();
                                        if (numBeeps.get() <= 0) {
                                            executor.shutdown(); // 达到指定次数后关闭执行器
                                        }
                                    };
                                    executor.scheduleAtFixedRate(beepTask, 0, delay, TimeUnit.SECONDS);
                                }
                                System.out.println("提取到的数字为：" + number1);
                            } else {
                                System.out.println("未找到匹配的数字");
                            }
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