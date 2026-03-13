import java.util.*;
public class LambdaExample{
    public static void main(String[] args) {
        // Create a ArrayList
        List<String> list=new ArrayList<String>();
        // Add values to the list
        list.add("Rick");
        list.add("Negan");
        list.add("Daryl");
        list.add("Glenn");
        list.add("Carl");
        
        // Loop through ArrayList
        list.forEach( 
            // lambda expression 
            (names)->System.out.println(names)
        );
    }
}