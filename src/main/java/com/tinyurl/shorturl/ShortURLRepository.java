package com.tinyurl.shorturl;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ShortURLRepository extends JpaRepository<ShortURL, Long> {
    ShortURL findByUrlKey(String urlKey);
    ShortURL findByCreatedByAndRedirectLinkAndCustomized(String createdBy, String redirectLink, Boolean customized);
    List<ShortURL> findAllByCreateDateIsLessThanEqual(Date date);
}
