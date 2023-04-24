package com.tinyurl.shorturl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface ShortURLRepository extends JpaRepository<ShortURL, Long> {
    ShortURL findByUrlKey(String urlKey);

    Page<ShortURL> findByCreatedBy(String userName, Pageable pageable);
    ShortURL findByCreatedByAndRedirectLinkAndCustomized(String createdBy, String redirectLink, Boolean customized);
    List<ShortURL> findAllByExpiredAndExpiryDateIsLessThanEqual(Boolean expired, Date date);
}
