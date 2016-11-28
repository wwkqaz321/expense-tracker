package com.tracker.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tracker.domain.project.Project;
import com.tracker.repositories.Projects.MongoProjectRepository;

@Service
public class ProjectsService {

	@Autowired
	private MongoProjectRepository projectRepository;
	
	
	public boolean save( Project project ) {
		projectRepository.save( project );
		return true;
	}
	
	public List<Project> findByOwnerId( String id ){
		return projectRepository.findByOwnerId(id);
	}
	public Project findByOwnerIdAndName( String id, String name ){
		Project p = projectRepository.findByOwnerIdAndName( id, name ); 
		return p;
	}
	
}
