package com.tinyurl.core.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Data
public class Paging {
    int limit = 10;
    String order = null;
    int page = 1;

    @JsonIgnore
    public Sort sort() {
        Sort.Direction dir = Sort.Direction.ASC;
        if (this.order == null) {
            return null;
        }
        if (this.order.startsWith("-")) {
            dir = Sort.Direction.DESC;
            this.order = this.order.substring(1);
        }
        return Sort.by(dir, this.order);
    }

    @JsonIgnore
    public PageRequest pageRequest() {
        Sort sort = sort();
        if (sort != null)
            return PageRequest.of(this.page(), this.limit, sort);
        return PageRequest.of(this.page(), this.limit);
    }

    int page() {
        return this.page - 1;
    }

    public static Paging of(Integer page, Integer limit, String order) {
        Paging paging = new Paging();
        if (page != null) paging.page = page;
        if (limit != null) paging.limit = limit;
        if (order != null) paging.order = order;
        return paging;
    }
}
