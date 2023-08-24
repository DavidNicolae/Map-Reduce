import java.util.ArrayList;
import java.util.HashMap;

public class ReduceTask {
    String file_name;
    ArrayList<HashMap<Integer, Integer>> map_list;
    ArrayList<String> largest_words;

    public ReduceTask(String file_name) {
        this.file_name = file_name;
        map_list = new ArrayList<>();
        largest_words = new ArrayList<>();
    }

    public static int fib(int n)
    {
        if (n <= 1)
            return n;
        return fib(n - 1) + fib(n - 2);
    }

    public void compute() {
        ComputedReduceTask task = new ComputedReduceTask(file_name, largest_words.get(0).length(), largest_words.size());
        long total_words_nr = 0;
        for (HashMap<Integer, Integer> map : map_list) {
            System.out.println(map);
            for (int key : map.keySet()) {
                task.rank += (long) fib(key + 1) * map.get(key);
                total_words_nr += map.get(key);
            }
        }
        task.rank /= total_words_nr;
        Tema2.computedReduceTasks.add(task);
    }
}
