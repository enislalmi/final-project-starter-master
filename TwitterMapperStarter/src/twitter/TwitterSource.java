package twitter;

import twitter4j.Status;
import util.ImageCache;

import java.util.*;

//task 1 making twittersource extend obeservable
public abstract class TwitterSource extends Observable {
    protected boolean doLogging = true;
    // The set of terms to look for in the stream of tweets
    protected Set<String> terms = new HashSet<>();

    // Called each time a new set of filter terms has been established
    abstract protected void sync();

    protected void log(Status s) {
        ImageCache.getInstance().loadImage(s.getUser().getProfileImageURL());
        if (doLogging) {
            printStatus(s); //implementing printStatus() for better readibility
        }

    }

    private void printStatus(Status s) {
         System.out.println(s.getUser().getName() + ": " + s.getText());
    }

    public void setFilterTerms(Collection<String> newterms) {
        terms.clear();
        terms.addAll(newterms);
        sync();
    }


    // This method is called each time a tweet is delivered to the application.
    // TODO: Each active query should be informed about each incoming tweet so that
    //       it can determine whether the tweet should be displayed
    protected void handleTweet(Status s) {
            setChanged();
            notifyObservers(s);
    }
}
