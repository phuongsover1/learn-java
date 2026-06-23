package com.manning.javapersistence.ch11.timestamp;

import com.manning.javapersistence.ch11.Constants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 * Same optimistic-locking idea as {@link Item}, but with a numeric {@code @Version}
 * so a database trigger can detect updates that did not come from Hibernate.
 */
@Entity
@Table(name = "SHARED_ITEM")
public class SharedItem {

    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    private Long id;

    @Version
    private long version;

    @NotNull
    private String name;

    public SharedItem() {
    }

    public SharedItem(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
