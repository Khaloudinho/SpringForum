/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.sid.forum.domain;

import fr.miage.sid.forum.repository.ProjectRepository;
import fr.miage.sid.forum.repository.TopicRepository;
import fr.miage.sid.forum.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author alex
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTestSetDroit {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TopicRepository topicRepo;
    @Autowired
    private ProjectRepository projectRepo;
    private User user,userTest; 
    private Project project;
    private Topic topic;
    
    @Before
    public void create() {
      User newUser = new User();
      newUser.setUsername("test");
      newUser.setPassword("test");
      newUser.setEmail("test@test.com");
      user = userRepo.save(newUser);
      Project p = new Project();
      p.setName("M2-SID");
      p.setCreator(user);
      project = projectRepo.save(p);
      Topic t = new Topic();
      t.setCreator(user);
      t.setTitle("Projet Agent");
      t.setProject(project);
      topic = topicRepo.save(t);
      User newUserOfTest = new User();
      newUserOfTest.setUsername("testDroit");
      newUserOfTest.setPassword("test");
      newUserOfTest.setEmail("test@test.com");
      userTest = userRepo.save(newUserOfTest);
    }

    @Test
    public void testSetDroit(){
        
    }
}
