package com.tinyurl.security.users;

import com.tinyurl.core.utils.Utils;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
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

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    private final String url = "/api/v1/user";

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void registerUser() throws Exception {
        User user = new User("Jon", "Jones", "jon.jones@localhost", "password");
        this.mockMvc.perform(MockMvcRequestBuilders.post(url.concat("/signup"))
                        .content(Utils.toJson(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
        User found = userRepository.findByUserName("jon.jones@localhost");
        MatcherAssert.assertThat(found.getFirstName(), CoreMatchers.is("Jon"));
        MatcherAssert.assertThat(found.getLastName(), CoreMatchers.is("Jones"));
    }
}
