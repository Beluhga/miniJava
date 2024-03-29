package com.michaelalbuquerque.minijava.users;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserReposutory extends JpaRepository<UserModel, UUID> {

    UserModel findByUsername(String username);
}
