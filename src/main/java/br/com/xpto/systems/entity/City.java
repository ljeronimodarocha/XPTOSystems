package br.com.xpto.systems.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Builder
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Integer id;

    @NotNull
    @Column(unique = true)
    private Integer ibgeId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private State uf;

    @NotEmpty
    private String name;

    @NotNull
    private boolean capital;

    @Column(precision = 10, scale = 2)
    private BigDecimal lon;

    @Column(precision = 10, scale = 2)
    private BigDecimal lat;

    @NotEmpty
    private String no_accents;

    private String alternative_names;

    @NotEmpty
    private String microregion;

    @NotEmpty()
    private String mesoregion;


}
