package pl.rafiki.typer.batchconfig;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pl.rafiki.typer.match.MatchRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private JobRepository jobRepository;

    private final String TEMP_STORAGE = "/app/batchfiles";


    @PostMapping("/admin/importData")
    public void importCsvToDBJob(@RequestParam("file") MultipartFile multipartFile) {

        try {
            String originalFileName = multipartFile.getOriginalFilename();
            String fullPath = TEMP_STORAGE + "/" + originalFileName;

            File fileToImport = new File(fullPath);
            multipartFile.transferTo(fileToImport);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("fullPathFileName", fullPath)
                    .addLong("startAt", System.currentTimeMillis()).toJobParameters();

            JobExecution execution = jobLauncher.run(job, jobParameters);

            if (execution.getExitStatus().equals(ExitStatus.COMPLETED)) {
                Files.deleteIfExists(Paths.get(fullPath));
            }

        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException | IOException e) {
            e.printStackTrace();
        }
    }
}
