package pl.rafiki.typer.batchconfig;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

public interface StringToLocalDateTimeConverter extends Converter<String, LocalDateTime> {
}
