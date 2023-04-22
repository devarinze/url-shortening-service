package com.tinyurl.click;

import com.tinyurl.core.data.BaseEntity;
import com.tinyurl.shorturl.ShortURL;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "click")
public class Click extends BaseEntity {

    public Click(String urlKey) {
        this.urlKey = urlKey;
    }

    @Column(name="url_key", nullable = false)
    private String urlKey;

    @Column(name="daily_total", nullable = false)
    private int dailyTotal = 1;
}
