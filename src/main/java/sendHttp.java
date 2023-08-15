import java.util.Scanner;
import java.util.concurrent.*;
import java.util.function.Supplier;

class SendHttp {
    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要查询的抖音id");
        String idDy = sc.next();
        System.out.println("请输入要查询的快手id");
        String idKs = sc.next();
        System.out.println("请输入要查询的微博id");
        String wbId = sc.next();

        int corePoolSize = 6;
        ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(corePoolSize);

        scheduler.scheduleAtFixedRate(() -> sendRequestAndPrintResult(idDy, () -> Dy.sendDy(idDy)), 5, 1800, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(() -> sendRequestAndPrintResult(idKs, () -> Ks.sendKs(idKs)), 5, 1800, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(() -> sendRequestAndPrintResult(wbId, () -> Wb.sendWb(wbId)), 5, 1800, TimeUnit.SECONDS);

        // 等待任务执行
        scheduler.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        // 关闭线程池
        scheduler.shutdown();
    }

    private static void sendRequestAndPrintResult(String id, Supplier<String> supplier) {
        String result = supplier.get();
        System.out.println(result); // 打印返回值
    }
}