package cuit.epoch.pymjl.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/28 17:53
 **/
public class MainTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        future.complete(12);
        System.out.println(future.get());
        System.out.println("=======主线程结束=======");
    }
}
