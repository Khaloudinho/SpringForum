/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.miage.sid.forum.domain;

import fr.miage.sid.forum.exception.PermissionPostException;
import fr.miage.sid.forum.exception.PermissionTopicException;
import fr.miage.sid.forum.repository.ProjectRepository;
import fr.miage.sid.forum.repository.TopicRepository;
import fr.miage.sid.forum.repository.UserRepository;
import org.junit.Assert;
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
    private User user,userTest,userTestAll; 
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
      
      User newUserOfTestAll = new User();
      newUserOfTest.setUsername("testDroitRW");
      newUserOfTest.setPassword("test");
      newUserOfTest.setEmail("test@test.com");
      userTestAll = userRepo.save(newUserOfTestAll);
    }

    @Test
    public void testSetDroit(){
        project.addDroit(user, EDroit.WRITE);
        project.addDroit(userTest ,EDroit.READ);
        project.addDroit(userTestAll ,EDroit.ALL);

        Assert.assertTrue(project.canExecute(userTest,EDroit.READ));
        Assert.assertTrue(project.canExecute(user,EDroit.WRITE));
        Assert.assertTrue(project.canExecute(userTestAll,EDroit.READ));
        Assert.assertTrue(project.canExecute(userTestAll,EDroit.ALL));
        Assert.assertTrue(project.canExecute(userTestAll,EDroit.WRITE));
        
        Assert.assertFalse(project.canExecute(userTest,EDroit.WRITE));
        Assert.assertFalse(project.canExecute(user,EDroit.READ));
        
        project.removeDroit(user, EDroit.WRITE);
        Assert.assertFalse(project.canExecute(user,EDroit.WRITE));
    }
    
     @Test
    public void testSetDroitTopic(){
        topic.addDroit(user, EDroit.WRITE);
        topic.addDroit(userTest ,EDroit.READ);
        topic.addDroit(userTestAll ,EDroit.ALL);

        Assert.assertTrue(topic.canExecute(userTest,EDroit.READ));
        Assert.assertTrue(topic.canExecute(user,EDroit.WRITE));
        Assert.assertTrue(topic.canExecute(userTestAll,EDroit.READ));
        Assert.assertTrue(topic.canExecute(userTestAll,EDroit.ALL));
        Assert.assertTrue(topic.canExecute(userTestAll,EDroit.WRITE));
        
        Assert.assertFalse(topic.canExecute(userTest,EDroit.WRITE));
        Assert.assertFalse(topic.canExecute(user,EDroit.READ));
        
        topic.removeDroit(user, EDroit.WRITE);
        Assert.assertFalse(topic.canExecute(user,EDroit.WRITE));

    }
    
    @Test(expected=PermissionTopicException.class)
    public void testCreateTopicWithNoPermission() throws Exception {
        project.addDroit(user, EDroit.READ);
        topic.setCreator(user);
        project.addTopic(topic);
    }
    
    @Test(expected=PermissionPostException.class)
    public void testCreatePostWithNoPermission() throws Exception {
        topic.addDroit(user, EDroit.READ);
        Post tmp = new  Post();
        tmp.setUser(user);
        topic.addPost(tmp);      
    }
    
    @Test(expected=Test.None.class)
    public void testCreateTopicWithPermission() throws Exception {
        project.addDroit(user, EDroit.WRITE);
        topic.setCreator(user);
        project.addTopic(topic);
    }
    
    @Test(expected=Test.None.class)
    public void testCreatePostWithPermission() throws Exception {
        topic.addDroit(user, EDroit.WRITE);
        Post tmp = new  Post();
        tmp.setUser(user);
        topic.addPost(tmp);      
    }
}
