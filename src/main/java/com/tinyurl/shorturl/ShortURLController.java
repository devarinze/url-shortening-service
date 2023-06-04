package com.tinyurl.shorturl;

import com.tinyurl.core.data.PageSearch;
import com.tinyurl.core.data.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/short-url")
public class ShortURLController {

    @Autowired
    private ShortURLService shortURLService;

    @PostMapping("/create")
    public Response create(@RequestBody ShortURL shortURL) throws Exception {
        if (!StringUtils.hasLength(shortURL.getUrlKey())) {
            return Response.of(shortURLService.createShortURL(shortURL));
        } else {
            return Response.of(shortURLService.createShortURLWithAlias(shortURL));
        }
    }

    @PostMapping("/all")
    public Response getShortURLs(@RequestBody PageSearch<ShortURL> ps) {
        return Response.of(shortURLService.getShortURLsByUsername(ps));
    }

    @GetMapping("/redirect-link")
    public Response getRedirectLink(@RequestParam("urlKey") String urlKey) throws Exception {
        return Response.of(shortURLService.getRedirectLink(urlKey));
    }
}
