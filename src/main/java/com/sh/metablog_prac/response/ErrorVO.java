package com.sh.metablog_prac.response;

import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorVO {
    public String fieldName;
    public String message;
}
