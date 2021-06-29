package com.example2.demo2.services;

import com.example2.demo2.entities.Brand;
import com.example2.demo2.entities.Role;
import com.example2.demo2.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    Users getUserByEmail(String email);
    List<Users> listAllUsers();
    Users listUser(Long id);
    void deleteUser(Long id);
    void saveUser(Users user);

    List<Role> listAllRoles();
    Role listRole(Long id);
    void deleteRole(Long id);
    void saveRole(Role role);

    List<Users> findRolesUserId(Long id);
    Role findRoleByNameF(String name);

    boolean RoleExists(Long id);
    boolean UserExists(Long id);


    //Users createUser(Users user);


}
