package com.cloudzon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cloudzon.dto.UserDto;
import com.cloudzon.model.User;

public interface UserRepository extends BaseRepository<User> {

	@Query(value = "SELECT user FROM User AS user WHERE user.email=:email")
	public User getUserByEmail(@Param("email") String email);

	@Query(value = "SELECT NEW com.cloudzon.dto.UserDto(user.id, user.firstName, user.lastName, user.email) FROM User AS user WHERE user.email != :email")
	public List<UserDto> getUsers(@Param("email") String email);

	@Query(value = "SELECT NEW com.cloudzon.dto.UserDto(user.id, user.firstName, user.lastName, user.email) FROM User AS user WHERE user.id = :userId")
	public UserDto getUserById(@Param("userId") Long userId);

}
