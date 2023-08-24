public class ComputedReduceTask {
    String file_name;
    double rank = 0;
    int largest_word_size;
    int nr_largest_words;

    public ComputedReduceTask(String file_name, int largest_word_size, int nr_largest_words) {
        this.file_name = file_name;
        this.largest_word_size = largest_word_size;
        this.nr_largest_words = nr_largest_words;
    }
}
