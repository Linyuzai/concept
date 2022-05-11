package com.github.linyuzai.connection.loadbalance.websocket.servlet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;

@Getter
@AllArgsConstructor
public class PrefixUrlPathHelper extends UrlPathHelper {

    private String prefix;

    @Override
    public @NonNull String resolveAndCacheLookupPath(@NonNull HttpServletRequest request) {
        String path = super.resolveAndCacheLookupPath(request);
        if (path.startsWith(prefix)) {
            return prefix + "**";
        }
        return path;
    }
}
