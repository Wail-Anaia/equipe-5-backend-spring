package ma.jobintech.projetfilrouge.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ADMIN,
    ENCADRANT,
    ETUDIANT;
    
    @JsonCreator
    public static Role from(String value) {
        return Role.valueOf(value.toUpperCase());
    }
}