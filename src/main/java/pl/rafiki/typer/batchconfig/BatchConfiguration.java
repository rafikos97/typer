package pl.rafiki.typer.batchconfig;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import pl.rafiki.typer.match.AddMatchDTO;
import pl.rafiki.typer.match.Match;
import pl.rafiki.typer.match.MatchRepository;
import pl.rafiki.typer.tournament.TournamentRepository;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class BatchConfiguration {
    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;

    @Autowired
    public BatchConfiguration(MatchRepository matchRepository, TournamentRepository tournamentRepository) {
        this.matchRepository = matchRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<AddMatchDTO> reader(@Value("#{jobParameters[fullPathFileName]}") String pathToFile) {

        return new FlatFileItemReaderBuilder<AddMatchDTO>()
                .name("matchItemReader")
                .resource(new FileSystemResource(new File(pathToFile)))
                .linesToSkip(1)
                .lineMapper(lineMapper())
                .build();
    }

    private LineMapper<AddMatchDTO> lineMapper() {
        DefaultLineMapper<AddMatchDTO> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("firstTeamName", "secondTeamName", "startDateAndTime", "tournamentCode");

        BeanWrapperFieldSetMapper<AddMatchDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setConversionService(myConversionService());
        fieldSetMapper.setTargetType(AddMatchDTO.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }

    @Bean
    public ConversionService myConversionService() {
        DefaultConversionService conversionService = new DefaultConversionService();
        DefaultConversionService.addDefaultConverters(conversionService);

        StringToLocalDateTimeConverter dateTimeConverter =  text -> LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
        conversionService.addConverter(dateTimeConverter);

        return conversionService;
    }

    @Bean
    public MatchItemProcessor processor() {
        return new MatchItemProcessor(tournamentRepository);
    }

    @Bean
    public RepositoryItemWriter<Match> writer() {
        RepositoryItemWriter<Match> writer = new RepositoryItemWriter<>();
        writer.setRepository(matchRepository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<AddMatchDTO> itemReader) {
        return new StepBuilder("step 1", jobRepository)
                .<AddMatchDTO, Match> chunk(3, transactionManager)
                .reader(itemReader)
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .faultTolerant()
                .skipPolicy(skipPolicy())
                .listener(skipListener())
                .build();
    }

    @Bean
    public Job runJob(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<AddMatchDTO> itemReader) {
        return new JobBuilder("importMatches", jobRepository)
                .flow(step1(jobRepository, transactionManager, itemReader))
                .end()
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(3);
        return asyncTaskExecutor;
    }

    @Bean
    public SkipPolicy skipPolicy() {
        return new ExceptionSkipPolicy();
    }

    @Bean
    public SkipListener skipListener() {
        return new StepSkipListener();
    }
}
