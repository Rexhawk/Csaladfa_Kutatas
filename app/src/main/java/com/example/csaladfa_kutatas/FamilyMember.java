package com.example.csaladfa_kutatas;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FamilyMember {
    private static final String DATE_PATTERN = "\\d{4}-\\d{2}-\\d{2}";
    private static final Pattern pattern = Pattern.compile(DATE_PATTERN);

    private String id;
    private String firstName;
    private String lastName;
    private String birthPlace;
    private String birthDate;
    private String deathPlace;
    private String deathDate;
    private List<String> parentIds;
    private List<String> parentNames;
    private List<String> partnerIds;
    private List<String> partnerNames;
    private List<String> childIds;
    private List<String> childNames;

    // Szükséges egy üres konstruktor a Firestore számára
    public FamilyMember() {
    }

    public FamilyMember(String firstName, String lastName, String birthPlace, String birthDate,
                        String deathPlace, String deathDate, List<String> parentIds, List<String> parentNames,
                        List<String> partnerIds, List<String> partnerNames, List<String> childIds, List<String> childNames) {
        this.id = generateId();
        if (!isValidDate(birthDate)) {
            throw new IllegalArgumentException("Érvénytelen születési dátum formátum");
        }
        if (deathDate != null && !isValidDate(deathDate)) {
            throw new IllegalArgumentException("Érvénytelen halálozási dátum formátum");
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthPlace = birthPlace;
        this.birthDate = birthDate;
        this.deathPlace = deathPlace;
        this.deathDate = deathDate;
        this.parentIds = parentIds;
        this.parentNames = parentNames;
        this.partnerIds = partnerIds;
        this.partnerNames = partnerNames;
        this.childIds = childIds;
        this.childNames = childNames;
    }

    // Dátum validáció
    private boolean isValidDate(String date) {
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    // Azonosító generálása
    private String generateId() {
        Random random = new Random();
        StringBuilder idBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            if (i == 4) {
                idBuilder.append('-');
            } else {
                char letter = (char) (random.nextInt(26) + 'A');
                idBuilder.append(letter);
            }
        }
        return idBuilder.toString();
    }

    // Getterek és setterek
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        if (!isValidDate(birthDate)) {
            throw new IllegalArgumentException("Érvénytelen születési dátum formátum");
        }
        this.birthDate = birthDate;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public void setDeathPlace(String deathPlace) {
        this.deathPlace = deathPlace;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        if (deathDate != null && !isValidDate(deathDate)) {
            throw new IllegalArgumentException("Érvénytelen halálozási dátum formátum");
        }
        this.deathDate = deathDate;
    }

    public List<String> getParentIds() {
        return parentIds;
    }

    public void setParentIds(List<String> parentIds) {
        this.parentIds = parentIds;
    }

    public List<String> getParentNames() {
        return parentNames;
    }

    public void setParentNames(List<String> parentNames) {
        this.parentNames = parentNames;
    }

    public List<String> getPartnerIds() {
        return partnerIds;
    }

    public void setPartnerIds(List<String> partnerIds) {
        this.partnerIds = partnerIds;
    }

    public List<String> getPartnerNames() {
        return partnerNames;
    }

    public void setPartnerNames(List<String> partnerNames) {
        this.partnerNames = partnerNames;
    }

    public List<String> getChildIds() {
        return childIds;
    }

    public void setChildIds(List<String> childIds) {
        this.childIds = childIds;
    }

    public List<String> getChildNames() {
        return childNames;
    }

    public void setChildNames(List<String> childNames) {
        this.childNames = childNames;
    }
}