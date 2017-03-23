import java.util.ArrayList;
import java.util.Scanner;

public class Run {

    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);
        System.out.println("Enter the values and press enter, confirm with '#': ");

        ArrayList<String> list = new ArrayList<>();

        while (true) {
            String key = reader.next();
            char x = key.charAt(0);
            int ascii = (int) x;
            if (ascii == 35) break;
            list.add(key);
// TODO     list.add(Integer.parseInt(key));
        }

        String[] a = list.toArray(new String[list.size()]);
//      call sorting algorithm
        Shellsort.sort(a);
        assert Helper.isSorted(a);
        Helper.print(a);
    }
}