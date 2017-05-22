package com.example.umit.simplefilemanager.viewmodel;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * Created by umit on 10.05.2017.
 */

public class FileExistenceValidator {
    private final static FileExistenceValidator instance = new FileExistenceValidator();

    private FileExistenceValidator() {
    }

    public static FileExistenceValidator getInstance() {
        return instance;
    }

    public enum ValidationResult {
        EMPTY_PATH(false),
        FILE_DOES_NOT_EXIST(false),
        PERMISSION_ACCESS_ERROR(false),

        FILE(true),
        DIRECTORY(true);

        private boolean successResult;

        ValidationResult(boolean successResult) {
            this.successResult = successResult;
        }

        public boolean isSuccessResult() {
            return successResult;
        }
    }

    public ValidationResult validate(String path) {
        if (StringUtils.isEmpty(path)) {
            return ValidationResult.EMPTY_PATH;
        }
        File file = new File(path);
        if (!file.exists()) {
            return ValidationResult.FILE_DOES_NOT_EXIST;
        }
        if (!file.canRead()) {
            return ValidationResult.PERMISSION_ACCESS_ERROR;
        }
        if (!file.isDirectory()) {
            return ValidationResult.FILE;
        }
        return ValidationResult.DIRECTORY;
    }
}
