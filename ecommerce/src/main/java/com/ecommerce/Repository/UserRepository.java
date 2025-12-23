package com.ecommerce.Repository;

import com.ecommerce.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUserName(String userName);
    Optional<User> findByEmail(String email);

}
