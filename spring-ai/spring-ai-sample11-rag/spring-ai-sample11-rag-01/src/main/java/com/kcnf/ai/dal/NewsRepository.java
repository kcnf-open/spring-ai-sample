package com.kcnf.ai.dal;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    // 根据日期删除旧数据（用于更新）
    void deleteByDate(LocalDate date);

    // 关键字查询：标题或内容包含关键字（不区分大小写）
    @Query("SELECT n FROM News n WHERE LOWER(n.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(n.content) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY n.date DESC, n.indexInDay ASC")
    List<News> searchByKeyword(@Param("keyword") String keyword);
}