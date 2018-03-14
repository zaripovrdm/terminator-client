package ru.raiffeisen.terminator.client;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * @author Roman Zaripov
 */
public class Main implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext context) throws ServletException {
		
		AnnotationConfigWebApplicationContext coreCtx = new AnnotationConfigWebApplicationContext();
		coreCtx.register(
			CoreConfiguration.class, 
			JmsConfiguration.class,
			DataSourceConfiguration.class, 
			ClientBundleFlow.class,
			ClientFlow.class			
		);
		ContextLoaderListener listener = new ContextLoaderListener(coreCtx);
		context.addListener(listener);
	}
}