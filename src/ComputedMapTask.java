import java.util.ArrayList;
import java.util.HashMap;

public class ComputedMapTask {
    String file_name;
    HashMap<Integer, Integer> map;
    ArrayList<String> largest_words;

    public ComputedMapTask(String file_name) {
        this.file_name = file_name;
        map = new HashMap<>();
        largest_words = new ArrayList<>();
    }
}
