package ru.gb.gbshopmart.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.gb.gbshopmart.security.validation.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldMatch(first = "password", second = "matchingPassword", message = "Пароли должны совпадать")
public class UserDto {
    @JsonIgnore
    private Long id;
    @NotBlank
    @Size(min = 3, message = "username length must be greater than 2 symbols")
    private String username;
    @NotNull(message = "is required")
    @Size(min = 8, message = "required 8 symbols")
    private String password;
    @NotNull(message = "is required")
    @Size(min = 8, message = "required 8 symbols")
    private String matchingPassword;
    @NotBlank(message = "is required")
    private String firstname;
    @NotBlank(message = "is required")
    private String lastname;
    @Email
    @NotBlank(message = "is required")
    private String email;
    @NotBlank(message = "is required")
    @Size(min = 5, message = "min 5 symbols")
    private String phone;
}