package com.ukma.cleaning.utils.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import org.slf4j.Marker;

public class LogLayout extends LayoutBase<ILoggingEvent> {

  public String doLayout(ILoggingEvent event) {
    StringBuffer sbuf = new StringBuffer(128);
    sbuf.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    sbuf.append(" | ");
    sbuf.append(event.getLevel());
    sbuf.append(" [");
    sbuf.append(event.getThreadName());
    sbuf.append("] ");
    List<Marker> markers = event.getMarkerList();
    if (markers != null && !markers.isEmpty()) {
      String m = markers.stream().map(Marker::getName).collect(Collectors.joining(" "));
      sbuf.append(m);
      sbuf.append(' ');
    }
    sbuf.append(event.getLoggerName());
    sbuf.append(" - ");
    sbuf.append(event.getFormattedMessage());
    Map<String, String> mdc = event.getMDCPropertyMap();
    if (mdc != null && !mdc.isEmpty()) {
      sbuf.append(" with parameter ");
      sbuf.append(event.getMDCPropertyMap().entrySet().stream().map(entry -> entry.getKey() + "=" + entry.getValue())
              .collect(Collectors.joining(", ", "{", "}")));
    }
    sbuf.append(CoreConstants.LINE_SEPARATOR);
    return sbuf.toString();
  }
}