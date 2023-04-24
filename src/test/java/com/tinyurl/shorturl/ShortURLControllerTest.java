package com.tinyurl.shorturl;

import com.tinyurl.core.utils.Utils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import com.tinyurl.core.utils.DateUtils;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class ShortURLControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ShortURLRepository shortURLRepository;
    private final String url = "/api/v1/short-url";

    @AfterEach
    public void tearDown() {
        shortURLRepository.deleteAll();
    }

    @Test
    public void createShortURL() throws Exception {
        Date expiryDate = com.tinyurl.core.utils.DateUtils.localDateToDate(LocalDate.now().plusYears(1));
        ShortURL shortURL = new ShortURL();
        shortURL.setRedirectLink("https://www.goal.com/en-ng");
        shortURL.setExpiryDate(expiryDate);
        this.mockMvc.perform(MockMvcRequestBuilders.post(url.concat("/create"))
                        .content(Objects.requireNonNull(Utils.toJson(shortURL)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.urlKey").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.redirectLink").value("https://www.goal.com/en-ng"));
    }

    @Test
    public void createShortURLWithCustomURLKey() throws Exception {
        Date expiryDate = com.tinyurl.core.utils.DateUtils.localDateToDate(LocalDate.now().plusYears(1));
        ShortURL shortURL = new ShortURL();
        shortURL.setRedirectLink("https://www.goal.com/en-ng");
        shortURL.setExpiryDate(expiryDate);
        shortURL.setUrlKey("myalias");
        this.mockMvc.perform(MockMvcRequestBuilders.post(url.concat("/create"))
                        .content(Objects.requireNonNull(Utils.toJson(shortURL)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.urlKey").value("myalias"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.redirectLink").value("https://www.goal.com/en-ng"));

        ShortURL found = shortURLRepository.findByUrlKey("myalias");
        MatcherAssert.assertThat(found.getRedirectLink(), CoreMatchers.is("https://www.goal.com/en-ng"));
    }

    @Test
    public void getRedirectLink() throws Exception {
        Date expiryDate = com.tinyurl.core.utils.DateUtils.localDateToDate(LocalDate.now().plusYears(1));
        ShortURL shortURL = new ShortURL("https://www.goal.com/en-ng", "SYSTEM", expiryDate);
        shortURL.setUrlKey("myalias");
        shortURLRepository.save(shortURL);
        this.mockMvc.perform(MockMvcRequestBuilders.get(url.concat("/redirect-link"))
                        .param("urlKey", "myalias")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").value("https://www.goal.com/en-ng"));
        ShortURL found = shortURLRepository.findByUrlKey("myalias");
        MatcherAssert.assertThat(found.getClicks(), CoreMatchers.is(1L));
    }
}
