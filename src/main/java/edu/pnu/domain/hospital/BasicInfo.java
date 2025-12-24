package edu.pnu.domain.hospital;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "basic_info")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasicInfo {

    @Id
    @Column(name = "care_enc_code", length = 200)
    private String careEncCode;

    @Column(name = "institution_name", length = 200, nullable = false)
    private String institutionName;

    @Column(name = "address", length = 200, nullable = false)
    private String address;

    @Column(name = "call", length = 200, nullable = false)
    private String call;

    @Column(name = "homepage", length = 200)
    private String homepage;

    // ===== 연관관계 (조회용) =====
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "sido_code",
        insertable = false,
        updatable = false
    )
    private SidoCode sidoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "type_code",
        insertable = false,
        updatable = false
    )
    private TypeCode typeCode;
}