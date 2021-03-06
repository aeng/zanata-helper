package org.zanata.sync.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Alex Eng <a href="mailto:aeng@redhat.com">aeng@redhat.com</a>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkSummary implements Serializable {
    private Long id;
    private String name;
    private String description;
    private JobSummary syncToRepoJob;
    private JobSummary syncToTransServerJob;
}
