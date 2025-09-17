package com.bitly.backend.service;

public interface UrlService {
    void shorten(String longUrl);
    String getOriginalUrl(String shortUrl);
}
