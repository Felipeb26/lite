package com.bats.lite.enums;

public enum UserProps {
    ID("id"),
    NOME("nome"),
    EMAIL("email"),
    DATA("nascimento");

    private final String props;

    UserProps(String props) {
        this.props = props;
    }

    public String getProps() {
        return this.props;
    }
}
