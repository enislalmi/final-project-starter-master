package twitter;
import twitter4j.Status;
import util.ObjectReader;



public abstract class TwitterObjectReaderThreads extends Thread {

    private ObjectReader source;
    private double speedup;
    private long playbackStartTime;
    private long recordStartTime;




    public TwitterObjectReaderThreads(ObjectReader source, double speedup) {
        this.source=source;
        this.speedup=speedup;
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
            if (recordStartTime == 0)
            {
                recordStartTime= (long) time;
            }

            Status status_twitter = (Status) status;
           long playbacktime = computeplaybacktime((Long)status);

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
        long difference_status = timeStatus - recordStartTime;
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
