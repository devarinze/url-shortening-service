package com.tinyurl.shorturl;

import com.tinyurl.click.Click;
import com.tinyurl.click.ClickRepository;
import com.tinyurl.security.auth.AuthService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static com.tinyurl.core.utils.DateUtils.localDateToDate;

@Transactional
@Service
public class ShortURLService {

    @Autowired
    ShortURLRepository shortURLRepository;
    @Autowired
    ClickRepository clickRepository;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * creates a unique url key for a given link. the custom url consists of this unique key and the subdomain
     * if the same user provides the same link, the same key is returned
     */
    public ShortURL createShortURL(String redirectLink) {
        String userName = AuthService.currentAuditDetails().getUserName();
        ShortURL found = shortURLRepository.findByCreatedByAndRedirectLinkAndCustomized(userName, redirectLink, false);
        if (found == null) {
            ShortURL shortURL = new ShortURL(redirectLink, userName);
            addURLKey(shortURL);
            return (shortURLRepository.save(shortURL));
        } else {
            return found;
        }
    }

    /**
     * custom urls consist of the subdomain (e.g 'localhost:4200') and a custom alias provided by the user
     * custom url keys are generated even if the same user provides the same link but with a different alias
     */
    public ShortURL createShortURL(String redirectLink, String customURLKey) throws Exception {
        String userName = AuthService.currentAuditDetails().getUserName();
        ShortURL found = shortURLRepository.findByUrlKey(customURLKey);
        if (found == null) {
            ShortURL shortURL = new ShortURL(redirectLink, userName);
            shortURL.setUrlKey(customURLKey);
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

    public String getRedirectLink(String urlKey) throws Exception {
        ShortURL shortURL = shortURLRepository.findByUrlKey(urlKey);
        if (shortURL != null) {
            createClick(urlKey);
            return shortURL.getRedirectLink();
        } else {
            throw new Exception("URL not found");
        }
    }

    /**
     * when any generated link is visited, the number of clicks for that day are updated
     */
    public void createClick(String urlKey) {
        Click found = clickRepository.findByUrlKeyAndCreateDateDay(urlKey, new Date());
        if (found == null) {
            Click click = new Click(urlKey);
            clickRepository.save(click);
        } else {
            int total = found.getDailyTotal();
            found.setDailyTotal(++total);
            clickRepository.save(found);
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
        Date date = localDateToDate(LocalDate.now().minusYears(1));
        List<ShortURL> expiredURLs = shortURLRepository.findAllByCreateDateIsLessThanEqual(date);
        for (ShortURL expiredURL: expiredURLs) {
            expiredURL.setExpired(true);
            clickRepository.deleteAllByUrlKey(expiredURL.getUrlKey());
        }
        shortURLRepository.saveAll(expiredURLs);
    }
}
