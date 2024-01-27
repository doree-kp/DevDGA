package doree.devg.service;

import doree.devg.entity.User;
import doree.devg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        return userOptional.orElse(null);
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Le nom d'utilisateur existe déjà. Veuillez en choisir un autre.");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("L'email existe déjà. Veuillez en choisir un autre.");
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long userId, User user) {
        Optional<User> existingUserOptional = userRepository.findById(userId);

        if (existingUserOptional.isPresent()) {
            User existingUser = existingUserOptional.get();

            existingUser.setUsername(user.getUsername());
            existingUser.setPassword(user.getPassword());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmail(user.getEmail());

            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("Utilisateur non trouvé avec l'ID : " + userId);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
