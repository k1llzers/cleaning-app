package com.ukma.cleaning.address;

import com.ukma.cleaning.user.UserEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@NoArgsConstructor
@Data
@SQLDelete(sql = "UPDATE address SET deleted = true WHERE id=?")
@FilterDef(name = "deletedAddressFilter", parameters = @ParamDef(name = "deleted", type = Boolean.class))
@Filter(name = "deletedProductFilter", condition = "deleted = :isDeleted")
@Table(name = "address")
public class AddressEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "house_number", nullable = false)
    private String houseNumber;

    @Column(name = "flat_number")
    private String flatNumber;

    @Column(name = "zip")
    private String zip;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity user;

    @Column(name = "user_id", insertable=false, updatable=false)
    private Long userId;
}
