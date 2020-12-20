package com.diamondfire.helpbot.sys.externalfile;

import java.io.File;

public enum ExternalFile {
    DB(new ExternalFileBuilder()
            .isDirectory(false)
            .setName("db")
            .setFileType("json")
            .buildFile()),
    DB_COMPARE(new ExternalFileBuilder()
            .isDirectory(false)
            .setName("db_last")
            .setFileType("json")
            .buildFile()),
    OTHER_CACHE_DIR(new ExternalFileBuilder()
            .isDirectory(true)
            .setName("other_cache")
            .buildFile()),
    IMAGES_DIR(new ExternalFileBuilder()
            .isDirectory(true)
            .setName("images")
            .buildFile()),
    SAM_DIR(new ExternalFileBuilder()
            .isDirectory(true)
            .setName("samquotes")
            .buildFile()),
    CONFIG(new ExternalFileBuilder()
            .isDirectory(false)
            .setName("config")
            .setFileType("json")
            .buildFile()),
    DISABLED_COMMANDS(new ExternalFileBuilder()
            .isDirectory(false)
            .setName("disabled_commands")
            .setFileType("txt")
            .buildFile());
    
    private final File file;
    
    ExternalFile(File file) {
        this.file = file;
    }
    
    public File getFile() {
        return file;
    }
    
    
}
