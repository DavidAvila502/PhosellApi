package com.dev.phosell.session.application.dto;

import com.dev.phosell.authentication.application.dto.RegisterClientDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SessionAndClientInsertDto {

//    Client info ----

    @NotNull(message = "The fullName can't be null")
    @NotBlank(message = "The fullName can't be blank")
    private String fullName;

    @NotNull(message = "The email can't be null")
    @Email(message = "The email is invalid")
    private String email;

    @NotNull(message = "The password can't be null")
    @NotBlank(message = "The password can't be blank")
    private String password;

    @NotNull(message = "The phone can't be null")
    @NotBlank(message = "The phone can't be blank")
    private String phone;

    @NotNull(message = "The city can't be null")
    @NotBlank(message = "The city can't be blank")
    private String city;

//    Session info ----
//    we don't know the clientId yet
//    @NotNull(message = "The clientId can't be null")
//    private UUID clientId;

    @NotNull(message = "The sessionPackageId can't be null")
    private UUID sessionPackageId;

    @NotNull(message = "The sessionDate can't be null")
    @FutureOrPresent(message = "The sessionDate must be today or in the future")
    private LocalDate sessionDate;

    @NotNull(message = "The sessionTime can't be null")
    private LocalTime sessionTime;

    @NotBlank(message = "the location can't be blank")
    @Length(min = 15, message = "The location must to have at least 15 characters")
    private String location;

    public RegisterClientDto getRegisterClientDto(){
        return new RegisterClientDto(fullName,email,password,phone,city);
    }

    public SessionInsertDto getSessionInsertDto(){
        SessionInsertDto sessionInsert = new SessionInsertDto();
        sessionInsert.setSessionPackageId(this.sessionPackageId);
        sessionInsert.setSessionDate(this.sessionDate);
        sessionInsert.setSessionTime(this.sessionTime);
        sessionInsert.setLocation(this.location);

        return  sessionInsert;
    }
}
