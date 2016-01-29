package org.visallo.web;

import com.v5analytics.webster.HandlerChain;
import com.v5analytics.webster.RequestResponseHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationHandler implements RequestResponseHandler {
    public static final String LOGIN_PATH = "/login";
    private static final String HEADER_X_FORWARDED_FOR = "x-forwarded-for";

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, HandlerChain chain) throws Exception {
        String currentUserId = CurrentUser.getUserId(request);
        if (currentUserId != null) {
            chain.next(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    public static String getRemoteAddr(HttpServletRequest request) {
        String xForwardedFor = request.getHeader(HEADER_X_FORWARDED_FOR);
        if (xForwardedFor != null && xForwardedFor.trim().length() > 0) {
            return xForwardedFor;
        }
        return request.getRemoteAddr();
    }
}
