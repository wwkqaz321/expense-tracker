package com.tracker.repositories.Projects;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.tracker.domain.project.Project;


public interface MongoProjectRepository extends UpdateableProjectRepository ,
													Repository< Project , String > {
	
	public void save( Project project);
	public List<Project> findByOwnerId( String id );
	public Project findByOwnerIdAndName( String id, String name );
}