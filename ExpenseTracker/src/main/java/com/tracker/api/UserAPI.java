package com.tracker.api;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tracker.domain.project.Project;
import com.tracker.domain.receipt.Item;
import com.tracker.domain.receipt.Receipt;
import com.tracker.domain.users.Role;
import com.tracker.domain.users.User;
import com.tracker.services.ProjectsService;
import com.tracker.services.ReceiptsService;


@Controller
@RequestMapping("/user")
@Secured("ROLE_USER")
public class UserAPI {

	@Autowired
	private ReceiptsService receiptService;

	@Autowired
	private ProjectsService projectService;
	
	@RequestMapping( value="", method=RequestMethod.GET )
	public String getAdminHome(	@AuthenticationPrincipal User user, Model model ) {
		System.out.println("Returning user's home page");
		return "redirect:#/user";
	}
	
	/* User browser all his receipts */
	@RequestMapping( value="/{uid}/receipt", method=RequestMethod.GET )
	@ResponseBody
	public Page<Receipt> getReceipts(	@AuthenticationPrincipal User user ,
										@PathVariable String uid,
										@RequestParam( required=false, defaultValue="" ) String place,
										@RequestParam( required=false, defaultValue="" ) String project,
										@RequestParam( required=false, defaultValue="" ) String total,
										@RequestParam( required=false, defaultValue="" ) String category,
										@RequestParam( required=false, defaultValue="0" ) String page,
										@RequestParam( required=false, defaultValue="10" ) String size,
										HttpServletResponse response ) throws IOException {
		if( !uid.equals( user.getId())) {
			response.sendError(403,"You are not allowed to browse other user's data!");
		}
		Pageable pageable = new PageRequest(  Integer.valueOf( page ), Integer.valueOf( size ) );
		
		if( place.length() > 0 || project.length() > 0 || total.length() > 0 || category.length() > 0 ) {
			System.out.println("Searching receipt");
			
			Page<Receipt> result = receiptService.searchReceipt( uid, place, project, total , category, pageable);
			return result;
		}
		
		Page< Receipt > result = receiptService.findByOwnerId(user.getId(), pageable);
		return result;
	}
	
	/* User create a receipt */
	@RequestMapping( value="/{uid}/receipt", method=RequestMethod.POST )
	@ResponseBody
	public Receipt createReceipts( 	@AuthenticationPrincipal User user ,
									@PathVariable String uid, Model model,
									@RequestBody MultiValueMap<String, String> data,
									HttpServletResponse response ) throws ParseException, IOException {
		if( !uid.equals( user.getId())) {
			response.sendError(403,"You are not allowed to browse other user's data!");
		}
		
		Receipt receipt = new Receipt();
		receipt.setOwnerId( user.getId() );
		
		/* Check if the project exists, and assign project id to receipt. */
		String project = data.getFirst("project");
		Project p = projectService.findByOwnerIdAndName( uid, project );
		if( p!= null ) {
			receipt.setProjectId( p.getId() );
		} else {
			p = new Project();
			p.setName( project );
			p.setOwnerId( uid );
			projectService.save( p );
		}
		
		/* Add note to receipt. */
		if( data.containsKey("note")) {
			receipt.setNote( data.get("note").get(0));
		}

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
		Date date =  sdf.parse( data.getFirst("time"));
		receipt.setTime( date );
		
		String[] category = new String[ data.get("category[]").size() ];
		for( int i=0; i < category.length; i++ ) {
			category[i] = data.get("category[]").get(i);
		}
		receipt.setCategory( category );

		List<Item> list_of_items = new ArrayList<Item>();	
		Item item = new Item();
		if( data.containsKey("items") ) {
			int numberOfItems = data.get("items").size();
			for( int i=0; i< numberOfItems; i++ ) {
				item.setName( data.get("items["+i+"]").get(0) );
				item.setQuantity( Integer.valueOf(data.get("items["+i+"]").get(1) ) );
				item.setPrice( Float.valueOf( data.get("items["+i+"]").get(2) ));
				list_of_items.add(item);
			}
			receipt.setList_of_items(list_of_items);
		}
		
		receipt.setPlace( data.getFirst("place") );
		receipt.setTotal( Float.valueOf( data.get("total").get(0) ) );
		
		receiptService.save( receipt );
		return receipt;
	}
	
	/* User edit a receipt */
	@RequestMapping( value="/{uid}/receipt/{rid}", method=RequestMethod.POST )
	@ResponseBody
	public Receipt updateReceipts( 	@AuthenticationPrincipal User user ,
									@PathVariable String uid, 
									@PathVariable String rid, Model model,
									@RequestBody MultiValueMap<String, String> data,
									HttpServletResponse response ) throws ParseException, IOException {
		if( !uid.equals( user.getId())) {
			response.sendError(403,"You are not allowed to browse other user's data!");
		}
		
		Receipt receipt = receiptService.findOne( rid );
		
		/* Change project*/
		String project = data.getFirst("project");
		Project p = projectService.findByOwnerIdAndName( uid, project );
		if( !p.getId().equals( receipt.getProjectId() ) ) {
			receipt.setProjectId( p.getId() );
		} 
		
		/* Add note to receipt. */
		if( data.containsKey("note")) {
			receipt.setNote( data.get("note").get(0));
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
		Date date =  sdf.parse( data.getFirst("time"));
		receipt.setTime( date );
		
		String[] category = new String[ data.get("category[]").size() ];
		for( int i=0; i < category.length; i++ ) {
			category[i] = data.get("category[]").get(i);
		}
		receipt.setCategory( category );
		
		List<Item> list_of_items = new ArrayList<Item>();	
		Item item = new Item();
		if( data.containsKey("items") ) {
			int numberOfItems = data.get("items").size();
			for( int i=0; i< numberOfItems; i++ ) {
				item.setName( data.get("items["+i+"]").get(0) );
				item.setQuantity( Integer.valueOf(data.get("items["+i+"]").get(1) ) );
				item.setPrice( Float.valueOf( data.get("items["+i+"]").get(2) ));
				list_of_items.add(item);
			}
			receipt.setList_of_items(list_of_items);
		}
		
		receipt.setPlace( data.getFirst("place") );
		receipt.setTotal( Float.valueOf( data.get("total").get(0) ) );
		
		receiptService.update(receipt);
		
		return receipt;
	}

	@RequestMapping( value="/{uid}/project", method=RequestMethod.GET )
	@ResponseBody
	public List<Project> getProjects( 	@AuthenticationPrincipal User user ,
									@PathVariable String uid, Model model,
									@RequestBody MultiValueMap<String, String> data,
									HttpServletResponse response ) throws ParseException, IOException {
		if( !uid.equals( user.getId())) {
			response.sendError(403,"You are not allowed to browse other user's data!");
		}
		
		List<Project> result = projectService.findByOwnerId( uid );
		
		return result;
	}
}
