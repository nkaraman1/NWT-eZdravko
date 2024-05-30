package ba.unsa.etf.nwt.APIGateway;

import ba.unsa.etf.nwt.APIGateway.GatewayFilter.AuthenticationFilter;
import ba.unsa.etf.nwt.APIGateway.GatewayFilter.CustomGatewayFilterFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder,
										   CustomGatewayFilterFactory customGatewayFilterFactory,
										   AuthenticationFilter authenticationFilter) {
		return builder.routes()
				.route("UserManagementService", r -> r.path("/UserManagementService/**")
						.filters(f -> f.stripPrefix(1)
								.filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
								.filter(customGatewayFilterFactory.apply(new CustomGatewayFilterFactory.Config("UserManagementService"))))
						.uri("lb://UserManagementService"))
				.route("NewsService", r -> r.path("/NewsService/**")
						.filters(f -> f.stripPrefix(1)
								.filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
								.filter(customGatewayFilterFactory.apply(new CustomGatewayFilterFactory.Config("NewsService"))))
						.uri("lb://NewsService"))
				.route("ForumService", r -> r.path("/ForumService/**")
						.filters(f -> f.stripPrefix(1)
								.filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
								.filter(customGatewayFilterFactory.apply(new CustomGatewayFilterFactory.Config("ForumService"))))
						.uri("lb://ForumService"))
				.route("PatientService", r -> r.path("/PatientService/**")
						.filters(f -> f.stripPrefix(1)
								.filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
								.filter(customGatewayFilterFactory.apply(new CustomGatewayFilterFactory.Config("PatientService"))))
						.uri("lb://PatientService"))
				.route("SurveyService", r -> r.path("/SurveyService/**")
						.filters(f -> f.stripPrefix(1)
								.filter(authenticationFilter.apply(new AuthenticationFilter.Config()))
								.filter(customGatewayFilterFactory.apply(new CustomGatewayFilterFactory.Config("SurveyService"))))
						.uri("lb://SurveyService"))
				.build();
	}
	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
