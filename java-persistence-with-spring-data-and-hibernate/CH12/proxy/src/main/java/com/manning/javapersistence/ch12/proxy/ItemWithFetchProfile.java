package com.manning.javapersistence.ch12.proxy;

import com.manning.javapersistence.ch12.Constants;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Item")
@FetchProfile(name = "item-with-bids", fetchOverrides = {
        @FetchProfile.FetchOverride(entity = ItemWithFetchProfile.class, association = "bids", mode = FetchMode.JOIN)
})
@FetchProfile(name = "with-bids-joined", fetchOverrides = {
        @FetchProfile.FetchOverride(entity = ItemWithFetchProfile.class, association = "bids", mode = FetchMode.JOIN)
})
public class ItemWithFetchProfile {

    private Long id;
    private String name;
    private LocalDate auctionEnd;
    private User seller;
    private Set<Bid> bids = new HashSet<>();
    private Set<ImageX> images = new HashSet<>();

    public ItemWithFetchProfile() {
    }

    public ItemWithFetchProfile(String name, LocalDate auctionEnd, User seller) {
        this.name = name;
        this.auctionEnd = auctionEnd;
        this.seller = seller;
    }

    @GeneratedValue(generator = Constants.ID_GENERATOR)
    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotNull
    public LocalDate getAuctionEnd() {
        return auctionEnd;
    }

    public void setAuctionEnd(LocalDate auctionEnd) {
        this.auctionEnd = auctionEnd;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    // Mapping stays LAZY. The @FetchProfile above only overrides this
    // to JOIN when a Session explicitly enables "item-with-bids".
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    public Set<Bid> getBids() {
        return bids;
    }

    public void addBid(Bid bid) {
        bids.add(bid);
    }

    public void setBids(Set<Bid> bids) {
        this.bids = bids;
    }

    // FetchProfile.FetchOverride only supports FetchMode.JOIN (Hibernate
    // throws MappingException at bootstrap for SELECT/SUBSELECT there), so
    // to avoid the bids+images Cartesian product without joining both,
    // SUBSELECT is set here statically on the mapping instead - it always
    // applies, independent of which fetch profile is enabled.
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    public Set<ImageX> getImages() {
        return images;
    }

    public void addImage(ImageX image) {
        images.add(image);
    }

    public void setImages(Set<ImageX> images) {
        this.images = images;
    }
}
