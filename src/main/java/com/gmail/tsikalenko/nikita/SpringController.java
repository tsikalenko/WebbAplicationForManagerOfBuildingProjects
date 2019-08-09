package com.gmail.tsikalenko.nikita;

import com.gmail.tsikalenko.nikita.entities.Manager;
import com.gmail.tsikalenko.nikita.entities.Project;
import com.gmail.tsikalenko.nikita.entities.Task;
import com.gmail.tsikalenko.nikita.repositories.ManagerRepository;
import com.gmail.tsikalenko.nikita.repositories.ProjectRepository;
import com.gmail.tsikalenko.nikita.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class SpringController {

    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    UserService userService;

    /**
     * Login
     */
    @RequestMapping(value = "/login")
    public String login(@RequestParam(name = "error", required = false) String error,
                        @RequestParam(name = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (logout != null) {
            model.addAttribute("logout", logout);
        }
        return "login";
    }
    @PostMapping(value = "/register")
    public String register(@RequestParam(name = "username", required = false) String username,
                        @RequestParam(name = "password", required = false) String password,
                        @RequestParam(name = "confirmPassword", required = false) String confirmPassword,
                        Model model) {
        if (managerRepository.findByUsername(username).isPresent()) {
            model.addAttribute("username", true);
            return "registration";
        } else if (!password.equals(confirmPassword)) {
            model.addAttribute("password", true);
            return "registration";
        } else {
            Manager manager = Manager.builder().username(username).password(new BCryptPasswordEncoder().encode(password)).build();
            managerRepository.save(manager);
            return "/login";
        }
    }

    @RequestMapping(value = "/registration")
    public String registration() {
        return "registration";
    }

    /**
     * Manager
     */
    @RequestMapping(value = "/")
    public String index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Manager manager = (Manager) authentication.getPrincipal();
        model.addAttribute("username", manager.getUsername());
        if (projectRepository.findAllByManager(manager).isEmpty()) {
            return "manager";
        } else {
            model.addAttribute("projects", projectRepository.findAllByManager(manager));
            return "manager";
        }
    }

    /**
     * Project
     */
    @GetMapping(value = "/project")
    public String project(@RequestParam(name = "id", required = false) Long id, Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Manager manager = (Manager) authentication.getPrincipal();
        model.addAttribute("username", manager.getUsername());
        if(id == null){
            return "manager";
        } else {
            Project project = projectRepository.findById(id).get();
            List<Task> listTask = taskRepository.findAllByProject(project);
            model.addAttribute("tasks", listTask);
            long sum = 0;
            for (Task task: listTask) {
                sum+= task.getCosts();
            }
            model.addAttribute("fullCost", sum);
            return "project";
        }
    }

    @GetMapping(value = "/create_project")
    public String createProjectGet(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Manager manager = (Manager) authentication.getPrincipal();
        model.addAttribute("username", manager.getUsername());
        return "createProject";
    }

    @PostMapping(value = "/create_project_form")
    public String createProjectPost(@RequestParam(name = "name") String name,
                                    @RequestParam(name = "description") String description,
                                    @RequestParam(name = "status") String status,
                                    @RequestParam(name = "deadline") String deadline,
                                    Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Manager manager = (Manager) authentication.getPrincipal();
        model.addAttribute("username", manager.getUsername());
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = sdf.format(date);
        Project project = Project.builder().name(name).description(description).manager(manager).status(status).startDate(sDate).deadline(deadline).build();
        projectRepository.save(project);
        return "manager";
    }

    /**
     * Tasks
     */
    @GetMapping(value = "/task")
    public String getTask(@RequestParam(name = "id", required = false) Long id,
            Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Manager manager = (Manager) authentication.getPrincipal();
        model.addAttribute("username", manager.getUsername());
        if(id == null){
            return "manager";
        } else {
            Task task = taskRepository.findById(id).get();
            model.addAttribute("name", task.getName());
            model.addAttribute("project", task.getProject().getId());
            model.addAttribute("description", task.getDescription());
            model.addAttribute("status", task.getStatus());
            model.addAttribute("sd", task.getStartDate());
            model.addAttribute("deadline", task.getDeadline());
            model.addAttribute("priority", task.getPriority());
            model.addAttribute("material", task.getMaterials());
            model.addAttribute("performer", task.getPerformer());
            model.addAttribute("cost", task.getCosts());
            return "task";
        }
    }

    @GetMapping(value = "/create_task")
    public String createTaskGet(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Manager manager = (Manager) authentication.getPrincipal();
        model.addAttribute("username", manager.getUsername());
        model.addAttribute("projects", projectRepository.findAllByManager(manager));
        return "createTask";
    }

    @PostMapping(value = "/create_task_form")
    public String createTaskPost(@RequestParam(name = "name") String name,
                                    @RequestParam(name = "project") String nameProject,
                                    @RequestParam(name = "description") String description,
                                    @RequestParam(name = "status") String status,
                                    @RequestParam(name = "deadline") String deadline,
                                    @RequestParam(name = "priority") String priority,
                                    @RequestParam(name = "materials") String materials,
                                    @RequestParam(name = "performer") String performer,
                                    @RequestParam(name = "costs") Long costs,
                                    Model model){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = simpleDateFormat.format(date);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Manager manager = (Manager)authentication.getPrincipal();
        model.addAttribute("username", manager.getUsername());
        Project project = projectRepository.findByNameAndManager(nameProject,manager).get();
        Task task = Task.builder().name(name).project(project).description(description).status(status).startDate(sDate).deadline(deadline).priority(priority).materials(materials).performer(performer).costs(costs).build();
        taskRepository.save(task);
        return "redirect:/project?id=" + project.getId();
    }
}
