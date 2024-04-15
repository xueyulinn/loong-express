package com.loong.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SetmealPageQueryVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long categoryId;

    private String name;

    private BigDecimal price;

    private Integer status;

    private String description;

    private String image;

    private String categoryName;

    private LocalDateTime updateTime;

}
