package com.ween.cloud.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.ween.cloud.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @HystrixCommand中的常用参数 fallbackMethod：指定服务降级处理方法；
 * ignoreExceptions：忽略某些异常，不发生服务降级；
 * commandKey：命令名称，用于区分不同的命令；
 * groupKey：分组名称，Hystrix会根据不同的分组来统计命令的告警及仪表盘信息；
 * threadPoolKey：线程池名称，用于划分线程池。
 */
@Service
public class UserService {

    @Autowired
    private RestTemplate restTemplate;
    @Value("${service-url.user-service}")
    private String userServiceUrl;

    public User getDefaultUser(@PathVariable Long id) {
        return new User(-1L, "defaultUser", 99);
    }

    @HystrixCommand(fallbackMethod = "getDefaultUser")
    public User get(Long id) {
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", User.class, id);
    }

    public User getDefaultUser2(@PathVariable Long id, Throwable e) {
        System.out.println("服务抛出异常:" + e.getClass());
        return new User(-1L, "defaultUser", 99);
    }

    //当抛出空指针异常时服务忽略降级
    @HystrixCommand(fallbackMethod = "getDefaultUser2",
            ignoreExceptions = {NullPointerException.class})
    public User getUserException(Long id) {
        if (id == 1L) {
            throw new IndexOutOfBoundsException();
        } else if (id == 2L) {
            throw new NullPointerException();
        }
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", User.class, id);
    }

    /**
     * Hystrix请求缓存
     * 当系统并发量越来越大时，我们需要使用缓存来优化系统，达到减轻并发请求线程数，提供响应速度的效果。
     *
     * @CacheResult：开启缓存，默认所有参数作为缓存的key，cacheKeyMethod可以通过返回String类型的方法指定key；
     * @CacheKey：指定缓存的key，可以指定参数或指定参数中的属性值为缓存key，cacheKeyMethod还可以通过返回String类型的方法指定；
     * @CacheRemove：移除缓存，需要指定commandKey。
     */

    //为缓存生成Key
    public String getCacheKey(Long id) {
        return String.valueOf(id);
    }

    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(fallbackMethod = "getDefaultUser",
            commandKey = "getUserCache")
    public User getUserCache(Long id) {
        System.out.println("看到打印输出则没有从缓存中获取数据");
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", User.class, id);
    }

    @CacheRemove(commandKey = "getUserCache", cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public String removeCache(Long id) {
        return restTemplate.postForObject(userServiceUrl + "/user/delete/{1}", null, String.class, id);
    }

    @HystrixCommand(
            fallbackMethod = "getDefaultUser",
            commandKey = "getUserCommand",
            groupKey = "getUserGroup",
            threadPoolKey = "getUserThreadPool"
    )
    public User getUserCommand(@PathVariable Long id) {
        return restTemplate.getForObject(userServiceUrl + "/user/{1}", User.class, id);
    }

    @HystrixCollapser(
            batchMethod = "getUserByIds",
            collapserProperties = {
                    @HystrixProperty(name = "timerDelayInMilliseconds", value = "100")
            }
    )
    public Future<User> getUserFuture(Long id) {
        return new AsyncResult<User>() {
            @Override
            public User invoke() {
                return restTemplate.getForObject(userServiceUrl + "/user{1}", User.class, id);
            }
        };
    }

    @HystrixCommand
    public List<User> getUserByIds(List<Long> ids) {
        return restTemplate.getForObject(userServiceUrl + "/user/getUserByIds?ids={1}", List.class, StringUtils.collectionToDelimitedString(ids, ","));
    }
}
