package fr.miage.sid.forum.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

import fr.miage.sid.forum.domain.Project;
import fr.miage.sid.forum.domain.ProjectRepository;
import fr.miage.sid.forum.service.ProjectService;
import fr.miage.sid.forum.service.ProjectServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ProjectServiceTests {
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Before
    public void setUp() {
        initMocks(this);
        this.projectService = new ProjectServiceImpl(projectRepository);
    }

    @Test
    public void createProjectWithSuccess() {
        Project project = new Project().setId(123L);
        given(this.projectRepository.getOne(123L)).willReturn(project);

        Project actual = this.projectService.getOne(123L);
        assertThat(actual).isEqualTo(project);
    }

    @Test
    public void inexistantProjectIsNull() {
        assertThat(this.projectService.getOne(3L)).isEqualTo(null);
    }

    @Test
    public void getAllIsImpossibleWithoutAccess() {
        Project projectA = new Project().setId(1L);
        Project projectB = new Project().setId(2L);
        Project projectC = new Project().setId(2L);

        this.projectRepository.save(projectA);
        this.projectRepository.save(projectB);
        this.projectRepository.save(projectC);

        given(this.projectRepository.getOne(1L)).willReturn(projectA);
        given(this.projectRepository.getOne(2L)).willReturn(projectB);

        assertThat(this.projectService.getAllAllowed().size()).isEqualTo(0);
        assertThat(this.projectService.getOne(1L)).isEqualTo(projectA);
        assertThat(this.projectService.getOne(2L)).isEqualTo(projectB);
        assertThat(this.projectService.getOne(2L)).isEqualTo(projectC);
    }


}
