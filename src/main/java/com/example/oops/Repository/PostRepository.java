package com.example.oops.Repository;
import com.example.oops.models.post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<post,Long>{
}
