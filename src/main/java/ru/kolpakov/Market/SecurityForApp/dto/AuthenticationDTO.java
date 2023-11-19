package ru.kolpakov.Market.SecurityForApp.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AuthenticationDTO {

    @Column(name = "login")
    @NotEmpty
    @NotNull
    @Size(min = 2, max = 70, message = "Имя должно иметь количество символов от 2 до 70")
    private String login;
    @NotNull
    @NotEmpty
    private String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
