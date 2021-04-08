package com.ysell.config.jwt.service;

import com.ysell.common.converters.enums.StringToEnumConverter;
import com.ysell.config.jwt.models.enums.Client;
import com.ysell.modules.common.exceptions.YSellRuntimeException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * @author tchineke
 * @since 07 April, 2021
 */
@Component
public class ClientService {

    private static final Map<Client, String> clientCredential = Collections.singletonMap(Client.YSELL_MOBILE, "ysell_mobile");


    public void validateClientId(String clientId) {
        StringToEnumConverter<Client> clientStringToEnumConverter = new StringToEnumConverter<>(Client.class);
        if (!clientStringToEnumConverter.isValid(clientId))
            throw new YSellRuntimeException(String.format("Invalid client id '%s'", clientId));
    }


    public void validateClient(String clientId, String clientSecret) {
        validateClientId(clientId);
        Client client = new StringToEnumConverter<>(Client.class).convert(clientId);
        String secret = clientCredential.getOrDefault(client, null);

        if (!clientSecret.equals(secret))
            throw new YSellRuntimeException("Invalid Client Credentials");
    }
}