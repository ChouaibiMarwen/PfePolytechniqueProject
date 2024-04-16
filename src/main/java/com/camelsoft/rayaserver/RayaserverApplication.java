package com.camelsoft.rayaserver;

import com.camelsoft.rayaserver.Tools.Configuration.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class RayaserverApplication {

	public static void main(String[] args) {
		System.setProperty("javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
		SpringApplication.run(RayaserverApplication.class, args);
	}

}
