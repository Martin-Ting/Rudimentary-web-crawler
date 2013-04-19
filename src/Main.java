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
        System.out.println("First, you want to crawl for robots.txt and parse that.");
        System.out.println("Now that you have crawl parameters, do this: (read comments)");
        /*  Use the Jsoup.connect(String url) method:

Document doc = Jsoup.connect("http://example.com/").get();
String title = doc.title();

Description

The connect(String url) method creates a new Connection, and get() fetches and parses a HTML file. If an error occurs whilst fetching the URL, it will throw an IOException, which you should handle appropriately.

The Connection interface is designed for method chaining to build specific requests:

Document doc = Jsoup.connect("http://example.com")
  .data("query", "Java")
  .userAgent("Mozilla")
  .cookie("auth", "token")
  .timeout(3000)
  .post();

This method only suports web URLs (http and https protocols); if you need to load from a file, use the parse(File in, String charsetName) method instead.
*/
                /*
                  String html = "<html><head><title>First parse</title></head>"
  + "<body><p>Parsed HTML into a doc.</p></body></html>";
Document doc = Jsoup.parse(html);
                */
        /*
                Use the DOM-like methods available after parsing HTML into a Document.

File input = new File("/tmp/input.html");
Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");

Element content = doc.getElementById("content");
Elements links = content.getElementsByTag("a");
for (Element link : links) {
  String linkHref = link.attr("href");
  String linkText = link.text();
}

Description

Elements provide a range of DOM-like methods to find elements, and extract and manipulate their data. The DOM getters are contextual: called on a parent Document they find matching elements under the document; called on a child element they find elements under that child. In this way you can winnow in on the data you want.
Finding elements

    getElementById(String id)
    getElementsByTag(String tag)
    getElementsByClass(String className)
    getElementsByAttribute(String key) (and related methods)
    Element siblings: siblingElements(), firstElementSibling(), lastElementSibling(); nextElementSibling(), previousElementSibling()
    Graph: parent(), children(), child(int index)

Element data

    attr(String key) to get and attr(String key, String value) to set attributes
    attributes() to get all attributes
    id(), className() and classNames()
    text() to get and text(String value) to set the text content
    html() to get and html(String value) to set the inner HTML content
    outerHtml() to get the outer HTML value
    data() to get data content (e.g. of script and style tags)
    tag() and tagName()

Manipulating HTML and text

    append(String html), prepend(String html)
    appendText(String text), prependText(String text)
    appendElement(String tagName), prependElement(String tagName)
    html(String value)

         */


    }
}