package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingDTO;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.user.api.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.capgemini.wsb.fitnesstracker.user.api.UserNotFoundException;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Provide Impl
@RequiredArgsConstructor
@Service
@Slf4j
public class TrainingServiceImpl implements TrainingProvider {

    @Autowired
    private final TrainingRepository trainingRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getTraining(final Long trainingId) {
        throw new UnsupportedOperationException("Not finished yet");
    }

    @Override
    public void deleteUserTrainingByUserId(Long userId) {
        Optional<Training> first = trainingRepository.findAll().stream()
                .filter(training -> Objects.equals(training.getUser().getId(), userId))
                .findFirst();
        first.ifPresent(trainingRepository::delete);
    }

    @Override
    public List<Training> findAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public List<Training> findAllTrainingsForUser(Long userId) {
        return trainingRepository.findTrainingsByUserId(userId);
    }

    @Override
    public List<Training> findAllFinishedTrainingsAfter(LocalDateTime afterTime) {
        return trainingRepository.findTrainingsByEndTimeAfter(afterTime);
    }

    @Override
    public List<Training> findAllTrainingsByActivityType(ActivityType activityType) {
        return trainingRepository.findTrainingsByActivityType(activityType);
    }

    @Override
    public Training createTraining(TrainingDTO trainingDTO) {
        User user = userRepository.findById(trainingDTO.userId())
                .orElseThrow(() -> new UserNotFoundException(trainingDTO.userId()));

        Training training = new Training(
                user,
                trainingDTO.startTime(),
                trainingDTO.endTime(),
                trainingDTO.activityType(),
                trainingDTO.distance(),
                trainingDTO.averageSpeed()
        );

        return trainingRepository.save(training);
    }

    @Override
    public Training updateTraining(Long trainingId, Training updatedTraining) {
        Optional<Training> existingTrainingOpt = trainingRepository.findById(trainingId);
        if (existingTrainingOpt.isPresent()) {
            Training existingTraining = existingTrainingOpt.get();
            existingTraining.setStartTime(updatedTraining.getStartTime());
            existingTraining.setEndTime(updatedTraining.getEndTime());
            existingTraining.setActivityType(updatedTraining.getActivityType());
            existingTraining.setDistance(updatedTraining.getDistance());
            existingTraining.setAverageSpeed(updatedTraining.getAverageSpeed());
            return trainingRepository.save(existingTraining);
        } else {
            throw new TrainingNotFoundException(trainingId);
        }
    }


}
