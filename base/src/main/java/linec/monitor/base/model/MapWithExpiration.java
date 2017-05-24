package linec.monitor.base.model;


import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MapWithExpiration<K, V> {
    private ConcurrentHashMap<K, V> pool = new ConcurrentHashMap<>();
    private ConcurrentHashMap<K, Date> timestamp = new ConcurrentHashMap<>();
    private long expiration;

    public MapWithExpiration(long expiration) {
        this.expiration = expiration;
    }

    public void put(K key, V value){
        pool.put(key, value);
        renew(key);
    }

    public boolean contains(K key){
        return pool.keySet().contains(key);
    }

    public void remove(K key){
        pool.remove(key);
        timestamp.remove(key);
    }

    public V get(K key){
        return pool.get(key);
    }

    public void renew(K key){
        timestamp.put(key, new Date());
    }

    public List list(){
        return pool.values().stream().collect(Collectors.toList());
    }

    @PostConstruct
    public void start(){
        new Thread(() -> {
            while (true){
                try {
                    Date now = new Date();
                    for (K key : timestamp.keySet()){
                        Date date = timestamp.get(key);

                        if (now.getTime() - date.getTime() > expiration){
                            pool.remove(key);
                            timestamp.remove(key);
                        }
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
