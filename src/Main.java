/**
 * File:
 * Description: Test code
 * User: imnotmartin
 * Date: 4/15/13
 * Editors:
 *   imnotmartin (mting005@ucr.edu)
 */

import org.jsoup.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import javax.xml.*;
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
class Pair<T1, T2>{
    private T1 left;
    private T2 right;
    public Pair(T1 inLeft, T2 inRight){
        left = inLeft;
        right = inRight;
    }
    public T1 getLeft(){
        return left;
    }
    public T2 getRight(){
        return right;
    }
}
public class Main {
    Crawler c;

    public void __init_(int num_crawlers){

    }
    public static void main(String[] args){

        System.out.println("Hello, World!");
        System.out.println("Hopefully, now, you have finished with setting up java JDK, IntelliJ IDE, and Git setup\n");
        System.out.println("First, you want to crawl for robots.txt and parse that.");
        System.out.println("Now that you have crawl parameters, do this: (read comments)");
        Document doc;
        Vector<Pair<String, String> > frontier;
        frontier = new Vector<Pair<String, String> >(50);
        try{
            doc = Jsoup.connect("http://www.cs.ucr.edu/").get();
            String docHTML= doc.html();
            System.out.println(docHTML);
            doc = Jsoup.parse(docHTML);
            Element content = doc.getElementById("content");
            Elements links = content.getElementsByTag("a");
            for (Element link : links) {
                String linkHref = link.attr("href");                           //link
                String linkText = link.text();                                 //hyperlinked text
                System.out.println("Printing element: ");
                System.out.println(linkHref + "\n" + linkText);
                frontier.add(new Pair<String, String>(linkHref, linkText));
            }

        }catch(java.io.IOException err){
            System.out.println("Ooops!  Jsoup can not connect to host.\n\t" + err.getMessage());
        }


    }
}