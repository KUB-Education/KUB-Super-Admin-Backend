package education.kub.superadmin.services.impl;

import education.kub.superadmin.entities.AdminEntity;
import education.kub.superadmin.entities.UserEntity;
import education.kub.superadmin.repositories.AdminRepo;
import education.kub.superadmin.repositories.UserRepo;
import education.kub.superadmin.services.inter.UserService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public UserEntity getUserById(Long id) {
        UserEntity user = userRepo.findById(id).orElse(null);
        if(user == null){
            throw new EntityNotFoundException(String.format("User with id=%d does not exist", id));
        }

        return user;
    }

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        UserEntity user = getUserById(id);
        userRepo.delete(user);
    }
}
