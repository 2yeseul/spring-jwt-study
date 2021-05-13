package com.example.lunit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class ImageReport {

    @Id @GeneratedValue
    private Long id;

    private boolean decision; // positive - 1, negative - 0

    private float scopeIoStart;

    private float scopeIoEnd;

    private float cutOffStart;

    private float cutOffEnd;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate reportDate;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Images images;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Account account;

    @JsonManagedReference
    @OneToMany(mappedBy = "imageReport", cascade = CascadeType.ALL)
    private List<GridImageReport> gridImageReports = new ArrayList<>();
}
