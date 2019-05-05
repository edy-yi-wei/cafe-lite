package com.besoft.cafelite.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.besoft.cafelite.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Page<User> findByUserNameContainingAllIgnoreCase(String userName, Pageable page);
	
	@Query("SELECT u FROM User u WHERE u.role.roleName LIKE %:roleName%")
	Page<User> findByRoleName(@Param("roleName") String roleName, Pageable page);
	
}
