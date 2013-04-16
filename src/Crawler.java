/**
 * File: Crawler.java
 * Description: Contains the crawler class
 * User: imnotmartin
 * Date: 4/15/13
 * Editors:
 *   imnotmartin (mting005@ucr.edu)
 */

public class Crawler {
    public int ID;
    public int maxCrawlDepth;

    public Crawler(int depth){
        ID = 0;
        maxCrawlDepth = depth;
    }

    public int getCrawlDepth(){
        return maxCrawlDepth;
    }
}
