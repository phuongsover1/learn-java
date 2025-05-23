package com.manning.javapersistence.springdatajpa.repositories;

import com.manning.javapersistence.springdatajpa.model.User;
import com.manning.javapersistence.springdatajpa.model.UserProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.util.Streamable;
import org.springframework.transaction.annotation.Transactional;

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

    // Returning Streaming results
    Streamable<User> findByEmailContaining(String text);
    Streamable<User> findByLevel(int level);

    // @Query annonation
    @Query("select count(u) from User u where u.active = ?1")
    int findNumberOfUserByActive(boolean active);

    @Query("select u from User u where u.level = :level and u.active = :active")
    List<User> findByLevelAndActive(@Param("level") int level, @Param("active") boolean active);

    @Query(value = "SELECT COUNT(*) FROM USERS WHERE ACTIVE = ?1", nativeQuery = true)
    int findNumberOfUserByActiveNativeQuery(boolean active);


    // #{#entityName} sẽ lấy tên entity dựa trên repository mà gọi method -> UserRepository<User, Long> -> Lấy User
    @Query("select u.username, LENGTH(u.email) as email_length from #{#entityName} u where u.username like %?1%")
    List<Object[]> findByAsArrayAndSort(String text, Sort sort);

    // Projections
    List<UserProjection.UserSummary> findByRegistrationDateAfter(LocalDate date);
    List<UserProjection.UsernameOnly> findByEmailLike(String text);

    <T> List<T> findByLevel(int level, Class<T> type); // dùng cái này nếu như ta muốn dùng lại method này cho nhiều kiểu projection (ví dụ như là chỉ lấy mỗi mail hoặc username, hoặc có thể cả 2)

    // Modifying Queries
    @Modifying
    @Transactional
    @Query("update User u set u.level = :newLevel where u.level = :oldLevel")
    int updateLevel(int oldLevel, int newLevel);

    @Transactional
    int deleteByLevel(int level);

    @Modifying
    @Transactional
    @Query("delete from User u where u.level = ?1")
    int deleteBulkByLevel(int level);
}
