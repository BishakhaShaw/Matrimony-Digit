//package digit.matrimony.security.logout;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class TokenBlacklistService {
//
//    private final CacheManager cacheManager;
//
//    private Cache cache() {
//        return cacheManager.getCache("jwtBlacklist");
//    }
//
//    public void blacklist(String token) {
//        cache().put(token, Boolean.TRUE);
//    }
//
//    public boolean isBlacklisted(String token) {
//        Boolean val = cache().get(token, Boolean.class);
//        return Boolean.TRUE.equals(val);
//    }
//}



















package digit.matrimony.security.logout;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {

    private final Set<String> blacklistedTokens = new HashSet<>();

    public void blacklist(String token) {
        blacklistedTokens.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
