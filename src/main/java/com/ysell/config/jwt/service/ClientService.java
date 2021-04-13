package com.ysell.config.jwt.service;

import com.google.common.collect.ImmutableMap;
import com.ysell.common.converters.enums.StringToEnumConverter;
import com.ysell.config.jwt.models.enums.Client;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author tchineke
 * @since 07 April, 2021
 */
@Component
@RequiredArgsConstructor
public class ClientService {

    private final Map<Client, String> clientCredential = ImmutableMap.of(
            Client.YSELL_MOBILE, "$2a$10$19NGHbb74iOKbViwQ9Sxuu6xZOpvbrK65v2DhMldlFPvmRUjYlCgK"
    );

    private final PasswordEncoder passwordEncoder;


    public void validateClientId(String clientId) {
        StringToEnumConverter<Client> clientStringToEnumConverter = new StringToEnumConverter<>(Client.class);
        if (!clientStringToEnumConverter.isValid(clientId))
            throw new YSellRuntimeException(String.format("Invalid client id '%s'", clientId));
    }


    public void validateClient(String clientId, String clientSecret) {
        validateClientId(clientId);

        Client client = new StringToEnumConverter<>(Client.class).convert(clientId);
        String hash = clientCredential.getOrDefault(client, null);

        if (!passwordEncoder.matches(clientSecret, hash))
            throw new YSellRuntimeException("Invalid Client Credentials");
    }
}