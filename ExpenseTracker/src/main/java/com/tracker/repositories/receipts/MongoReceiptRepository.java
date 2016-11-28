package com.tracker.repositories.receipts;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.tracker.repositories.receipts.UpdateableReceiptRepository;
import com.tracker.domain.receipt.Receipt;

public interface MongoReceiptRepository  extends UpdateableReceiptRepository,
												PagingAndSortingRepository< Receipt,String > {
	
//	query.;
	public Page<Receipt> findByOwnerId( String id , Pageable pageable, Query query);
	
	public Page<Receipt> 
				findByPlaceContainingAndTotalContainingAndCategoryContaining
					( String place, String total, String category, Pageable pageable);
	public Page<Receipt> findByOwnerIdAndCategoryContaining( String id, String category, Pageable pageable );
}