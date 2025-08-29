package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import io.swagger.v3.oas.annotations.Operation;

@Configuration
public class RouterRest {
    @Bean
    @RouterOperations({
        @RouterOperation(
            path = "/api/v1/solicitud",
            beanClass = Handler.class,
            beanMethod = "createLoanApplication",
            method = RequestMethod.POST,
            operation = @Operation(
                operationId = "createLoanApplication",
                tags = {"LoanApplication"},
                summary = "Create a new loan application",
                description = "Creates a new loan application in the system",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Loan application to create",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                        schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = co.com.bancolombia.api.dto.CreateLoanApplicationDTO.class)
                    )
                ),
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User created successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data", content = @io.swagger.v3.oas.annotations.media.Content(
                        mediaType = "application/json",
                        schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = co.com.bancolombia.api.dto.ErrorResponse.class)
                    )),
                }
        )),
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/solicitud"), handler::createLoanApplication);
    }
}
