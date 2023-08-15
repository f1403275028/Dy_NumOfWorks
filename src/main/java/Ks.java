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

public class Ks {
    private static final Pattern PATTERN = Pattern.compile("\\d+");
    public static String sendKs(String id_Ks) {
        // 设置 ChromeDriver 路径
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver-win64\\chromedriver.exe");

        // 创建 ChromeDriver 实例
        WebDriver driver = new ChromeDriver();

        String Num = null;
        try {
            // 导航到网页
            String url = "https://www.kuaishou.com/profile/" + id_Ks;
            driver.get(url);

            Thread.sleep(30000);

            // 使用显式等待，等待动态加载内容出现
            WebElement element = new WebDriverWait(driver, 60)
                    .until(ExpectedConditions.visibilityOfElementLocated(By.className("user-photo-list")));

            // 获取动态加载内容
            String content = element.getText();
            int count = countOccurrences(content, "喜欢");
            if (content.equals("已经到底了，没有更多内容了")){
                count =0;
            }
            System.out.println("该快手用户的作品数为：" + count);
            Num = String.valueOf(count);

            File file = new File("D:\\" + id_Ks + "的快手作品数");
            if (file.exists()) {
                System.out.println("文件已存在");
                String filePath = "D:\\" + id_Ks + "的快手作品数";
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    line = reader.readLine();
                    Matcher matcher = PATTERN.matcher(line);
                    if (matcher.find()) {
                        String number = matcher.group();
                        if(!number.equals(Num)){
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
                fw.write("快手作品数为" + Num);
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭浏览器
            driver.quit();
        }
        return Num;
    }

    private static int countOccurrences(String content, String searchStr) {
        int count = 0;
        int index = 0;

        while ((index = content.indexOf(searchStr, index)) != -1) {
            count++;
            index += searchStr.length();
        }

        return count;
    }
}