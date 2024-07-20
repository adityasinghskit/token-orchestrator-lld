package com.adityatomar.generate_token_edra.controller;

import com.adityatomar.generate_token_edra.dto.GenerateTokenRequestDto;
import com.adityatomar.generate_token_edra.model.Token;
import com.adityatomar.generate_token_edra.service.TokenService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TokenController {
    private final TokenService tokenService;

    /**
     * Api to generate token
     *
     * @param requestBody
     * @return
     */
    @PostMapping(value = "/keys", consumes = "application/json", produces = "application/json")
    public ResponseEntity generateToken(@RequestBody @Valid @NonNull GenerateTokenRequestDto requestBody) {
        Token tokenMap = tokenService.generateToken(requestBody.getToken());
        if (tokenMap == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenMap);
    }

    /**
     * Api to get all key
     *
     * @return
     */
    @GetMapping(value = "/allKeys", produces = "application/json")
    public ResponseEntity getAllToken() {
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenService.getAllTokens());
    }

    /**
     * Api to retrieve available key
     *
     * @return
     */
    @GetMapping(value = "/keys", produces = "application/json")
    public ResponseEntity getAvailableToken() {
        Token token = tokenService.getAvailableToken();
        if (token == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    /**
     * Api to get token info
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/keys/{id}", produces = "application/json")
    public ResponseEntity getTokenInfo(@PathVariable @Valid String id) {
        Token token = tokenService.getToken(id);
        if(token == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(tokenService.getToken(id));
    }

    /**
     * Api to delete a token
     *
     * @param id
     * @return
     */
    @DeleteMapping(value = "/keys/{id}", produces = "application/json")
    public ResponseEntity deleteToken(@PathVariable @Valid String id) {
        String tokenMap = tokenService.deleteToken(id);
        if (tokenMap == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    /**
     * Api to unblock the provided token
     * @param id
     * @return
     */
    @PutMapping(value = "/keys/{id}", produces = "application/json")
    public ResponseEntity unblockToken(@PathVariable @Valid String id) {
        Token token = tokenService.unblockToken(id);
        if(token == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

    /**
     * Api to keep the key Alive
     * @param id
     * @return
     */
    @PutMapping(value = "/keepalive/{id}", produces = "application/json")
    public ResponseEntity keepAliveToken(@PathVariable @Valid String id) {
        Token token = tokenService.keepAliveToken(id);
        if(token == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }


}
