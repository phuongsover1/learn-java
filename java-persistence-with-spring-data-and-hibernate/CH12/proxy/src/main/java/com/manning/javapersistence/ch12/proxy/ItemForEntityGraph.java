package com.manning.javapersistence.ch12.proxy;

import com.manning.javapersistence.ch12.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Same table as Item, but seller is mapped EAGER on purpose - it plays the
 * role of "an association outside the entity graph that someone later
 * flipped to EAGER in the mapping". bids stays LAZY by default. The named
 * graph below lists only bids, never seller.
 */
@Entity
@Table(name = "Item")
@NamedEntityGraph(
        name = "Item.withBids",
        attributeNodes = {
                @NamedAttributeNode("bids")
        }
)
@NamedEntityGraph(
        name = "Item.withBidsAndImages",
        attributeNodes = {
                @NamedAttributeNode("bids"),
                @NamedAttributeNode("images")
        }
)
@NamedEntityGraph(
        name = "Item.withImages",
        attributeNodes = {
                @NamedAttributeNode("images")
        }
)
public class ItemForEntityGraph {

    private Long id;
    private String name;
    private LocalDate auctionEnd;
    private User seller;
    private Set<BidForEntityGraph> bids = new HashSet<>();
    private Set<ImageXForEntityGraph> images = new HashSet<>();

    public ItemForEntityGraph() {
    }

    public ItemForEntityGraph(String name, LocalDate auctionEnd, User seller) {
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
    @ManyToOne(fetch = FetchType.EAGER)
    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    @OneToMany(mappedBy = "item")
    public Set<BidForEntityGraph> getBids() {
        return bids;
    }

    public void addBid(BidForEntityGraph bid) {
        bids.add(bid);
    }

    public void setBids(Set<BidForEntityGraph> bids) {
        this.bids = bids;
    }

    @OneToMany(mappedBy = "item")
    public Set<ImageXForEntityGraph> getImages() {
        return images;
    }

    public void addImage(ImageXForEntityGraph image) {
        images.add(image);
    }

    public void setImages(Set<ImageXForEntityGraph> images) {
        this.images = images;
    }
}
