package com.ecommerce.Entity;

import com.ecommerce.Role;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {

    @Id
    private  String id;

    private String name;

    private String userName;

    private String email;

    private String password;

    private Role role = Role.USER;

}
