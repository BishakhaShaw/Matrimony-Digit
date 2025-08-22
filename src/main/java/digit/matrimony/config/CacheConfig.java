package digit.matrimony.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        var manager = new SimpleCacheManager();

        var blacklistCache = new CaffeineCache(
                "jwtBlacklist",
                Caffeine.newBuilder()
                        .expireAfterWrite(30, TimeUnit.MINUTES) // should be >= token lifetime
                        .maximumSize(10_000)
                        .build()
        );

        var usersCache = new CaffeineCache(
                "users",
                Caffeine.newBuilder()
                        .expireAfterWrite(10, TimeUnit.MINUTES) // 🔑 tune as needed
                        .maximumSize(5_000)
                        .build()
        );

        manager.setCaches(List.of(blacklistCache, usersCache));
        return manager;
    }
}
