package main.java;
/**
 * File:
 * Description: Web Crawler
 * User: dnguyy078, mting005
 * Date: 4/15/13
 * Editors:
 *   Martin Ting - mting005@ucr.edu
 *   Dennis Nguyen - dnguy078@ucr.edu
 */
import java.io.*;

import org.apache.commons.io.FileUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.*;

import java.net.*;
import java.net.SocketTimeoutException;
import java.net.MalformedURLException;

import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main{
    private static AtomicBoolean running = new AtomicBoolean();
    private static AtomicBoolean okayToReset = new AtomicBoolean(true);
    private static Frontier frontier;
    private static AtomicBoolean maxDepthReached = new AtomicBoolean(false); 
    public class Frontier{
        private ConcurrentHashMap<String, Integer> visitedURL;
        private Vector<ConcurrentLinkedQueue<String> > masterQueue = new Vector<ConcurrentLinkedQueue<String> >(2);

        public int numFiles = 0;
        private int MAXDEPTH = 0;
        private int MAXFILES = 0;
        private int numAdds = 0;
        private int maxAtCurr = 0;
        private int curr = 0;
        public int currDepth = 0;
        public Frontier(int depth, int files, Vector<String> newURL){
            MAXFILES = files;
            MAXDEPTH = depth;
            masterQueue.add(0, new ConcurrentLinkedQueue<String>() );
            masterQueue.add(1, new ConcurrentLinkedQueue<String>() );

            visitedURL = new ConcurrentHashMap<String, Integer>();

            for( String st : newURL ){
                st = cleanURL(st);
                if(st.equals("")) continue;
                masterQueue.get(0).add(st);
                visitedURL.put(st, 1);
            }
            maxAtCurr = masterQueue.get(0).size();
        }

        String cleanURL(String inURL){
            //System.out.println("cURL called on " + inURL);
            URL url;
            String ret = "";
            try{
                //System.out.println("Instanciating URL from " + inURL);
                url = new URL(inURL);
            }catch(MalformedURLException err)
            {
                System.out.println("[Error]: Frontier receieved malformed URL " + inURL) ;
                return ret;
            }
            if(inURL.contains("mailto:") || inURL.contains("&") || inURL.contains("@")){

                return ret;
            }
            if(url.getProtocol().contains("http") && !url.getProtocol().contains("https")){
                if(url.getFile().length() == 1){
            		return url.getProtocol() + "://" + url.getHost() + url.getFile();
            	}
            		

                if(url.getFile().contains(".html") || url.getFile().contains(".htm")){
                    //System.out.println("Cleaned URL: " + ret + "\n\t" + inURL);
                    ret = url.getProtocol() + "://" + url.getHost() + url.getFile();
                }
            }
            if(ret.contains("#")){
                ret = ret.substring(0, ret.indexOf("#")-1);
            }
            return ret;
        }

        /*
            function getNext() returns next URL to work on
            Doesn't need to worry about repeats, Add does

        */
        String getNext(int ID) throws InterruptedException{
            //  System.out.println(ID + " called getNext.");
            String top;
              // System.out.println(ID + " status Report: \nnumFiles = " + numFiles + "\nMAXDEPTH = " + MAXDEPTH +
                //                   "\n numAdds: " + numAdds + "\nmaxAtCurr: " + maxAtCurr + "\ncurr: " + curr + ", " + currDepth);
            if(currDepth >= MAXDEPTH || numFiles >= MAXFILES)
            {	
            	maxDepthReached.set(true); 
            	return "";
            }
            if(masterQueue.get(curr).isEmpty()){
                //if atomic bool is not set
                //set bool to true

                if(okayToReset.compareAndSet(true, false)){
                    if(numAdds == maxAtCurr){


                        //flip curr
                        if( curr == 0 ) curr = 1;
                        else            curr = 0;
                        //reset maxAtCurr
                        maxAtCurr = masterQueue.get(curr).size();
                        //reset numAdds

                        currDepth++;
                        okayToReset.compareAndSet(false, true);
                        if(okayToReset.get() == false)
                        {

                            okayToReset.set(true);
                        }
                    }
                    else{


                        if(okayToReset.get() == false)
                        {

                            okayToReset.set(true);
                        }
                        return "";

                    }
                }
                else
                {
                    return "";
                }
                //set bool to false.
            }

            top = masterQueue.get(curr).poll();
            //System.out.println("Broadcasting " + top + " to " + ID);

            if( top == null) return "";

            numFiles++;
            return top;

        }

        void Add(Vector<String> newURL){
            int numAdded  = 0;
            for(String st : newURL){
                //////////////////CLEAN ST HERE
                st = cleanURL(st);
                //System.out.println("Attempting to add: " + st);
                if(st.equals("")) continue;
                if(!visitedURL.contains(st)){
                    int other;
                    if( curr == 0 ) other = 1;
                    else            other = 0;
                    masterQueue.get(other).add(st);
                    numAdded++;
                    //System.out.println(st + " was added at: ") +
                            //"//" + ((curr == 0) ? 1 : 0));
                }
                else{
                    visitedURL.replace(st, visitedURL.get(st), visitedURL.get(st) + 1);
                }

            }

            ++numAdds;
            System.out.println("Frontier added " + numAdded + " links to the queue.");
        }
    }

    static int cID = 0;
    public class Crawler implements Runnable{
        private Vector<String> newURL;
        int myID = -1;
        public void run(){
            myID = cID++;
            newURL = new Vector<String>();

            while(running.get()){
                String startURL = null;
                try {
                    startURL = frontier.getNext(myID);
                    if(startURL.equals("")) continue;
                } catch (InterruptedException e) {
                    System.out.println("[Error]: Frontier.run() - getting next URL" + "\n\t" + e.getMessage());
                    e.printStackTrace();
                }
               // System.out.println("\t" + ( (RobotExclusionUtil.robotsShouldFollow(startURL) ? "TRUE" : "FALSE") ));
                //recursiveCrawl(startURL, 0);
                if (RobotExclusionUtil.robotsShouldFollow(startURL)){
                    System.out.println("Thread " + myID + " is crawling " + startURL);
                    crawl(startURL);
                }
                //System.out.println("Thread " + myID + " adding links: " + newURL.size());

                frontier.Add(newURL);
                newURL.removeAllElements();
                //frontier.printHashMap();
            }
        }

        public void downloadFile(String htmlContent, String url) throws IOException{
           try{
        	File htmlFile = new File(url);

        	FileUtils.writeStringToFile(htmlFile, htmlContent);


            System.out.println("Crawler " + myID+ " Downloaded " + url);
           }
           catch (IOException err)
           {
               System.out.println("Thread " + myID + " is naively ignoring previously crawled page." );
           }
        }
        public void crawl(String strURL){
            if(strURL.length() == 0) return;
            //System.out.println("Thread " + myID + " is crawling " + strURL);
            Vector<String> newLinks = new Vector<String>();
            newLinks.ensureCapacity(100);
            try{
                //System.out.println("Thread " + myID + " Connecting to " + strURL);
                Connection connection = Jsoup.connect(strURL);

                Document doc = connection.get();

                downloadFile(doc.html(), strURL);

                //System.out.println("Connected.");
                Elements links = doc.select("a[href]");
                for(Element link : links){
                    String linkHref = link.attr("abs:href");
                    if(!newURL.contains(linkHref))
                        newLinks.add(linkHref);
                }
            }catch(UnsupportedMimeTypeException err){
                System.out.println("[Error]: HTTP Connection failed. Unsupported mime type.");
            }catch(SocketTimeoutException err){
                System.out.println("[Error]: Could not connect to " + strURL + ": connection timed out.");
            }catch(HttpStatusException err){
                System.out.println("[Error]: HTTP error fetchung url " + strURL);
            }catch(UnknownHostException err){
                System.out.println("[Error]: Cannot connect to unknown host.");
            }catch(IOException err){
                System.out.println("[Error]: Jsoup can not connect to " + strURL); //"\n\t" + err.getMessage());
            }

            newURL.addAll(newLinks);
        }

    }

    public static void main(String[] args){
        System.out.println("Initializing Crawler.");
        String inputFilePath = args[0];
        int MAXDEPTH = Integer.parseInt(args[1]);
        int MAXFILES = Integer.parseInt(args[2]);
        System.out.println("Input file: " + inputFilePath);
        System.out.println("Max crawl depth: " + MAXDEPTH);
        System.out.println("Max files attempt allowed: " + MAXFILES);
        Vector<String> crawlSet = new Vector<String>();

        File inputFile = new File(inputFilePath);
        BufferedReader br;

        try{

            br = new BufferedReader(new FileReader(inputFile));

            String line;
            while((line = br.readLine()) != null){
                crawlSet.add(line);
                System.out.println("Added " + line + " to seeds.");
            }

            br.close();
        } catch(FileNotFoundException err){
            System.out.println("[ERROR]: could not read seed file.");
        } catch(IOException err){
            System.out.println("[ERROR]: IO Exception - could not read from file." );
        }

        running.set(true);
        maxDepthReached.set(false); 
        Main redundant = new Main();


        crawlSet.add("http://www.ucr.edu/");
        crawlSet.add("http://www.cs.ucr.edu/~vagelis/classes/CS172/");

        frontier = redundant.new Frontier(MAXDEPTH, MAXFILES, crawlSet);
        int MAXTHREADS = 0;
        if(crawlSet.size() <= 20 )
        {
            MAXTHREADS = crawlSet.size();
        }
        else
        {
            MAXTHREADS = 20;
        }
        for(int i = 0; i < MAXTHREADS; ++i){
            System.out.println("Spawning thread " + i);
            new Thread(redundant.new Crawler()).start();
        }

        while(maxDepthReached.get() == false) {}
        System.out.println("Exiting"); 
        System.exit(1); 
        running.set(false);
    }

}

