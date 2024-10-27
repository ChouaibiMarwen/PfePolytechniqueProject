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

import java.util.*;
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
// ****************************************** needed extra  skills based on the old missions descriptions history ********************

    public String analyzeAndIdentifyMissingSkills() {
        // Step 1: Retrieve all mission descriptions
        List<Mission> missions = this.missionService.findAll();

        if (missions.isEmpty()) {
            return "No missions found. Unable to analyze missing skills.";
        }

        // Step 2: Combine mission descriptions into a single prompt input
        String combinedDescriptions = missions.stream()
                .map(Mission::getDescription)
                .collect(Collectors.joining(". "));

        // Step 3: Retrieve existing skills
        List<String> existingSkills = Arrays.stream(SoftSkillsEnum.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        // Step 4: Construct a prompt for OpenAI without listing known skills
        String prompt = "Based on the following mission descriptions, analyze any missing or underrepresented soft skills "
                + "that are essential for future missions. Only list new, relevant skills that are not currently covered. "
                + "Provide explanations for the importance of each skill, as well as specific, practical advice for "
                + "identifying these skills in potential hires. Avoid listing any skills already known or common.";

        // Add the mission descriptions without directly referencing existing skills
        prompt += " Mission descriptions: " + combinedDescriptions;

        // Step 5: Call OpenAI's getPrediction method to get the missing skills analysis
        String predictionResponse = openAIService.getPrediction(prompt);

        // Step 6: Return the response as human-readable analysis and advice
        return predictionResponse.trim();
    }
    // **************************** overdue missions and how we solve this into the future ***********************
    public String analyseOverdueMissionsAndGiveAdvices() {
        List<Mission> overdueMissions = missionService.findAllByStatus(MissionStatusEnum.OVERDUE);

        int totalOverdue = overdueMissions.size();
        double averageBudget = overdueMissions.stream()
                .mapToDouble(Mission::getBudget)
                .average()
                .orElse(0.0);

        double averageParticipants = overdueMissions.stream()
                .mapToInt(mission -> mission.getParticipants().size())
                .average()
                .orElse(0);

        // Generate the prompt for OpenAI based on overdue missions' summary data
        String prompt = "There are " + totalOverdue + " overdue missions that have exceeded their execution periods." +
                " The average budget allocated to these missions is $" + String.format("%.2f", averageBudget) +
                " and the average number of participants involved per mission is approximately " +
                String.format("%.1f", averageParticipants) + "." +
                " Please provide some advice on how to execute missions within the set timelines, considering these average statistics." +
                " Provide suggestions on budget management, optimal participant numbers, and timeline adjustments for timely completion.";

        // Call OpenAI for advice
        String response = openAIService.getPrediction(prompt);

        return response;
    }
}
