package com.michaelalbuquerque.minijava.filter;
import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.michaelalbuquerque.minijava.users.IUserReposutory;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter{

    @Autowired
    private IUserReposutory userReposutory;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                var servletPath = request.getServletPath();
                if(servletPath.startsWith("/tasks/")) {
                    var authorization = request.getHeader("Authorization");

                var user_password = authorization.substring("Basic".length()).trim();

                byte[] authDecode = Base64.getDecoder().decode(user_password);

                var authString = new String(authDecode);

                String[] credentials = authString.split(":");
                String username =  credentials[0];
                String password = credentials[1];

                var user = this.userReposutory.findByUsername(username);
                if(user == null){
                    response.sendError(401, "Usuário sem autorização");
                } else {

                var passwordVefiry = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                if(passwordVefiry.verified){
                    request.setAttribute("idUser", user.getId());
                    filterChain.doFilter(request, response);
                }else {
                    response.sendError(401, "Usuário sem autorização");
                }

                }
            } else {
                filterChain.doFilter(request, response);

            }

        }

    }
