/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcbst.benefitchange.config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


public class CORSFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		  HttpServletResponse res = (HttpServletResponse) response;
		  
		  res.setHeader("Access-Control-Allow-Origin", "*");
		  res.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
		  res.setHeader("Access-Control-Allow-Credentials", "true");
		  res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		  res.setHeader("Access-Control-Max-Age", "1209600");
	      
	      chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
		// Auto-generated method stub		
	}
}