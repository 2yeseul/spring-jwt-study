package com.example.lunit.model;

import com.example.lunit.dto.account.InfoForm;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder @AllArgsConstructor @NoArgsConstructor
@DynamicUpdate
public class Account {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate signUpDate;

    private String address;
    private String telephone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonManagedReference
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<ImageReport> imageReportSet = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<Images> images = new HashSet<>();

    public void modifyPassword(String password) {
        this.password = password;
    }

    public void modifyInfos(InfoForm infoForm) {
        this.address = infoForm.getAddress();
        this.telephone = infoForm.getTelephone();
    }
}
