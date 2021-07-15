package twitter;

import twitter4j.Status;
import util.ObjectReader;

/**
 * A Twitter source that plays back a recorded stream of tweets.
 *
 * It ignores the set of terms provided except it uses the first call to setFilterTerms
 * as a signal to begin playback of the recorded stream of tweets.
 *
 * Implements Observable - each tweet is signalled to all observers
 */
public class PlaybackTwitterSource extends TwitterSource {
    // The speedup to apply to the recorded stream of tweets; 2 means play at twice the rate
    // at which the tweets were recorded
    private final double speedup;
    private ObjectReader source;
    private boolean threadStarted = false;

    public PlaybackTwitterSource(double speedup) {
        this.speedup = speedup;
        source = new ObjectReader("data/TwitterCapture.jobj");

    }

    //removed the code from this method to a specific class for refactoring purposes
    private void startThread() {
        if (threadStarted) {
            return;
        }
        threadStarted = true;
        new TwitterPlaybackObjectReader(source, speedup) {

            @Override
            public void handleTweetOperation(Status status) {
                handleTweet(status);
            }
        }.start();





    }

    /**
     * The playback source merely starts the playback thread, it it hasn't been started already
     */
    protected void sync() {

        printSync();
        startThread();
    }

    private void printSync() {
        System.out.println("Starting thread with: " + terms);
    }


}
