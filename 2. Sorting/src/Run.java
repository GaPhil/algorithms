import java.util.ArrayList;
import java.util.Scanner;

public class Run {

    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);
        System.out.println("Enter the values and press enter, confirm with '#': ");

        ArrayList<Comparable> list = new ArrayList<>();

        while (true) {
            String key = reader.next();
            Comparable value;
            try {
//                is an integer
                value = Integer.parseInt(key);
            } catch (NumberFormatException e) {
//                not an integer
                value = key;
            }

            char x = key.charAt(0);
            int ascii = (int) x;
            if (ascii == 35) break;
            list.add(value);
        }

        try {
            Comparable[] a = list.toArray(new Comparable[list.size()]);
//            call sorting algorithm
            QuickSort.sort(a);
            assert Helper.isSorted(a);
            Helper.print(a);
        } catch (ClassCastException e) {
            System.out.println("ERROR: the provided values must be of the same type (strings or numbers)");
        }
    }
}