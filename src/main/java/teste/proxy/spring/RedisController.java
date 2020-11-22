package teste.proxy.spring;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

@RestController @RequestMapping("/redis")
public class RedisController {


    JedisPool jPool;
    JedisCluster jedisCluster;
    private boolean clusterMode = false;

    public RedisController() throws IOException {
        Properties props = new Properties();
        InputStream is = RedisController.class.getResourceAsStream("/appConfig.properties");
        props.load(is);

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        if(Boolean.parseBoolean(props.getProperty("redis.auth"))){
            jPool = new JedisPool(poolConfig, props.getProperty("redis.host"),
                    Integer.parseInt(props.getProperty("redis.port")),0, props.getProperty("redis.auth.usr"),
                    props.getProperty("redis.auth.pwd"), 0, null, true);
        }
        else{
            jPool = new JedisPool(poolConfig, props.getProperty("redis.host"),
                    Integer.parseInt(props.getProperty("redis.port")), 5, null,
                    null, 0, null, true);
        }

        // WITH TLS
        if(Boolean.parseBoolean(props.getProperty("redis.tls"))) {

            System.setProperty("javax.net.ssl.keyStore", props.getProperty("redis.tls.keystore"));
            System.setProperty("javax.net.ssl.keyStorePassword", props.getProperty("redis.tls.keystore.password"));
            System.setProperty("javax.net.ssl.trustStore", props.getProperty("redis.tls.truststore"));
            System.setProperty("javax.net.ssl.trustStorePassword", props.getProperty("redis.tls.truststore.password"));

            // cluster=true
            if(Boolean.parseBoolean(props.getProperty("redis.clustermode"))){
                clusterMode = true;
                HostAndPort hp = new HostAndPort(props.getProperty("redis.cluster.host"),
                        Integer.parseInt(props.getProperty("redis.cluster.port")));

                GenericObjectPoolConfig<Object> genObject=new GenericObjectPoolConfig<Object>();

                jedisCluster = new JedisCluster(hp, 5000, 1000, 5,
                        props.getProperty("redis.auth.pwd"), props.getProperty("redis.auth.usr"), genObject, true);
            }
            else{ // no cluster
                Jedis jedis = jPool.getResource();
                //jedis = new Jedis(props.getProperty("redis.host"), Integer.parseInt(props.getProperty("redis.port")), true);
                //test jedispool
                jedis.connect();
                jedis.close();
            }
        }
        else{ // WITHOUT TLS
            /*if(Boolean.parseBoolean(props.getProperty("redis.auth"))){
                jedis.auth(props.getProperty("redis.auth.usr"), props.getProperty("redis.auth.pwd"));
            }
            else{
                // Do nothing
            }*/
            if(Boolean.parseBoolean(props.getProperty("redis.clustermode"))){
                clusterMode = true;
                HostAndPort hp = new HostAndPort(props.getProperty("redis.cluster.host"), Integer.parseInt(props.getProperty("redis.cluster.port")));
                jedisCluster = new JedisCluster(hp);
            }
            else {
                //test jedispool
                Jedis jedis = jPool.getResource();
                jedis.connect();
                jedis.close();
            }
        }
        //System.out.println(jedis.ping());
    }


    @GetMapping("/{key}")
    public String getKey(@PathVariable("key") String key) throws Exception{
        Jedis jedis = jPool.getResource();
        try{
            if(!clusterMode) {
                String result = jedis.get(key);
                System.out.println("getting result: " + result);
                if(result==null)
                    System.out.println("No value found for key + "+key);
                return result;
            }
            else
                return jedisCluster.get(key);
        } catch (Exception e) {
            throw e;
        }
        finally {
            jedis.close();
        }
    }

    @PostMapping("/{key}")
    public String setValueToKey(@PathVariable("key") String key, @RequestParam (required = true) String value) throws Exception{
        Jedis jedis = jPool.getResource();
        try {
            if(!clusterMode) {
                System.out.println("posting value: " + value + " to key: " + key);
                return jedis.set(key, value);
            }
            else
                return jedisCluster.set(key, value);
        } catch (Exception e) {
            throw e;
        }
        finally {
            jedis.close();
        }
    }

    @PutMapping("/{key}")
    public String updateKey(@PathVariable("key") String key, @RequestParam String value) throws Exception{
        Jedis jedis = jPool.getResource();
        try{
            if(!clusterMode) {
                if (jedis.exists(key))
                    return jedis.set(key, value);
            }
            else{
                if(jedis.exists(key))
                    return jedisCluster.set(key, value);
            }
        }
        catch (Exception e){
            throw e;
        }finally {
            jedis.close();
        }
        return "";
    }

    @DeleteMapping("/{key}")
    public String deleteKey(@PathVariable("key") String key) throws Exception{
        Jedis jedis = jPool.getResource();
        try {
            if(!clusterMode) {
                if (jedis.get(key) != null)
                    return jedis.del(key).toString();

            }
            else {
                if (jedis.get(key) != null)
                    return jedisCluster.del(key).toString();

            }
        }
        catch(Exception e){
            throw e;
        }finally {
            jedis.close();
        }
        return "";
    }

    @DeleteMapping("/all")
    public String deleteAllKeys() throws Exception{
        Jedis jedis = jPool.getResource();
        try {
            if(!clusterMode) {
                return jedis.flushAll();
            }
            /*else {
                return jedisCluster.flushAll
            }*/
        }
        catch(Exception e){
            throw e;
        }finally {
            jedis.close();
        }
        return "";
    }

    @PostMapping("/sadd/{key}")
    public Long sadd(@PathVariable("key") String key, @RequestParam (required = true) String value) throws Exception{
        Jedis jedis = jPool.getResource();
        try {
            if(!clusterMode) {
                return jedis.sadd(key, value);
            }
            /*else {
                return jedisCluster.flushAll
            }*/
        }
        catch(Exception e){
            throw e;
        }finally {
            jedis.close();
        }
        return null;
    }

    @GetMapping("/smembers/{key}")
    public Set<String> smembers(@PathVariable("key") String key) throws Exception{
        Jedis jedis = jPool.getResource();
        Set<String> v;
        try {
            if(!clusterMode) {
                return v = jedis.smembers(key);
            }
            /*else {
                return jedisCluster.flushAll
            }*/
        }
        catch(Exception e){
            throw e;
        }finally {
            jedis.close();
        }
        return null;
    }

}
