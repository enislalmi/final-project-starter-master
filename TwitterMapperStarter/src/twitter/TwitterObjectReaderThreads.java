package twitter;
import twitter4j.Status;
import util.ObjectSource;


public abstract class TwitterObjectReaderThreads extends Thread {

    private ObjectSource source;
    private double speedup;
    private long playbackStartTime;
    private long recordStartTIme;


    public TwitterObjectReaderThreads(Runnable target, ObjectSource source, double speedup, long playbackStartTime, long recordStartTIme) {
        super(target);
        this.source = source;
        this.speedup = speedup;
        this.playbackStartTime = playbackStartTime;
        this.recordStartTIme = recordStartTIme;
    }

    public TwitterObjectReaderThreads(ObjectSource source, double speedup) {
    }

    public void run()
    {
        long time_now;
        while(true)
        {
            Object time = source.readObject();
            Object status = source.readObject();

            if (time==null || status == null)
            {
                break;

            }

            long statusTime = (long) time;
            if (recordStartTIme == 0)
            {
                recordStartTIme= (long) time;
            }

            Status status_twitter = (Status) status;
           long playbacktime = computeplaybacktime((Long) status);

           while((time_now = System.currentTimeMillis())<playbacktime)
           {
               pause(playbacktime-time_now);
           }
           if(( status_twitter).getPlace()!=null){
               handleOperation(status_twitter);
           }
        }
    }
    private long computeplaybacktime(long timeStatus)
    {
        long difference_status = timeStatus - recordStartTIme;
        long diference = Math.round(difference_status/speedup);
        return playbackStartTime+diference;
    }

    public abstract void handleOperation(Status st);

    private void pause(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)

        {
        e.printStackTrace();
        }
    }

}
