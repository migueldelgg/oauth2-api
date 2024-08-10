package github.migueldelgg.springsecurity.controller.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
