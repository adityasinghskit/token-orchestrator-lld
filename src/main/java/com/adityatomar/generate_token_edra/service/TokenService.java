package com.adityatomar.generate_token_edra.service;

import com.adityatomar.generate_token_edra.model.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final Map<String, Token> allTokenMap = new HashMap<>();
    //Have used map to store index of available keys to achieve O(1)
    private final Map<String, Integer> availableTokenIndex = new HashMap<>();
    // Have used list to store available keys to get random
    private final List<String> availableToken = new ArrayList<>();

    {
        log.info("Initializing keys...");
        LocalDateTime time = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        allTokenMap.put("1", new Token("1", time, null, false));
        allTokenMap.put("2", new Token("2", time, null, false));
        allTokenMap.put("3", new Token("3", time, null, false));
        allTokenMap.put("4", new Token("4", time, time, true));
        allTokenMap.put("5", new Token("5", time, time, true));

        availableTokenIndex.put("1", 0);
        availableTokenIndex.put("2", 1);
        availableTokenIndex.put("3", 2);
//        availableTokenIndex.put("4", 3);
//        availableTokenIndex.put("5", 4);

        availableToken.add("1");
        availableToken.add("2");
        availableToken.add("3");
//        availableToken.add("4");
//        availableToken.add("5");

        log.info("Initialization done: {}", allTokenMap.values());

    }

    public Token generateToken(String key) {
        if (allTokenMap.containsKey(key)) {
            return null;
        }
        Token newToken = new Token(key, LocalDateTime.now(ZoneId.of("Asia/Kolkata")), null, false);
        allTokenMap.put(key, newToken);
        availableToken.add(key);
        availableTokenIndex.put(key, availableToken.size() - 1);
        return newToken;
    }

    public String deleteToken(String key) {

        if (allTokenMap.containsKey(key)) {
            allTokenMap.remove(key);
            if(availableTokenIndex.get(key) != null){
                removeKeyForAvailableKeys(key);
            }
            return key;
        }
        return null;
    }

    public Token getToken(String key) {
        return allTokenMap.get(key);
    }

    public Token getAvailableToken() {
        int noOfAvailableTokens = availableToken.size();
        if (noOfAvailableTokens == 0){
            return null;
        }
        Random random = new Random();
        int randomIndex = (random.nextInt(noOfAvailableTokens));
        log.info("Random index: {}", randomIndex);
        String key = availableToken.get(randomIndex);

        removeKeyForAvailableKeys(key);
        return settingKeyAsBlocked(key);
    }

    public Token unblockToken(String key) {
        Token token = allTokenMap.get(key);
        if (!token.isBlocked()) {
            return null;
        }
        token.setBlocked(false);
        allTokenMap.replace(key, token);
        availableToken.add(key);
        availableTokenIndex.put(key, availableToken.size() - 1);
        return token;
    }

    public Token keepAliveToken(String key) {
        Token token = allTokenMap.get(key);
        token.setCreatedAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
        allTokenMap.replace(key, token);
        return token;
    }

    public Map getAllTokens() {
        return Map.of("all keys: ", allTokenMap.values(),
                "available index: ", printMap(availableTokenIndex),
                "available keys: ", availableToken.toString()
        );
    }

    public String printMap(Map map){
        String[] output = {""};
        map.keySet().forEach( key -> {
            output[0]+=" "+key+": "+map.get(key);
        });
        return output[0];
    }

    private void removeKeyForAvailableKeys(String key) {
        //removing key from availableTokenIndex Map
        int index = availableTokenIndex.get(key);
        availableTokenIndex.remove(key);
        //removing key from availableToken List
        removeKeyFromAvailableKeyList(key, index);


    }

    private void removeKeyFromAvailableKeyList(String key, int indexOfKey) {
        String lastKey = availableToken.get(availableToken.size() - 1);
        availableToken.remove(key);
        availableToken.add(indexOfKey, lastKey);
        availableToken.remove(availableToken.size()-1);
        availableTokenIndex.replace(lastKey, indexOfKey);
//        availableToken.add(indexOfKey, lastKey);
//        availableToken.add(availableToken.size() - 1, key);
//        availableToken.remove(availableToken.size() - 1);
    }

    private Token settingKeyAsBlocked(String key) {
        Token token = allTokenMap.get(key);
        token.setBlockedAt(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
        token.setBlocked(true);
        allTokenMap.replace(key, token);
        return token;
    }


    public void deleteExpiredTokens() {
        log.info("deleteExpiredTokens started");
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        Set<String> allKeys = allTokenMap.keySet();
        allKeys.forEach(key -> {
            Token token = allTokenMap.get(key);
            LocalDateTime expiredTime = token.getCreatedAt().plusMinutes(5);
            if (expiredTime.isBefore(currentTime)) {
                log.info("key: {} deleted.", key);
                allTokenMap.remove(key);
                Integer index = availableTokenIndex.get(key);
                availableTokenIndex.remove(key);
                if (index != null) {
                    removeKeyFromAvailableKeyList(key, index);
                }
            }
        });
    }

    public void unblockTokens() {
        log.info("unblockTokens started");
        LocalDateTime currentTime = LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
        Set<String> allKeys = allTokenMap.keySet();
        allKeys.stream()
                .filter(key -> allTokenMap.get(key).isBlocked())
                .forEach(key -> {
                    Token token = allTokenMap.get(key);
                    LocalDateTime blockedTime = token.getBlockedAt().plusMinutes(1);
                    if (blockedTime.isBefore(currentTime)) {
                        log.info("key: {} unblocked.", key);
                        token.setBlocked(false);
                        allTokenMap.replace(key, token);
                        availableToken.add(key);
                        availableTokenIndex.put(key, availableToken.size() - 1);
                    }
                });
    }

}
