package com.example.lunit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class Images {

    @Id @GeneratedValue
    private Long id;

    private String imagePath;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate uploadDate;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Account account;

    @JsonManagedReference
    @OneToMany(mappedBy = "images", cascade = CascadeType.ALL)
    private Set<ImageReport> imageReports = new HashSet<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "images", cascade = CascadeType.ALL)
    private Set<GridImageReport> gridImageReports = new HashSet<>();
}
