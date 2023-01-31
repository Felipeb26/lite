package com.bats.lite.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Roles {

	ROOT("root"),
	USER("user"),
	ADMIN("admin");

	private final String role;
}
