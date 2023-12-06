package com.ukma.cleaning_v2.comment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentDao extends JpaRepository<CommentEntity, Long> {
    
}
