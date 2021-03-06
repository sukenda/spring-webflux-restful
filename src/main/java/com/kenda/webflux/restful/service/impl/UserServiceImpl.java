package com.kenda.webflux.restful.service.impl;

import com.kenda.webflux.restful.config.JWTTokenProvider;
import com.kenda.webflux.restful.entity.User;
import com.kenda.webflux.restful.exception.UserException;
import com.kenda.webflux.restful.model.TokenRequest;
import com.kenda.webflux.restful.model.UserRequest;
import com.kenda.webflux.restful.repository.UserRepository;
import com.kenda.webflux.restful.service.UserService;
import com.kenda.webflux.restful.service.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService {

    private final JWTTokenProvider tokenProvider;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ValidationService validationService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).block();
        Assert.notNull(user, "User must not be null!");

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    @Override
    public Mono<User> token(TokenRequest request) {
        validationService.validate(request);

        return userRepository.findByUsername(request.getUsername())
                .switchIfEmpty(Mono.error(new UserException("Pastikan username dan password anda benar")))
                .flatMap(user -> {
                    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                        user.setRefreshToken(tokenProvider.generateToken(user, user.getRoles(), true));
                        user.setAccessToken(tokenProvider.generateToken(user, user.getRoles(), false));

                        return userRepository.save(user).flatMap(Mono::just);
                    }

                    return Mono.error(new UserException("Pastikan username dan password anda benar"));
                });
    }

    @Override
    public Mono<User> refreshToken(String refreshToken) {
        validationService.validate(refreshToken);

        return userRepository.findByRefreshToken(refreshToken)
                .defaultIfEmpty(new User())
                .flatMap(user -> {
                    if (user.getRefreshToken() == null) {
                        return Mono.error(new UserException("Pastikan refresh token yang anda kirim benar"));
                    }

                    user.setRefreshToken(tokenProvider.generateToken(user, user.getRoles(), true));
                    user.setAccessToken(tokenProvider.generateToken(user, user.getRoles(), false));

                    return userRepository.save(user).flatMap(Mono::just);
                });
    }

    @Override
    public Mono<User> signup(UserRequest request) {
        validationService.validate(request);

        return userRepository.findByUsername(request.getUsername())
                .defaultIfEmpty(new User())
                .flatMap(current -> {
                    if (current.getId() != null) {
                        return Mono.error(new UserException("Username sudah ada, silahkan gunakan username lain"));
                    }

                    User user = new User();
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    user.setUsername(request.getUsername());
                    user.setProfileName(request.getProfileName());
                    user.setEmail(request.getEmail());
                    user.setEnabled(true);
                    user.setRoles(request.getRoles());

                    String token = tokenProvider.generateToken(user, user.getRoles(), false);
                    user.setAccessToken(token);

                    String refresh = tokenProvider.generateToken(user, user.getRoles(), true);
                    user.setRefreshToken(refresh);

                    return userRepository.save(user).flatMap(Mono::just);
                });
    }

    /**
     * This used just for testing
     *
     * @param refreshToken from this user
     * @return User Deleted
     */
    @Override
    public Mono<User> delete(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken)
                .flatMap(user -> userRepository.delete(user).then(Mono.just(user)));
    }
}
