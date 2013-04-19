/**
 * File:
 * Description: Test code
 * User: imnotmartin
 * Date: 4/15/13
 * Editors:
 *   imnotmartin (mting005@ucr.edu)
 */
import java.util.Vector;
//import java.util.concurrent.*;

class Frontier {
    Vector<String> URLcollection;
    int frontierSize, current;
    public Frontier(Vector<String> uCol)
    {
        frontierSize = uCol.size();
        current = 0;
        URLcollection = new Vector<String>(frontierSize);
        for(String str : uCol) {
            URLcollection.add(str);
        }
    }
    public String getNext(){
        if(current < frontierSize)
            return URLcollection.get(current++);
        else
            return new String();
    }

    public void addAll(Vector<String> newFrontier){
        URLcollection.ensureCapacity(URLcollection.size() + newFrontier.size());
        for(String str : newFrontier){
            URLcollection.add(str);
            ++frontierSize;
        }
        System.out.println("Added " + newFrontier.size() + " new items to Frontier!");
    }
}
class Crawler {
    private int myID;

    public Crawler(int ID, Frontier f){
        myID = ID;
    }

    public void parse(){
        //Jsoup parsing done here
    }
    public void crawl(){
        //String workURL = /*Frontier object*/.getNext();

    }
}
//====================================================================================================================//

public class Main {


    public static void main(String[] args){
        System.out.println("Hello, World!");
        System.out.println("Hopefully, now, you have finished with setting up java JDK, IntelliJ IDE, and Git setup\n");

    }
}