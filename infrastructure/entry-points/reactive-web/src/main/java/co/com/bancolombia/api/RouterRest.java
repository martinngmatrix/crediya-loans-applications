package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
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
        @RouterOperation(
            path = "/api/v1/solicitud",
            beanClass = Handler.class,
            beanMethod = "listLoanApplication",
            method = RequestMethod.GET,
            operation = @Operation(
                operationId = "listLoanApplication",
                tags = {"LoanApplication"},
                summary = "List loans applications",
                description = "List loans applications in the system",
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User created successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    array = @io.swagger.v3.oas.annotations.media.ArraySchema(
                        schema = @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = co.com.bancolombia.api.dto.PageResponse.class
                        )
                    )
                    )),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data", content = @io.swagger.v3.oas.annotations.media.Content(
                        mediaType = "application/json",
                        schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = co.com.bancolombia.api.dto.ErrorResponse.class)
                    )),
                }
        )),
        @RouterOperation(
            path = "/api/v1/solicitud",
            beanClass = Handler.class,
            beanMethod = "updateLoanApplicationStatus",
            method = RequestMethod.PUT,
            operation = @Operation(
                operationId = "updateLoanApplicationStatus",
                tags = {"LoanApplication"},
                summary = "Update loan application status",
                description = "Update the status of an existing loan application. Allowed values are 'Aprobado' or 'Rechazado'.",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Status update request",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                        schema = @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = co.com.bancolombia.api.dto.UpdateLoanApplicationDTO.class
                        )
                    )
                ),
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Loan application status updated successfully"),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid status value or loan application not found", content = @io.swagger.v3.oas.annotations.media.Content(
                        mediaType = "application/json",
                        schema = @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = co.com.bancolombia.api.dto.ErrorResponse.class
                        )
                    )),
                }
            )
        ),
        @RouterOperation(
            path = "/api/v1/calcular-capacidad",
            beanClass = Handler.class,
            beanMethod = "calculateDebtCapacity",
            method = RequestMethod.POST,
            operation = @Operation(
                operationId = "calculateDebtCapacity",
                tags = {"DebtCapacity"},
                summary = "Calculate debt capacity",
                description = "Calculates the debt capacity for a given user and loan request",
                requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request with userId and newLoan data",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                        schema = @io.swagger.v3.oas.annotations.media.Schema(
                            implementation = co.com.bancolombia.api.dto.CalculateDebtCapacityDTO.class
                        )
                    )
                ),
                responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "202",
                        description = "Debt capacity calculated successfully",
                        content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                example = "{ \"message\": \"Aprobado\" }"
                            )
                        )
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                        responseCode = "400",
                        description = "Invalid input data",
                        content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                implementation = co.com.bancolombia.api.dto.ErrorResponse.class
                            )
                        )
                    )
                }
            )
        )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/solicitud"), handler::createLoanApplication)
            .andRoute(GET("/api/v1/solicitud"), handler::listLoanApplication)
            .andRoute(PUT("/api/v1/solicitud"), handler::updateLoanApplicationStatus)
            .andRoute(POST("/api/v1/calcular-capacidad"), handler::calculateDebtCapacity)
            .andRoute(POST("/api/v1/rendimiento"), handler::sendBusinessPerformanceReport);
    }
}
