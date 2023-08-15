import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

import java.awt.*;
import java.io.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Dy {

    private static final Pattern PATTERN = Pattern.compile("\\d+");
    public static String sendDy(String dyId) {
        String result = "";

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(100));

            Page page = browser.newPage();
            page.navigate("https://www.douyin.com/search/" + dyId + "?aid=c8d732e9-ae01-429b-859c-315ead058fe8&publish_time=0&sort_type=0&source=search_history&type=general");

            Thread.sleep(5000);

            ElementHandle elementHandle = page.querySelector("#douyin-right-container > div.FtarROQM > div > div.HHwqaG_P > div:nth-child(1) > ul > li:nth-child(1) > div > div > div > div.AwIKR2fG > div > div > div.mSoL45jm > a");
            String link = elementHandle.getProperty("href").toString();
            System.out.println(link);

            String trimmedLink = link.substring(0, link.indexOf("?"));
            System.out.println(trimmedLink);

            Page page2 = browser.newPage();
            page2.navigate(trimmedLink);

            if (page2.title().equals("验证码中间页")) {
                System.out.println("遇到验证码！");
                // 验证码处理
                // 收费验证码 5美元1千条左右
            }

            page2.waitForLoadState(LoadState.NETWORKIDLE);
            Thread.sleep(15000);

            String content = (String) page2.evaluate("() => { return document.querySelector('.J6IbfgzH').textContent; }");
            result = "该抖音用户的作品数为：" + content;
            System.out.println(result);

            File file = new File("D:\\" + dyId + "的抖音作品数");
            if (file.exists()) {
                System.out.println("文件已存在");
                String filePath = "D:\\" + dyId + "的抖音作品数";
                try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                    String line;
                    line = reader.readLine();
                    Matcher matcher = PATTERN.matcher(line);
                    if (matcher.find()) {
                        String number = matcher.group();
                        if(!number.equals(content)){
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
                fw.write("抖音作品数为：" + content);
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}