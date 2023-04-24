package com.tinyurl.shorturl;

import com.tinyurl.core.data.PageSearch;
import com.tinyurl.security.auth.AuthService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Transactional
@Service
public class ShortURLService {

    @Autowired
    ShortURLRepository shortURLRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * creates a unique url key for a given link. the custom url consists of this unique key and the subdomain
     */
    public ShortURL createShortURL(ShortURL shortURL) {
        String userName = AuthService.currentAuditDetails().getUserName();
        shortURL.setCreatedBy(userName);
        addURLKey(shortURL);
        return shortURLRepository.save(shortURL);
    }

    /**
     * custom urls consist of the subdomain (e.g 'localhost:4200') and a custom alias provided by the user
     * custom url keys are generated even if the same user provides the same link but with a different alias
     */
    public ShortURL createShortURLWithAlias(ShortURL shortURL) throws Exception {
        String userName = AuthService.currentAuditDetails().getUserName();
        ShortURL found = shortURLRepository.findByUrlKey(shortURL.getUrlKey());
        if (found == null) {
            shortURL.setCreatedBy(userName);
            shortURL.setCustomized(true);
            return shortURLRepository.save(shortURL);
        } else {
            throw new Exception("Alias is not available");
        }
    }

    private void addURLKey(ShortURL shortURL){
        String urlKey = RandomStringUtils.randomAlphanumeric(7);
        ShortURL found = shortURLRepository.findByUrlKey(urlKey);
        if (found == null) {
            shortURL.setUrlKey(urlKey);
        } else {
            addURLKey(shortURL);
        }
    }

    public Page<ShortURL> getShortURLsByUsername(PageSearch<ShortURL> ps) {
        String userName = AuthService.currentAuditDetails().getUserName();
        ps.getPage().setOrder("-createDate");
        return shortURLRepository.findByCreatedBy(userName, ps.getPage().pageRequest());
    }

    /**
     * when any generated link is visited, the number of clicks are updated
     */
    public String getRedirectLink(String urlKey) throws Exception {
        ShortURL shortURL = shortURLRepository.findByUrlKey(urlKey);
        if (shortURL != null) {
            Long total = shortURL.getClicks();
            shortURL.setClicks(++(total));
            return shortURLRepository.save(shortURL).getRedirectLink();
        } else {
            throw new Exception("URL not found");
        }
    }

    /**
     * cron job that marks every url that's over a year old as expired
     * note that only valid (not expired) urls are fetched from the db
     * clicks related to expired urls are deleted
     */
    @Scheduled(cron = "${expired.urls.expression}")
    public void deleteExpiredURLs() {
        logger.info("Deleting expired urls");
        Date date = new Date();
        List<ShortURL> expiredURLs = shortURLRepository.findAllByExpiredAndExpiryDateIsLessThanEqual(false, date);
        for (ShortURL expiredURL: expiredURLs) {
            expiredURL.setExpired(true);
        }
        shortURLRepository.saveAll(expiredURLs);
    }
}
