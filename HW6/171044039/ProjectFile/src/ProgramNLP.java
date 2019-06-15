import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class ProgramNLP {

    @SuppressWarnings("unchecked cast")
    public static void main(String[] args) throws IOException {

        NLP nlp = new NLP();
        nlp.readDataset("dataset/");

        System.out.println("Query Input File>");
        String filename = new Scanner(System.in).nextLine();
        System.out.println("Query Results for " + filename + "\n");

        String queryFile = new String(Files.readAllBytes(Paths.get(filename)));
        String[] lines = queryFile.split("\\r?\\n");
        String[][] queries = new String[lines.length][];

        for (int i=0;i<lines.length;++i)
            queries[i] = lines[i].split("\\s+");

        for (String[] query: queries){
            if (query.length == 2) // bi-grams query.
                System.out.println(nlp.bigrams(query[1]));
            else // TFIDF query.
                System.out.printf("%.7f\n",nlp.tfIDF(query[1], query[2]));
            System.out.print("\n");
        }

    }
}
