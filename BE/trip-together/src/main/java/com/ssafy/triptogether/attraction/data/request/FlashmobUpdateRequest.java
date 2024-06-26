package com.ssafy.triptogether.attraction.data.request;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FlashmobUpdateRequest(
	@NotBlank String title,
	@NotNull @JsonProperty("start_time") LocalDateTime startAt,
	@NotNull @JsonProperty("max_users") Integer maxMemberCount
) {
}
