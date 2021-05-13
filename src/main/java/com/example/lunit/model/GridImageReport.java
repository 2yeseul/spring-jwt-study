package com.example.lunit.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
public class GridImageReport {

    @Id @GeneratedValue
    private Long id;

    // i_** : Intratumoral TIL destiny
    private float i_min;
    private float i_avg;
    private float i_max;

    // s_** : Stromal TIL destiny
    private float s_min;
    private float s_avg;
    private float s_max;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ImageReport imageReport;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Images images;
}
