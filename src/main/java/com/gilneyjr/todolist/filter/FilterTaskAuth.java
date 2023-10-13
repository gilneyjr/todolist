package com.gilneyjr.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gilneyjr.todolist.user.IUserRepository;
import com.gilneyjr.todolist.user.UserModel;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getServletPath().startsWith("/task/")) {
            var authEnconded = request.getHeader("Authorization")
                .substring("Basic".length())
                .trim();
            var credentials = new String(Base64.getDecoder().decode(authEnconded)).split(":");
            var username = credentials[0];
            var password = credentials[1];

            UserModel user = this.userRepository.findByUsername(username);

            if (user == null) {
                response.sendError(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            var passwordIsValid = BCrypt.verifyer()
                .verify(password.toCharArray(), user.getPassword().toCharArray())
                .verified;

            if (!passwordIsValid) {
                response.sendError(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            request.setAttribute("userId", user.getId());
        }

        filterChain.doFilter(request, response);
    }
}
