package kovacsi0907.atlas.Network;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class TrackedNetworkReciever {
    private TrackedNetworkReciever(){}

    private static final Lock lock = new ReentrantLock();
    public static final HashMap<String, Integer> tracked = new HashMap<>();

    public static void track(Identifier channelId) {
        lock.lock();
        try{
            if(!tracked.containsKey(channelId.toString())){
                tracked.put(channelId.toString(), 1);
            }else{
                int value = tracked.get(channelId.toString());
                tracked.put(channelId.toString(), value+1);
            }
        }finally {
            lock.unlock();
        }
    }

    public static void receive(Identifier channelId){
        lock.lock();
        try{
            if(!tracked.containsKey(channelId.toString()))
                return;
            int val = tracked.get(channelId.toString());
            if(val>1)
                tracked.put(channelId.toString(), val-1);
        }finally {
            lock.unlock();
        }
    }

    public static void trackAndWait(Identifier channelId, int refreshRateMillis, int timeOutMillis){
        track(channelId);
        int current = getCount(channelId);
        int last = current;
        int tries = (int)Math.ceil((double)timeOutMillis/refreshRateMillis);
        do{
            try {
                Thread.sleep(refreshRateMillis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            last = current;
            current = getCount(channelId);
            tries--;
        }while(tries>0&&current>0&&current==last);
    }

    private static int getCount(Identifier channelId){
        lock.lock();
        String id = channelId.toString();
        try{
            if(!tracked.containsKey(id))
                return 0;
            if(tracked.get(id) <= 0){
                tracked.remove(id);
                return 0;
            }
            return tracked.get(id);
        }finally {
            lock.unlock();
        }
    }
}
