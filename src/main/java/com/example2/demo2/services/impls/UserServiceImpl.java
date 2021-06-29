package com.example2.demo2.services.impls;

import com.example2.demo2.entities.Role;
import com.example2.demo2.entities.Users;
import com.example2.demo2.repositories.RoleRepository;
import com.example2.demo2.repositories.UserRepository;
import com.example2.demo2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // as beans podt
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users u=userRepository.findByEmail(s);
        if(u!=null){
            User secUser=new User(u.getEmail(),u.getPassword(),u.getRoles());
            return secUser;
        }
        throw new UsernameNotFoundException("User with such email not found");
    }

    @Override
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<Users> listAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users listUser(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void saveUser(Users user) {
        userRepository.save(user);
    }

    @Override
    public List<Role> listAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role listRole(Long id) {
        return roleRepository.getOne(id);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public List<Users> findRolesUserId(Long id) {
        return userRepository.findAllByRolesId(id);
    }

    @Override
    public Role findRoleByNameF(String name){
        return roleRepository.findRoleByName(name);
    }

    @Override
    public boolean RoleExists(Long id) {
        if(roleRepository.checkRole(id)>0){
            return true;
        }
        return false;
    }

    @Override
    public boolean UserExists(Long id) {
        if(userRepository.checkUser(id)>0){
            return true;
        }
        return false;
    }

}
