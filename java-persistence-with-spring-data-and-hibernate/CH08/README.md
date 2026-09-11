# CH08 — Mapping Collections and Entity Associations

Tracks learning progress through this chapter. Update the checkboxes as
each mapping is added and observed (predicted SQL vs actual SQL) in the
`collections` module.

## Baseline

- [x] `User`, `Item`, `Bid` created with no association — plain entities,
      each in its own table, no foreign keys.

## Collection Mappings To Explore

- [ ] `Set<Bid>` on `Item` (`@OneToMany`, unordered, no duplicates)
- [ ] `Bag` semantics (`List` without `@OrderColumn` — no real order,
      allows duplicates)
- [ ] `List<Bid>` with `@OrderColumn` (persistent order via index column)
- [ ] `Map<K, V>` association (e.g. keyed by a `Bid` property)
- [ ] Unidirectional vs bidirectional `@OneToMany`/`@ManyToOne`
- [ ] Owning side vs inverse side (`mappedBy`)
- [ ] Cascade types on collections
- [ ] `orphanRemoval`
- [ ] `@ManyToMany` (e.g. `Item` categories)

## Per-Mapping Notes Template

When a mapping is added, capture:

- Predicted SQL (before running)
- Actual SQL (after running)
- Why Hibernate generated it that way
- Trade-offs vs the closest alternative mapping
- One thing that was surprising or counter-intuitive

## Session Log

(Append one entry per learning session — date, what was covered, key
takeaway, what's next.)
