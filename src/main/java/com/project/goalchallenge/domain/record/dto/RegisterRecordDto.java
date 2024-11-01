package com.project.goalchallenge.domain.record.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

public class RegisterRecordDto {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RegisterRecordRequest {

    @NotBlank(message = "Challenge record is required")
    private String textRecord;

    private MultipartFile imageRecord;

    @NotNull(message = "Record Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate recordDate;

  }

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RegisterRecordResponse {

    private String challengeName;
    private LocalDateTime recordDateTime;

    public static RegisterRecordResponse fromEntity
        (String challengeName, LocalDateTime recordDateTime) {
      return RegisterRecordResponse.builder()
          .challengeName(challengeName)
          .recordDateTime(recordDateTime)
          .build();
    }

  }
}
