package com.manning.javapersistence.springdatajpa.repositories;

import com.manning.javapersistence.springdatajpa.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findAllByOrderByUsernameDesc();
    List<User> findByRegistrationDateBetween(LocalDate start, LocalDate end);
    List<User> findByUsernameAndEmail(String username, String email);
    List<User> findByUsernameOrEmail(String username, String email);
    List<User> findByUsernameIgnoreCase(String username);
    List<User> findByLevelOrderByUsernameDesc(int level);
    List<User> findByLevelGreaterThanEqual(int level);
    List<User> findByUsernameContaining(String text);
    List<User> findByUsernameLike(String text);
    List<User> findByUsernameStartingWith(String text);
    List<User> findByUsernameEndingWith(String text);
    List<User> findByActive(boolean active);
    List<User> findByRegistrationDateIn(Collection<LocalDate> dates);
    List<User> findByRegistrationDateNotIn(Collection<LocalDate> dates);

    // Limiting query results, sorting, and paging
    User findFirstByOrderByUsernameAsc();
    User findTopByOrderByRegistrationDateDesc();
    Page<User> findAll(Pageable pageable);
    List<User> findFirst2ByLevel(int level, Sort sort); // by default sort is asc
    List<User> findByLevel(int level, Sort sort);
    List<User> findByActive(boolean active, Pageable pageable);

}
