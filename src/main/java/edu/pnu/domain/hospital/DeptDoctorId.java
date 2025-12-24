package edu.pnu.domain.hospital;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DeptDoctorId implements Serializable {
	@Column(name = "care_enc_code")
    private String careEncCode;
	
	@Column(name = "dept_code")
    private String deptCode;
}