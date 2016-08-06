/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upv.dsic.quep.filters;

import es.upv.dsic.quep.beans.LoginBean;
import es.upv.dsic.quep.beans.NavigationBean;
import java.io.IOException;
import javax.faces.application.ResourceHandler;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author agna8685
 */
public class LoginFilter implements Filter {

    @Inject
    private LoginBean loginBean;
    @Inject
    private NavigationBean navigationBean;

    private static final String AJAX_REDIRECT_XML = "";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String loginURL = request.getContextPath() + navigationBean.toLogin();
        String userURL = request.getContextPath() + navigationBean.toUser();

        boolean loggedIn = (loginBean != null && loginBean.isLoggedIn());
        boolean loginRequest=false;
        if(request.getRequestURI().equals(loginURL) || 
                request.getRequestURI().equals(request.getContextPath()) || 
                request.getRequestURI().equals(request.getContextPath() + "/"))
            loginRequest = true;
        boolean resourceRequest = request.getRequestURI().startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER + "/");
        boolean ajaxRequest = "partial/ajax".equals(request.getHeader("Faces-Request"));

        if (loggedIn || loginRequest || resourceRequest) {
            if (loggedIn && loginRequest) {
                response.sendRedirect(userURL);
            } else if (!resourceRequest) {
                response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
            }
            chain.doFilter(request, response);
        } else if (ajaxRequest) {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().printf(AJAX_REDIRECT_XML, loginURL);
        } else {
            response.sendRedirect(loginURL);
        }

    }

    @Override
    public void destroy() {
//
    }

}
