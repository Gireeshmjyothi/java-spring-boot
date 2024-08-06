package com.example.practice;

import com.example.practice.config.audit.SpringSecurityAuditorAware;
import com.example.practice.dsa.LinkedList;
import com.example.practice.dsa.SearchAlg;
import com.example.practice.dsa.Stack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@ComponentScan(basePackages = {
		"org.springframework.boot.context.embedded.tomcat",
		"com.example.practice"
})
@EntityScan(basePackages = "com.example.practice")
@EnableJpaRepositories(basePackages = "com.example.practice")
@Slf4j
public class PracticeApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(PracticeApplication.class, args);
		int[] arr = {1,2,3,5,6,7,8,23,30};
		int target = 3;

		//linear search
		int linearResult = SearchAlg.linearSearch(arr, target);
		if(linearResult != -1){
			log.info("Linear value found at index :{}", linearResult);
		}else {
			log.info("No linear value found for the given target.");
		}

		//binary search
		int binarySearch =  SearchAlg.binarySearch(arr, target);
		if(binarySearch != -1){
			log.info("Binary value found at index :{}", binarySearch);
		}else {
			log.info("No binary value found for the given target.");
		}

		//Recursive binary search
		int binarySearchWithRecursive =  SearchAlg.binarySearchWithRecursive(arr, target, 0, arr.length-1);
		if(binarySearch != -1){
			log.info("BinaryRecursive value found at index :{}", binarySearchWithRecursive);
		}else {
			log.info("No binaryRecursive value found for the given target.");
		}

		LinkedList list =  new LinkedList();
		list.insert(10);
		list.insert(12);
		list.insert(2);
		list.insertAtStart(23);

		list.insertAt(0,30);

		list.deleteAt(2);

//		list.show();

		Stack l = new Stack();
		l.push(10);
		l.push(23);
		l.push(12);

		System.out.println(l.peek());
		System.out.println(l.pop());


		l.show();

	}

	@Bean
	public AuditorAware<String> auditorAware() {
		return new SpringSecurityAuditorAware();
	}

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");

	}

}
