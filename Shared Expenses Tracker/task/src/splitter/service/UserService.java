package splitter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import splitter.entity.User;
import splitter.repository.UserRepository;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOrCreate(String name) {
        return userRepository.findByName(name).orElseGet(() -> userRepository.save(new User(name)));
    }
}
