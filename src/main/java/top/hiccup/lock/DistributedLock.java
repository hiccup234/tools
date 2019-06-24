package top.hiccup.lock;

import java.util.Collections;

import redis.clients.jedis.Jedis;

/**
 * 采用Redis实现分布式锁（也可借助Redis的官方客户端Redisson来实现）
 *
 * @author wenhy
 * @date 2019/6/19
 */
public class DistributedLock {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";

    /**
     * 尝试获取分布式锁
     *
     * @param jedis      Redis客户端
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
        // 不推荐的做法
//        Long result = jedis.setnx(lockKey, requestId);
//        if (result == 1) {
//            // 若在这里程序突然崩溃，则无法设置过期时间，将发生死锁
//            jedis.expire(lockKey, expireTime);
//        }

        // 注意，低版本的Redis不支持多参数的set方法
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }


    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 释放分布式锁：需要Redis服务器支持Lua脚本（有些云Redis为了安全考虑会关闭Lua脚本的执行权限）
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {
        // requiestId来判断，谁加锁就谁解锁，原子地执行
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }


    /**
     * 如代码注释，问题在于如果调用jedis.del()方法的时候，这把锁已经不属于当前客户端的时候会解除他人加的锁。
     * 那么是否真的有这种场景？答案是肯定的，比如客户端A加锁，一段时间之后客户端A解锁，在执行jedis.del()之前，
     * 锁突然过期了，此时客户端B尝试加锁成功，然后客户端A再执行del()方法，则将客户端B的锁给解除了。
     *
     * @param jedis
     * @param lockKey
     * @param requestId
     */
    public static void wrongReleaseLock2(Jedis jedis, String lockKey, String requestId) {

        // 判断加锁与解锁是不是同一个客户端
        if (requestId.equals(jedis.get(lockKey))) {
            // 若在此时，这把锁突然不是这个客户端的，则会误解锁
            jedis.del(lockKey);
        }
    }
}
