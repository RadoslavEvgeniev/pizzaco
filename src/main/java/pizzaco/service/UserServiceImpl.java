package pizzaco.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pizzaco.domain.entities.User;
import pizzaco.domain.entities.UserRole;
import pizzaco.domain.models.service.UserServiceModel;
import pizzaco.repository.RoleRepository;
import pizzaco.repository.UserRepository;

import java.util.HashSet;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails = this.userRepository.findByUsername(email).orElse(null);

        if (userDetails == null) {
            throw new UsernameNotFoundException("Non-existent user.");
        }

        return userDetails;
    }

    @Override
    public boolean registerUser(UserServiceModel userServiceModel) {
        this.seedRolesInDb();

        User userEntity = this.modelMapper.map(userServiceModel, User.class);
        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));
        userEntity.setUsername(userServiceModel.getEmail());

        this.setUserRole(userEntity);

        this.userRepository.save(userEntity);
        return true;
    }

    @Override
    public UserServiceModel extractUserByEmail(String email) {
        User userEntity = this.userRepository.findByUsername(email).orElse(null);

        if (userEntity == null) {
            throw new UsernameNotFoundException("Non-existent user.");
        }

        UserServiceModel userServiceModel = this.modelMapper.map(userEntity, UserServiceModel.class);
        userServiceModel.setEmail(userEntity.getUsername());

        return userServiceModel;
    }

    @Override
    public boolean editUser(UserServiceModel userServiceModel) {
        User userEntity = this.userRepository.findByUsername(userServiceModel.getEmail()).orElse(null);
        userEntity = this.modelMapper.map(userServiceModel, User.class);
        userEntity.setId(userServiceModel.getId());
        userEntity.setUsername(userServiceModel.getEmail());
        userEntity.setPassword(this.bCryptPasswordEncoder.encode(userEntity.getPassword()));

        this.userRepository.save(userEntity);

        return true;
    }

    private void seedRolesInDb() {
        if (this.roleRepository.count() == 0) {
            this.roleRepository.save(new UserRole("ROLE_ROOT"));
            this.roleRepository.save(new UserRole("ROLE_ADMIN"));
            this.roleRepository.save(new UserRole("ROLE_MODERATOR"));
            this.roleRepository.save(new UserRole("ROLE_USER"));
        }
    }

    private void setUserRole(User userEntity) {
        if (this.userRepository.count() == 0) {
            userEntity.setAuthorities(new HashSet<>(this.roleRepository.findAll()));
        } else {
            UserRole roleUser = this.roleRepository.findByAuthority("ROLE_USER").orElse(null);
            if (roleUser == null) {
                throw new IllegalArgumentException("Non-existent role.");
            }

            userEntity.getAuthorities().add(roleUser);
        }
    }
}
