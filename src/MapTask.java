import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MapTask {
    String file_name;
    int offset;
    int size;

    public MapTask(String file_name, int offset, int size) {
        this.file_name = file_name;
        this.offset = offset;
        this.size = size;
    }

    public void compute_task() throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/" + file_name));
        BufferedReader mark = new BufferedReader(new FileReader("src/" + file_name));
        boolean start_skip = false;
        boolean end_complete = true;
        boolean empty_fragment = false;
        int c;

        if (offset != 0) {
            mark.skip(offset - 1);
            c = mark.read();
            if (Tema2.spacers.indexOf(c) == -1) {
                start_skip = true;
            }
        }
        mark.skip(size - 1);
        c = mark.read();
        int next = mark.read();
        if (Tema2.spacers.indexOf(c) == -1 && Tema2.spacers.indexOf(next) == -1 && next != -1) {
            end_complete = false;
        }
        mark.close();

        char[] buffer = new char[size];
        bufferedReader.skip(offset);
        bufferedReader.read(buffer, 0, size);

        String text = "";
        if (start_skip) {
            int skipped_chars = 0;
            while (Tema2.spacers.indexOf(buffer[skipped_chars]) == -1) {
                skipped_chars++;
                if (skipped_chars == size - 1) {
                    empty_fragment = true;
                    break;
                }
            }
            if (!empty_fragment) {
                text = String.valueOf(buffer, skipped_chars, size - skipped_chars);
            }
        } else {
            text = String.valueOf(buffer);
        }
        if (!end_complete && !empty_fragment) {
            while (true) {
                c = bufferedReader.read();
                if (c != -1 && Tema2.spacers.indexOf(c) == -1) {
                    text += (char) c;
                } else {
                    break;
                }
            }
        }
        bufferedReader.close();

        StringTokenizer st = new StringTokenizer(text, Tema2.spacers);
        ComputedMapTask computedMapTask = new ComputedMapTask(file_name);
        while (st.hasMoreTokens()) {
            String s = st.nextToken();
            int key = s.length();
            if (computedMapTask.map.containsKey(key)) {
                int current_value = computedMapTask.map.get(key);
                computedMapTask.map.replace(key, current_value + 1);
            } else {
                computedMapTask.map.put(key, 1);
            }
            if (computedMapTask.largest_words.size() == 0) {
                computedMapTask.largest_words.add(s);
            } else if (computedMapTask.largest_words.get(0).length() == key) {
                computedMapTask.largest_words.add(s);
            } else if (computedMapTask.largest_words.get(0).length() < key) {
                computedMapTask.largest_words = new ArrayList<>();
                computedMapTask.largest_words.add(s);
            }
        }
        Tema2.computedMapTasks.add(computedMapTask);
    }
}
