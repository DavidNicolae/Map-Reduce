import java.util.concurrent.ExecutorService;

public class MapWorker implements Runnable{
    ExecutorService executorService;
    MapTask task;

    public MapWorker (ExecutorService executorService, MapTask task) {
        this.executorService = executorService;
        this.task = task;
    }

    @Override
    public void run() {
        try {
            task.compute_task();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
