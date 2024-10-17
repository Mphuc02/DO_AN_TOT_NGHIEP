package dev.common.model;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProcessedImageData {
    private String decodedImg;
    private List<String> diseases;
    private UUID owner;
}