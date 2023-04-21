package com.tinyurl.click;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface ClickRepository extends JpaRepository<Click, Long> {
    void deleteAllByUrlKey(String urlKey);

    @Query("SELECT c FROM Click AS c WHERE c.urlKey = ?1 AND date(c.createDate) = ?2")
    Click findByUrlKeyAndCreateDateDay(String urlKey, Date date);
}
