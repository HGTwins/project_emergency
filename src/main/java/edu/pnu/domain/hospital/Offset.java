package edu.pnu.domain.hospital;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "offset")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Offset {

    @Id
    @Column(name = "care_enc_code", length = 200)
    private String careEncCode;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "care_enc_code",
        insertable = false,
        updatable = false
    )
    private BasicInfo basicInfo;
}
