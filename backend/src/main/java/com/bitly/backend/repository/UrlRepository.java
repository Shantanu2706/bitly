package com.bitly.backend.repository;

import com.bitly.backend.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlRepository extends JpaRepository<Url,String> {
}
