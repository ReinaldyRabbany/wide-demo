package id.wide.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDTO {
    @NotNull
    private Long id;
    @NotBlank @Size(max = 100)
    private String name;
    @NotBlank @Size(max = 100)
    private String address;
}
