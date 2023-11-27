package pl.rafiki.typer.batchconfig;

import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;
import pl.rafiki.typer.match.MatchDTO;

public class StepSkipListener implements SkipListener<MatchDTO, Number> {

    Logger logger = LoggerFactory.getLogger(StepSkipListener.class);

    @Override
    public void onSkipInRead(Throwable throwable) {
        logger.info("A failure on read {} ", throwable.getMessage());
    }

    @Override
    public void onSkipInWrite(Number item, Throwable throwable) {
        logger.info("A failure on write {}, {}", throwable.getMessage(), item);
    }

    @SneakyThrows
    @Override
    public void onSkipInProcess(MatchDTO matchDTO, Throwable throwable) {
//        logger.info("Item {} was skipped due to the exception: {}", new ObjectMapper().findAndRegisterModules().writeValueAsString(matchDTO),
//                throwable.getMessage());
        logger.info("Item {} was skipped due to the exception: {}", matchDTO.toString(),
                throwable.getMessage());
    }
}
