package com.example.practice;

import com.example.practice.config.audit.SpringSecurityAuditorAware;
import com.example.practice.dsa.SearchAlg;
import com.example.practice.dsa.SortingAlg;
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
//		int[] arr = new int[1000];
//		int target = 900;
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

		int[] unsortedArray = {2,3,1,4,6,5,9,8};
		for (int j : unsortedArray) {
			System.out.print(" "+j);
		}
		//Bubble sort
		System.out.println();
		int[] sortedArray = SortingAlg.bubbleSort(unsortedArray);
        for (int j : sortedArray) {
			System.out.print(" "+j);
        }
	}

	@Bean
	public AuditorAware<String> auditorAware() {
		return new SpringSecurityAuditorAware();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		registry.addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/");

		registry.addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/");

	}

}
