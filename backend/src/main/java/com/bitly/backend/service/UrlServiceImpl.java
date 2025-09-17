package com.bitly.backend.service;

import com.bitly.backend.entity.Url;
import com.bitly.backend.repository.UrlRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UrlServiceImpl implements UrlService {
    private final UrlRepository urlRepository;
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public UrlServiceImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @Override
    public void shorten(String longUrl) {
        try{
            if (!longUrl.startsWith("http://") && !longUrl.startsWith("https://")) {
                longUrl = "https://" + longUrl; // default scheme
            }
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(longUrl.getBytes(StandardCharsets.UTF_8));
            String base62Encoded = toBase62(hash);
            Url url = new Url();
            url.setOriginalUrl(longUrl);
            url.setShortCode(base62Encoded.substring(0,6));
            urlRepository.save(url);
        }catch (NoSuchAlgorithmException e){
            throw new RuntimeException("Error while hashing the URL", e);
        }

    }

    @Override
    public String getOriginalUrl(String shortUrl) {
        String shortCode = shortUrl.replace("http://short.ly/", "");
        return urlRepository.findById(shortCode)
                .map(Url::getOriginalUrl)
                .orElseThrow(() -> new RuntimeException("URL not found"));
    }

    private String toBase62(byte[] hash) {
        StringBuilder base62 = new StringBuilder();
        for(byte b: hash){
            int value = b & 0xFF;
            base62.append(BASE62.charAt(value % 62));
        }
        return base62.toString();
    }
}
