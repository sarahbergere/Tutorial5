package experiment.tutorial5;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.bean.ManagedBean;
import jakarta.faces.bean.ViewScoped;
import jakarta.faces.context.FacesContext;

import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class ProjectBean {
    private Project project;
    private static ProjectLookupService lookupService = new ProjectSimpleMap(); //To get all of the project : useful for the index page
    private String projectId;

    @PostConstruct
    public void init() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        this.projectId = params.get("p"); //getting the id of the project depending on the link

        if (projectId != null) {
            project = lookupService.findProject(projectId);
        } else {
            project = lookupService.findProject("Project 1"); // in case there is an issue with the parameter the default project is the first one
        }
    }

    public List<Project> getAllProjects() {
        return lookupService.getAllProjects();
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project currentProject) {
        this.project = currentProject;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public boolean validateProject() {   //do some back-end verification
        return project != null && project.getId() != null
                && project.getTitle() != null && !project.getTitle().isEmpty()
                && project.getDescription() != null && !project.getDescription().isEmpty()
                && project.getImage() != null && !project.getImage().isEmpty()
                && project.getTechnologies() != null && !project.getTechnologies().isEmpty();
    }

    public String saveProject() {
        if (validateProject()) {
            lookupService.saveProject(project);
            return "project?faces-redirect=true&p=" + projectId;  //redirecting to the page where the new informations will be displayed
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Please fill the required fields."));  //there is an issue so we stay on the edit page
            return "editProject?faces-redirect=true&p=" + projectId;
        }
    }
}
