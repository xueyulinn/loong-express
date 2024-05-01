package com.loong.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * encapsulate the result of paging query
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResult implements Serializable {

    // total number of records
    private long total; 

    // records of the current page
    private List records; 

}
