import java.util.concurrent.ExecutorService;

public class ReduceWorker implements Runnable{
    ExecutorService executorService;
    ReduceTask task;

    public ReduceWorker (ExecutorService executorService, ReduceTask task) {
        this.executorService = executorService;
        this.task = task;
    }

    @Override
    public void run() {
        try {
            task.compute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
