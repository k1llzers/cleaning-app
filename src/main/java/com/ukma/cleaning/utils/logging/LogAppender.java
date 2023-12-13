package com.ukma.cleaning.utils.logging;

import ch.qos.logback.core.FileAppender;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogAppender<E> extends FileAppender<E> {
    public LogAppender() {
        super();
        super.setFile("logs/myapp" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm")) + ".log");
    }
}
