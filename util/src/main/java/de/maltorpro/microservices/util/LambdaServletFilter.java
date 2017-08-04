package de.maltorpro.microservices.util;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;


public interface LambdaServletFilter extends Filter {

    default public void init(FilterConfig filterConfig) throws ServletException {}
    default public void destroy() {}
}
