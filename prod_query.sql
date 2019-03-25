fetch ln_of_svc_id_no_p1 and ln_of_svc_id_no_p2

select db_userid,db_tmstamp,ln_of_svc_id_no_p1,ln_of_svc_id_no_p2 FROM ln_of_svc_cust where cust_id_no=$custIdNo and acct_no=$acctNo and bucket_no=#bucketNo and source_table='LN_OF_SVC_CUST_BA' and bill_sys_id = $billSysId limit 100

Ran Finally 


SELECT bill_sys_id,city_nme ,st_cd,zip_cd,ln_of_svc_dvc_alt_nme,ln_of_svc_id_no_p1,ln_of_svc_id_no_p2,ln_prim_id_dvc_only_list,lpido_db_tmstamp,lpido_db_userid FROM ln_of_svc WHERE ln_of_svc_id_no_p2 = %lnPrimIdDvcGeneric.lnOfSvcIdNoP2 AND ln_of_svc_id_no_p1 = %lnPrimIdDvcGeneric.lnOfSvcIdNoP1 and bill_sys_id =$billSysId 