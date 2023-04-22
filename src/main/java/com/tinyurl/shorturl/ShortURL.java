package com.tinyurl.shorturl;

import com.tinyurl.click.Click;
import com.tinyurl.core.data.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "short_url")
@Where(clause = "expired = false")
public class ShortURL extends BaseEntity {

    public ShortURL(String redirectLink, String createdBy) {
        this.redirectLink = redirectLink;
        this.createdBy = createdBy;
    }

    @Column(name="url_key", nullable = false)
    private String urlKey;

    @Column(name="redirect_link", nullable = false)
    private String redirectLink;

    @Column(name="created_by", nullable = false)
    private String createdBy;

    @Column(name = "expired")
    private Boolean expired = false;

    @Column(name = "customized")
    private Boolean customized = false;
}
