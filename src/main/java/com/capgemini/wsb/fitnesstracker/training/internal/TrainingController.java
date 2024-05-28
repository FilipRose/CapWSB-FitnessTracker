package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
@Slf4j
public class TrainingController {
    @Autowired
    private TrainingServiceImpl trainingService;

    @GetMapping
    public ResponseEntity<List<Training>> findAllTrainings() {
        return ResponseEntity.ok(trainingService.findAllTrainings());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Training>> findAllTrainingsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(trainingService.findAllTrainingsForUser(userId));
    }

    @GetMapping("/finished/{afterTime}")
    public ResponseEntity<List<Training>> findAllFinishedTrainingsAfter(@PathVariable String afterTime) {
        LocalDateTime afterDateTime = LocalDate.parse(afterTime).atStartOfDay();
        return ResponseEntity.ok(trainingService.findAllFinishedTrainingsAfter(afterDateTime));
    }

    @GetMapping("/activityType")
    public ResponseEntity<List<Training>> findAllTrainingByActivityType(@RequestParam ActivityType activityType) {
        return ResponseEntity.ok(trainingService.findAllTrainingsByActivityType(activityType));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Training createTraining(@RequestBody TrainingDTO trainingDTO) {
        return trainingService.createTraining(trainingDTO);
    }

    @PutMapping("/{trainingId}")
    public ResponseEntity<Training> updateTraining(@PathVariable Long trainingId, @RequestBody Training training) {
        Training updatedTraining = trainingService.updateTraining(trainingId, training);
        return ResponseEntity.ok(updatedTraining);
    }
}
