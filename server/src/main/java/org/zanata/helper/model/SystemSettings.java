package org.zanata.helper.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Alex Eng <a href="mailto:aeng@redhat.com">aeng@redhat.com</a>
 */
@Getter
@AllArgsConstructor
public class SystemSettings implements Serializable {
    /**
     * Must have read write access
     * i.e /tmp/zanataHelperRoot
     */
    @NotEmpty
    private String storageDir;

    private boolean deleteJobDir = true;

    @NotNull
    private List<String> fieldsNeedEncryption = new ArrayList<>();
}
