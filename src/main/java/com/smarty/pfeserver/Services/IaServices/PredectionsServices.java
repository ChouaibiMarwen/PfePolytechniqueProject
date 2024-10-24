package com.smarty.pfeserver.Services.IaServices;

import com.smarty.pfeserver.Enum.Project.MissionStatusEnum;
import com.smarty.pfeserver.Enum.Project.SoftSkillsEnum;
import com.smarty.pfeserver.Enum.TransactionEnum;
import com.smarty.pfeserver.Enum.User.RoleEnum;
import com.smarty.pfeserver.Models.Project.BoostBudgetRequest;
import com.smarty.pfeserver.Models.Project.Mission;
import com.smarty.pfeserver.Models.Project.Task;
import com.smarty.pfeserver.Models.User.users;
import com.smarty.pfeserver.Services.Project.BoostBudgetRequestService;
import com.smarty.pfeserver.Services.Project.MissionService;
import com.smarty.pfeserver.Services.Project.TransactionService;
import com.smarty.pfeserver.Services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PredectionsServices {
    @Autowired
    private UserService userService;
    @Autowired
    private MissionService missionService;
    @Autowired
    private OpenAIService openAIService;
    @Autowired
    private BoostBudgetRequestService boostBudgetRequestService;
    @Autowired
    private TransactionService transactionService;
    // Main method to decide mission budget and participants
    public String decideMissionParticipantsBudget(String missiondescription, Integer participantsnumber) {
        // Fetch all missions and technicians
        List<Mission> missions = this.missionService.findAll();
        List<users> technicians = this.userService.allusersByRole(RoleEnum.ROLE_TECHNICIEN);

        // Step 1: Budget prediction based on past missions
        double predictedBudget = predictBudget(missiondescription, missions);

        // Step 2: Select the best-matched participants based on soft skills and tasks
        List<users> selectedParticipants = selectBestParticipants(technicians, missiondescription, participantsnumber);

        // Step 3: Generate a readable explanation of the results
        return generateHumanReadableExplanation(predictedBudget, selectedParticipants);
    }

    // Budget prediction logic based on previous missions
    private double predictBudget(String missiondescription, List<Mission> missions) {
        double totalBudget = 0.0;
        int missionCount = 0;

        // Calculate the adjusted budget for similar missions
        for (Mission mission : missions) {
            double baseBudget = mission.getBudget();

            // Adjust budget based on approved BoostBudgetRequests
            if (mission.getRequestBoostBudgetRequest() != null && !mission.getRequestBoostBudgetRequest().isEmpty()) {
                for (BoostBudgetRequest boostRequest : mission.getRequestBoostBudgetRequest()) {
                    if (boostRequest.getStatus() == TransactionEnum.APPROVED) {
                        baseBudget += boostRequest.getAmount();
                    }
                }
            }

            // Add the adjusted base budget to the total
            totalBudget += baseBudget;
            missionCount++;
        }

        // Calculate average budget from past missions
        double averageBudget = missionCount > 0 ? totalBudget / missionCount : 0.0;

        // Use OpenAI to predict the budget based on the description and the average budget
        String budgetPrompt = "Based on past missions with similar descriptions, the average budget is " + averageBudget
                + ". Predict the budget for a mission described as: " + missiondescription;

        // Call OpenAI's prediction method
        String predictedBudgetString = openAIService.getPrediction(budgetPrompt);
        double predictedBudget = parseBudget(predictedBudgetString.trim());

        return predictedBudget;
    }

    // Method to parse the budget from the OpenAI response
    private double parseBudget(String budgetString) {
        try {
            // Attempt to parse the string to double
            return Double.parseDouble(budgetString.replaceAll("[^\\d.]", "")); // Remove non-numeric characters
        } catch (NumberFormatException e) {
            e.getMessage();
            return 0.0;
        }
    }

    // Method to select the best participants based on their soft skills and task performance
    private List<users> selectBestParticipants(List<users> technicians, String missiondescription, Integer participantsnumber) {
        // Sort the technicians by task completion rate and skill match score, then select the top participantsnumber
        return technicians.stream()
                .sorted(Comparator.comparingDouble((users technician) -> calculateSkillMatchScore(technician, missiondescription))
                        .thenComparingDouble(this::calculateTaskCompletionRate).reversed())
                .limit(participantsnumber)
                .collect(Collectors.toList());
    }

    // Calculate task completion rate for a user
    private double calculateTaskCompletionRate(users technician) {
        Set<Task> tasks = technician.getTasks();
        double completedTasks = 0;
        double totalTasks = tasks.size();

        for (Task task : tasks) {
            if (task.getStatus() == MissionStatusEnum.COMPLETED) {
                completedTasks++;
            }
        }

        return totalTasks > 0 ? (completedTasks / totalTasks) * 100 : 0.0;
    }

    // Calculate how well a technician's skills match the mission description
    private double calculateSkillMatchScore(users technician, String missiondescription) {
        Set<SoftSkillsEnum> technicianSkills = technician.getSoftskills();
        double matchScore = 0;

        // Check for keywords in the mission description that match technician's skills
        for (SoftSkillsEnum skill : technicianSkills) {
            if (missiondescription.toLowerCase().contains(skill.name().toLowerCase())) {
                matchScore += 1;
            }
        }

        // Return score as a percentage
        return technicianSkills.size() > 0 ? matchScore / technicianSkills.size() : 0;
    }

    // Generate a human-readable explanation of the predicted budget and selected participants
    private String generateHumanReadableExplanation(double predictedBudget, List<users> selectedParticipants) {
        StringBuilder explanation = new StringBuilder();

        // Add budget explanation
        explanation.append("The predicted budget for this mission is approximately $")
                .append(String.format("%.2f", predictedBudget))
                .append(". This prediction is based on past missions and the mission description provided.\n");

        // Add participant explanation
        explanation.append("Based on their skills and past task performance, the following technicians have been selected:\n");
        for (users technician : selectedParticipants) {
            explanation.append("- ")
                    .append(technician.getUsername())
                    .append(" (")
                    .append(technician.getSoftskills())
                    .append(")\n");
        }

        return explanation.toString();
    }

}
