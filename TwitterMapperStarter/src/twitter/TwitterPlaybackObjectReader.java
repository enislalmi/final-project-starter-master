package twitter;

import twitter4j.Status;
import util.ObjectReader;

public abstract class TwitterPlaybackObjectReader extends Thread {
    private ObjectReader source;
    private double speedup;
    private long playbackStartTime;
    private long recordStartTime;

    public TwitterPlaybackObjectReader(ObjectReader source, double speedup) {
        this.source = source;
        this.speedup = speedup;
        this.playbackStartTime = System.currentTimeMillis() + 1000;
    }

    public void run() {
        long now;
        while (true) {
            Object timeObject = source.readObject();
            Object statusObject = source.readObject();

            if (timeObject == null || statusObject == null) {
                break;
            }

            long statusTime = (Long) timeObject;
            if (recordStartTime == 0) {
                recordStartTime = statusTime;
            }

            Status status = (Status) statusObject;
            long playbackTime = computePlaybackTime(statusTime);
            while ((now = System.currentTimeMillis()) < playbackTime) {
                pause(playbackTime - now);
            }

            if (status.getPlace() != null) {
                handleTweetOperation(status);
            }
        }
    }

    private long computePlaybackTime(long statusTime) {
        long difference_Status = statusTime - recordStartTime;
        long difference_target = Math.round(difference_Status / speedup);
        return playbackStartTime + difference_target;
    }

    public abstract void handleTweetOperation(Status status);

    private void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}