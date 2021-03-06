package twitter;

import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Encapsulates the connection to Twitter
 *
 * Terms to include in the returned tweets can be set with setFilterTerms
 *
 * Implements Observable - each received tweet is signalled to all observers
 */
public class LiveTwitterSource extends TwitterSource {
    private TwitterStream twitterStream;
    private StatusListener listener;

    public LiveTwitterSource() {
        initializeTwitterStream();
    }

    protected void sync() {
        FilterQuery filter = new FilterQuery();
        // https://stackoverflow.com/questions/21383345/using-multiple-threads-to-get-data-from-twitter-using-twitter4j
        String[] queriesArray = terms.toArray(new String[0]);
        filter.track(queriesArray);

        printSync();

        twitterStream.filter(filter);
    }

    private void printSync() {
        System.out.println("Syncing live Twitter stream with " + terms);
    }

    private void initializeListener() {
        listener = new StatusAdapter() {
            @Override
            public void onStatus(Status status) {
                // This method is called each time a tweet is delivered by the twitter API
                if (status.getPlace() != null) {
                    handleTweet(status);
                    System.out.println(status.getText());
                }
           }
        };
    }

    private void initializeTwitterStream() {
        twitterStream = new TwitterStreamFactory(ConfigurationController.getDefaultConfiguration()).getInstance();
        initializeListener();
        twitterStream.addListener(listener);
    }
}
