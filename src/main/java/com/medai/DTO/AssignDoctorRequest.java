package com.medai.DTO;

import lombok.Data;

@Data
public class AssignDoctorRequest {
    private Long patientId;
    private Long doctorId;
}