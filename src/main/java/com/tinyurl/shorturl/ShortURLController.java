package com.tinyurl.shorturl;

import com.tinyurl.core.data.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/short-url")
public class ShortURLController {

    @Autowired
    private ShortURLService shortURLService;

    @GetMapping("/create")
    public Response create(@RequestParam("redirectLink") String redirectLink, @RequestParam(value = "customURLKey", required = false) String customURLKey) throws Exception {
        if (!StringUtils.hasLength(customURLKey)) {
            return Response.of(shortURLService.createShortURL(redirectLink));
        } else {
            return Response.of(shortURLService.createShortURL(redirectLink, customURLKey));
        }
    }

    @GetMapping("/redirect-link")
    public Response getRedirectLink(@RequestParam("urlKey") String urlKey) throws Exception {
        return Response.of(shortURLService.getRedirectLink(urlKey));
    }
}
