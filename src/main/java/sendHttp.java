import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.function.Supplier;

class SendHttp {

    public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException {
        System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        System.out.println("请输入需要按照id查找还是按照链接查找,按id输入1，按链接输入2");
        Scanner sc = new Scanner(System.in);
        String k = sc.nextLine();
        if (k.equals("1")) {
            idMethod(sc);
        } else if (k.equals("2")) {
            linkMethod(sc);
        }
    }

    private static void idMethod(Scanner sc) throws InterruptedException {
        Map<String, String> idMap = new HashMap<>();

        System.out.println("请输入要查询的id和平台，格式为：平台:id（输入完成后输入exit结束）");
        String input;
        while (!(input = sc.nextLine().trim()).equalsIgnoreCase("exit")) {
            String formattedInput = input.replace("：", ":"); // 将中文冒号替换为英文冒号
            String[] parts = formattedInput.split(":", 2);
            if (parts.length != 2) {
                System.out.println("输入格式无效");
                continue;
            }

            final String platform = parts[0].trim();
            final String id = parts[1].trim();

            switch (platform) {
                case "抖音":
                case "快手":
                case "微博":
                    idMap.put(platform, id);
                    break;
                default:
                    System.out.println("无效的平台");
                    continue;
            }
        }

        int corePoolSize = idMap.size();
        ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(corePoolSize);

        for (Map.Entry<String, String> entry : idMap.entrySet()) {
            final String platform = entry.getKey();
            final String id = entry.getValue();
            Supplier<String> supplier;

            switch (platform) {
                case "抖音":
                    supplier = () -> Dy.sendDy(id);
                    break;
                case "快手":
                    supplier = () -> Ks.sendKs(id);
                    break;
                case "微博":
                    supplier = () -> Wb.sendWb(id);
                    break;
                default:
                    continue;
            }

            Supplier<String> finalSupplier = supplier;
            scheduler.scheduleAtFixedRate(() -> sendRequestAndPrintResult(id, finalSupplier), 5, 1800, TimeUnit.SECONDS);
        }

        // 等待任务执行
        scheduler.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        // 关闭线程池
        scheduler.shutdown();
    }

    private static void sendRequestAndPrintResult(String id, Supplier<String> supplier) {
        String result = supplier.get();
        System.out.println(result); // 打印返回值
    }

    private static void linkMethod(Scanner sc) throws InterruptedException {
        Map<String, String> linkMap = new HashMap<>();

        System.out.println("请输入要查询的链接和平台，格式为：平台:链接（输入完成后输入exit结束）");
        String input;
        while (!(input = sc.nextLine().trim()).equalsIgnoreCase("exit")) {
            String formattedInput = input.replace("：", ":"); // 将中文冒号替换为英文冒号
            String[] parts = formattedInput.split(":", 2);
            if (parts.length != 2) {
                System.out.println("输入格式无效");
                continue;
            }

            final String platform = parts[0].trim();
            final String link = parts[1].trim();

            switch (platform) {
                case "抖音":
                case "快手":
                case "微博":
                    linkMap.put(platform, link);
                    break;
                default:
                    System.out.println("无效的平台");
                    continue;
            }
        }

        int corePoolSize = linkMap.size();
        ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(corePoolSize);

        for (Map.Entry<String, String> entry : linkMap.entrySet()) {
            final String platform = entry.getKey();
            final String link = entry.getValue();
            Supplier<String> supplier;

            switch (platform) {
                case "抖音":
                    supplier = () -> {
                        try {
                            dyLink.sendDyLink(link);
                        } catch (UnsupportedEncodingException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        return "ok";
                    };
                    break;
                case "快手":
                    supplier = () -> {
                        try {
                            ksLink.sendKsLink(link);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return "ok";
                    };
                    break;
                case "微博":
                    supplier = () -> {
                        try {
                            wbLink.sendWbLink(link);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return "ok";
                    };
                    break;
                default:
                    continue;
            }

            Supplier<String> finalSupplier = supplier;
            scheduler.scheduleAtFixedRate(() -> sendRequestAndPrintResult(link, finalSupplier), 5, 1800, TimeUnit.SECONDS);
        }

        // 等待任务执行
        scheduler.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        // 关闭线程池
        scheduler.shutdown();
    }
}