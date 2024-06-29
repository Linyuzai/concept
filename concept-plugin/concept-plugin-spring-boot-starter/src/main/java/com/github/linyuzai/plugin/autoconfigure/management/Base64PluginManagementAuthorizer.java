package com.github.linyuzai.plugin.autoconfigure.management;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Base64PluginManagementAuthorizer implements PluginManagementAuthorizer {

    private String password;

    @Override
    public boolean unlock(String password) {
        if (this.password == null || this.password.isEmpty()) {
            return true;
        }
        byte[] decoded = Base64.getDecoder().decode(password);
        return this.password.equals(new String(decoded));
    }
}
