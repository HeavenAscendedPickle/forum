package telran.java2022.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.java2022.security.context.SecurityContext;
import telran.java2022.security.context.User;

@Component
@RequiredArgsConstructor
@Order(20)
public class AdminFilter implements Filter {

    final SecurityContext context;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
	    throws IOException, ServletException {
	HttpServletRequest request = (HttpServletRequest) req;
	HttpServletResponse response = (HttpServletResponse) resp;
	if (checkEndPoint(request.getMethod(), request.getServletPath())) {
	    User userAccount = context.getUser(request.getUserPrincipal().getName());
	    if (!userAccount.getRoles().contains("Administrator".toUpperCase())) {
		response.sendError(403);
		return;
	    }
	}
	chain.doFilter(request, response);
    }

    private boolean checkEndPoint(String method, String servletPath) {
	return servletPath.matches("/account/user/\\w+/role/\\w+/?");
    }

}
