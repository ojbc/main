/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.connectors.warrantmod.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.helper.DaoUtils;
import org.ojbc.warrant.repository.dao.ChargeReferralMapper;
import org.ojbc.warrant.repository.dao.PersonMapper;
import org.ojbc.warrant.repository.model.ChargeReferralReport;
import org.ojbc.warrant.repository.model.Person;
import org.ojbc.warrant.repository.model.Warrant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Scope("prototype")
public class WarrantsRepositoryBaseDaoImpl implements WarrantsRepositoryBaseDAO {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(WarrantsRepositoryBaseDaoImpl.class);
	
    @Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
	private JdbcTemplate jdbcTemplate;
	
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

	
	@Override
	public ChargeReferralReport retrieveChargeReferralInfo(String lawEnforcementORI,
			String originatingAgencyComplaintNumber) {

		String sql = "SELECT p.PersonID as PersonID, cr.ChargeRefID as ChargeReferralID "
				+ "FROM ChargeRef cr, Person p "
				+ "WHERE cr.PersonID = p.PersonID "
				+ " AND cr.ReportingAgencyORI = ? and cr.CaseAgencyComplaintNumber=?";
		
		List<ChargeReferralReport> chargeReferralReports = 
				jdbcTemplate.query(sql,new ChargeReferralMapper(), lawEnforcementORI, originatingAgencyComplaintNumber);
		
		return DataAccessUtils.singleResult(chargeReferralReports);
	}


	@Override
	public ChargeReferralReport retrieveChargeReferralAndWarrantInfo(
			String lawEnforcementORI, String originatingAgencyComplaintNumber) {
		String sql = "SELECT wcra.WarrantID as WarrantID, p.PersonID as PersonID, cr.ChargeRefID as ChargeReferralID "
				+ "FROM ChargeRef cr, WarrantChargeRef wcra, Person p "
				+ "WHERE cr.PersonID = p.PersonID "
				+ " AND cr.ChargeRefID = wcra.ChargeRefID "
				+ " AND cr.ReportingAgencyORI = ? and cr.CaseAgencyComplaintNumber=?";
		
		List<ChargeReferralReport> chargeReferralReports= 
				jdbcTemplate.query(sql,new ChargeReferralMapper(), lawEnforcementORI, originatingAgencyComplaintNumber);
		
		return DataAccessUtils.singleResult(chargeReferralReports);
	}


	@Override
	public Person retrievePersonInfo(Integer personPk) {
		String sql = "SELECT * from Person where personID=?";
		
		Person person = 
				jdbcTemplate.queryForObject(sql,new PersonMapper(), personPk);
		
		return person;	
	}


	@Override
	public Warrant retrieveWarrant(Integer warrantId) {
		String sql = "SELECT w.*, wr.WarrantRemarkText from Warrant w "
				+ "LEFT JOIN WarrantRemarks wr ON wr.warrantID = w.warrantID WHERE w.warrantID = ? ";
		
		List<Warrant> warrants = jdbcTemplate.query(sql, new WarrantResultSetExtractor(), warrantId);
		return DataAccessUtils.singleResult(warrants);
	}

	private class WarrantResultSetExtractor implements ResultSetExtractor<List<Warrant>> {

		@Override
		public List<Warrant> extractData(ResultSet rs)
				throws SQLException, DataAccessException {
            Map<Integer, Warrant> map = new HashMap<Integer, Warrant>();
            Warrant warrant = null;
            while (rs.next()) {
                Integer warrantId = rs.getInt("warrantID" ); 
                warrant  = map.get( warrantId );
                if ( warrant  == null){
                	warrant = new Warrant(); 
                	
                	warrant.setStateWarrantRepositoryID(rs.getString("StateWarrantRepositoryID"));
                	warrant.setDateOfWarrantRequest( DaoUtils.getLocalDate(rs, "DateOfWarrant"));
                	warrant.setDateOfExpiration( DaoUtils.getLocalDate(rs, "DateOfExpiration"));
                	warrant.setBroadcastArea(rs.getString("BroadcastArea"));
                	warrant.setWarrantEntryType(rs.getString("WarrantEntryType"));
                	warrant.setCourtAgencyORI(rs.getString("CourtAgencyORI"));
                	warrant.setLawEnforcementORI(rs.getString("LawEnforcementORI"));
                	warrant.setCourtDocketNumber(rs.getString("CourtDocketNumber"));
                	warrant.setOcaComplaintNumber(rs.getString("OCAComplaintNumber"));
                	warrant.setOperator(rs.getString("Operator"));
                	warrant.setPaccCode(rs.getString("PACCCode"));
                	warrant.setOriginalOffenseCode(rs.getString("OriginalOffenseCode"));
                	warrant.setOffenseCode(rs.getString("OffenseCode"));
                	warrant.setGeneralOffenseCharacter(rs.getString("GeneralOffenseCharacter"));
                	warrant.setCriminalTrackingNumber(rs.getString("CriminalTrackingNumber"));
                	warrant.setExtradite(rs.getBoolean("Extradite"));
                	warrant.setExtraditionLimits(rs.getString("ExtraditionLimits"));
                	warrant.setPickupLimits(rs.getString("PickupLimits"));
                	warrant.setBondAmount(rs.getString("BondAmount"));
                	warrant.setWarrantStatus(rs.getString("WarrantStatus"));
                	warrant.setWarrantStatusTimestamp(DaoUtils.getLocalDateTime( rs, "WarrantStatusTimestamp"));
                	warrant.setWarrantModificationRequestSent( rs.getBoolean("WarrantModRequestSent") );
                	warrant.setWarrantModificationResponseReceived( rs.getBoolean("WarrantModResponseReceived") );
                	
                	List<String> warrantRemarks = new ArrayList<String>();
                	String warrantRemark = rs.getString("WarrantRemarkText");
                	
                	if (StringUtils.isNotBlank(warrantRemark)){
                		warrantRemarks.add( warrantRemark );
                	}
                	warrant.setWarrantRemarkStrings(warrantRemarks);
                	
                	map.put(warrantId, warrant); 
                }
                else{
                	String warrantRemark = rs.getString("WarrantRemarkText");
                	
                	if (StringUtils.isNotBlank(warrantRemark)){
                		warrant.getWarrantRemarkStrings().add( warrantRemark );
                	}
                }
	              
            }
            
            return (List<Warrant>) new ArrayList<Warrant>(map.values());
		}

	}

}
