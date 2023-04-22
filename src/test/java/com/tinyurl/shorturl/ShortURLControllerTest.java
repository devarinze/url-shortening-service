package com.tinyurl.shorturl;

import com.tinyurl.click.Click;
import com.tinyurl.click.ClickRepository;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class ShortURLControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ShortURLRepository shortURLRepository;
    @Autowired
    private ClickRepository clickRepository;
    private final String url = "/api/v1/short-url";

    @AfterAll
    public void tearDown() {
        shortURLRepository.deleteAll();
    }

    @Test
    public void createShortURL() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(url.concat("/create"))
                        .param("redirectLink", "https://www.goal.com/en-ng")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.urlKey").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.redirectLink").value("https://www.goal.com/en-ng"));
    }

    @Test
    public void createShortURLWithCustomURLKey() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(url.concat("/create"))
                        .param("redirectLink", "https://www.goal.com/en-ng")
                        .param("customURLKey", "myalias")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.urlKey").value("myalias"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload.redirectLink").value("https://www.goal.com/en-ng"));

        ShortURL shortURL = shortURLRepository.findByUrlKey("myalias");
        MatcherAssert.assertThat(shortURL.getRedirectLink(), CoreMatchers.is("https://www.goal.com/en-ng"));
    }

    @Test
    public void getRedirectLink() throws Exception {
        ShortURL shortURL = new ShortURL("https://www.goal.com/en-ng", "SYSTEM");
        shortURL.setUrlKey("myalias");
        shortURLRepository.save(shortURL);
        this.mockMvc.perform(MockMvcRequestBuilders.get(url.concat("/redirect-link"))
                        .param("urlKey", "myalias")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.payload").value("https://www.goal.com/en-ng"));

        Click click = clickRepository.findByUrlKeyAndCreateDateDay("myalias", new Date());
        MatcherAssert.assertThat(click.getDailyTotal(), CoreMatchers.is(1));
    }
}
