import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Tema2 {
    public static List<ComputedMapTask> computedMapTasks = Collections.synchronizedList(new ArrayList<>());
    public static ArrayList<ReduceTask> reduceTasks = new ArrayList<>();
    public static List<ComputedReduceTask> computedReduceTasks = Collections.synchronizedList(new ArrayList<>());
    public static int nr_workers;
    public static String spacers = ";:/?˜\\.,><'[]{}()!@#$%ˆ&-_+’=*”|\t\r\n ";
    public static ArrayList<String> files = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("Usage: Tema2 <workers> <in_file> <out_file>");
            return;
        }
        BufferedReader bufferedReader = new BufferedReader(new FileReader(args[1]));

        nr_workers = Integer.parseInt(args[0]);

        String line = bufferedReader.readLine();
        int split_size = Integer.parseInt(line);

        line= bufferedReader.readLine();
        int nr_files = Integer.parseInt(line);

        ExecutorService executorService = Executors.newFixedThreadPool(nr_workers);

        for (int i = 0; i < nr_files; i++) {
            line = bufferedReader.readLine();
            files.add(line);
            BufferedReader b = new BufferedReader(new FileReader("src/" + line));
            int offset = 0;
            int fragment_size;
            while ((fragment_size = (int) b.skip(split_size)) != 0) {
                executorService.submit(new MapWorker(executorService, new MapTask(line, offset, fragment_size)));
                offset += fragment_size;
            }
            b.close();
        }
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        for (String file_name : files) {
            ReduceTask task = new ReduceTask(file_name);
            for (ComputedMapTask computedMapTask : computedMapTasks) {
                String file = computedMapTask.file_name;
                if (file_name.compareTo(file) == 0) {
                    task.map_list.add(computedMapTask.map);
                    for (String word : computedMapTask.largest_words) {
                        if (task.largest_words.size() == 0) {
                            task.largest_words.add(word);
                        } else if (task.largest_words.get(0).length() == word.length()) {
                            task.largest_words.add(word);
                        } else if (task.largest_words.get(0).length() < word.length()) {
                            task.largest_words = new ArrayList<>();
                            task.largest_words.add(word);
                        }
                    }
                }
            }
            reduceTasks.add(task);
        }
        bufferedReader.close();
        ExecutorService executorService1 = Executors.newFixedThreadPool(nr_workers);
        for (ReduceTask reduceTask : reduceTasks) {
            executorService1.submit(new ReduceWorker(executorService1, reduceTask));
        }

        executorService1.shutdown();
        executorService1.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        computedReduceTasks.sort((o1, o2) -> {
            if (o2.rank > o1.rank) {
                return 1;
            } else if (o2.rank < o1.rank) {
                return -1;
            }
            return o1.file_name.compareTo(o2.file_name);
        });

        FileWriter writer = new FileWriter(args[2]);
        for (ComputedReduceTask task : computedReduceTasks) {
            String[] path = task.file_name.split("/");
            writer.write(path[path.length - 1] + "," + String.format(Locale.US, "%.2f", task.rank) + "," + task.largest_word_size + "," + task.nr_largest_words + "\n");
        }
        writer.close();
    }
}