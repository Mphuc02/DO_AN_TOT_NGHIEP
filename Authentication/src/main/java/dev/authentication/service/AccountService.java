package dev.authentication.service;

import dev.authentication.constant.ValueConstant;
import dev.authentication.entity.Account;
import dev.authentication.model.request.AuthenticationRequest;
import dev.authentication.model.request.RegisterAccountRequest;
import dev.authentication.model.response.AuthenticationResponse;
import dev.authentication.repository.AccountRepository;
import dev.authentication.util.AccountUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final JwtService jwtService;
    private final AccountUtil accountUtil;
    private final AuthenticationManager authenticationManager;

    @Value(ValueConstant.JWT.ACCESS_TOKEN_EXPIRATION)
    public int ACCESS_TOKEN_EXPIRATION;

    public void register(RegisterAccountRequest request){
        Account account = accountUtil.mapFromRegister(request);
        accountRepository.save(account);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassWord()));
            Account account = (Account) authentication.getPrincipal();
            String accessToken = jwtService.generateToken(account, ACCESS_TOKEN_EXPIRATION);

            return new AuthenticationResponse(accessToken, "");
        } catch (AuthenticationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}