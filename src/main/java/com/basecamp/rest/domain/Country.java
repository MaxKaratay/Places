package com.basecamp.rest.domain;

import com.basecamp.rest.validation.CountryCode;
import com.basecamp.rest.validation.CountryName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    @JsonView(Views.Model.class)
    private Integer id;

    @NotBlank(message = "Fill country name")
    @CountryName
    @JsonView({Views.Public.class, Views.Attribute.class, Views.Model.class})
    private String name;

    @NotBlank(message = "Fill country code")
    @CountryCode
    @JsonView(Views.Model.class)
    private String code;

    @OneToMany(
            mappedBy = "country",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    @JsonView(Views.Model.class)
    private List<City> cities = new ArrayList<>();
}
