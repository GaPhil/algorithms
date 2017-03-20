import java.util.Scanner;

public class Run {

    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);
        System.out.println("Enter a number: ");

        String[] a = null;
        for (int i = 0; i < a.length; i++) {
            a[i] = reader.next();
        }
        // call sorting algorithm
        assert Helper.isSorted(a);
        Helper.print(a);
    }
}