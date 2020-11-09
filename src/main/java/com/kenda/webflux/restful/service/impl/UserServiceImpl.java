package com.kenda.webflux.restful.service.impl;

import com.kenda.webflux.restful.config.JWTTokenProvider;
import com.kenda.webflux.restful.entity.User;
import com.kenda.webflux.restful.exception.UserException;
import com.kenda.webflux.restful.model.TokenRequest;
import com.kenda.webflux.restful.model.UserRequest;
import com.kenda.webflux.restful.repository.UserRepository;
import com.kenda.webflux.restful.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username).block();
        Assert.notNull(user, "User must not be null!");
        if (user.getUsername() == null) throw new UsernameNotFoundException("Username atau password tidak valid.");

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), user.getAuthorities());
    }

    @Override
    public Mono<User> token(TokenRequest param) {
        return userRepository.findByUsername(param.getUsername())
                .switchIfEmpty(Mono.error(new UserException("Pastikan username dan password anda benar", HttpStatus.BAD_REQUEST.name())))
                .flatMap(user -> {
                    if (passwordEncoder.matches(param.getPassword(), user.getPassword())) {
                        user.setRefreshToken(tokenProvider.generateToken(user, user.getRoles(), true));
                        user.setAccessToken(tokenProvider.generateToken(user, user.getRoles(), false));

                        return userRepository.save(user).flatMap(Mono::just);
                    }

                    return Mono.error(new UserException("Pastikan username dan password anda benar", HttpStatus.BAD_REQUEST.name()));
                });
    }

    @Override
    public Mono<User> refreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken)
                .defaultIfEmpty(new User())
                .flatMap(user -> {
                    if (user.getRefreshToken() == null) {
                        return Mono.error(new UserException("Pastikan refresh token yang anda kirim benar", HttpStatus.BAD_REQUEST.name()));
                    }

                    user.setRefreshToken(tokenProvider.generateToken(user, user.getRoles(), true));
                    user.setAccessToken(tokenProvider.generateToken(user, user.getRoles(), false));

                    return userRepository.save(user).flatMap(Mono::just);
                });
    }

    @Override
    public Mono<User> signup(UserRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .defaultIfEmpty(new User())
                .flatMap(current -> {
                    if (current.getId() != null) {
                        return Mono.error(new UserException("Username sudah ada, silahkan gunakan username lain", HttpStatus.BAD_REQUEST.name()));
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
}
