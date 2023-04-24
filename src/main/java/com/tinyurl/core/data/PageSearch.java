package com.tinyurl.core.data;

import lombok.Data;

@Data
public class PageSearch<T> {
    Paging page = new Paging();
    T data = null;
}
