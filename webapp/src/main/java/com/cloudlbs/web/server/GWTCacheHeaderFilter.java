package com.cloudlbs.web.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Applies appropriate cache headers to requests for GWT resources with the
 * *.cache.* or *.nocache.* naming patterns.
 * 
 * @author danmascenik
 * 
 */
public class GWTCacheHeaderFilter implements Filter {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String uri = request.getRequestURI();
        log.debug("Request URI: " + uri);
        if (uri.matches(".*\\.cache\\..*")) {
            // Set expiration to 60 mins
            response.setHeader("Cache-Control", "max-age=3600, must-revalidate");
            log.debug("Set cachable headers");
        } else if (uri.matches(".*\\.nocache\\..*")) {
            // Never cache
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Expires", "-1");
            log.debug("Set uncachable headers");
        } else {
            log.debug("Not applying any cache control headers");
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}
