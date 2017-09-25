package org.ojbc.audit.enhanced.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.FederalRapbackSubscription;
import org.ojbc.util.helper.DaoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class EnhancedAuditDAOImpl implements EnhancedAuditDAO {

    @Autowired
	private JdbcTemplate jdbcTemplate;
    
    @Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    
    private final Log log = LogFactory.getLog(this.getClass());
	
	@Override
	public Integer saveFederalRapbackSubscription(
			FederalRapbackSubscription federalRapbackSubscription) {
        log.debug("Inserting row into FEDERAL_RAPBACK_SUBSCRIPTION table : " + federalRapbackSubscription);
        
        final String FEDERAL_RAPBACK_SUBSCRIPTION_INSERT="INSERT into FEDERAL_RAPBACK_SUBSCRIPTION "
        		+ "(REQUEST_SENT_TIMESTAMP, TRANSACTION_CONTROL_REFERENCE_IDENTIFICATION, PATH_TO_REQUEST_FILE) "
        		+ "values (?, ?, ?)";
        

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
        	    new PreparedStatementCreator() {
        	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        	            PreparedStatement ps =
        	                connection.prepareStatement(FEDERAL_RAPBACK_SUBSCRIPTION_INSERT, new String[] {"FEDERAL_RAPBACK_SUBSCRIPTION_ID"});
        	            DaoUtils.setPreparedStatementVariable(federalRapbackSubscription.getRequestSentTimestamp(), ps, 1);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackSubscription.getTransactionControlReferenceIdentification(), ps, 2);
        	            DaoUtils.setPreparedStatementVariable(federalRapbackSubscription.getPathToRequestFile(), ps, 3);
        	            
        	            return ps;
        	        }
        	    },
        	    keyHolder);

         return keyHolder.getKey().intValue();	
    }

	@Override
	public void updateFederalRapbackSubscriptionWithResponse(
			FederalRapbackSubscription federalRapbackSubscription)
			throws Exception {
		
		Map<String, Object> paramMap = new HashMap<String, Object>(); 
		
		final String FEDERAL_SUBSCRIPTION_UPDATE="UPDATE FEDERAL_RAPBACK_SUBSCRIPTION SET "
				+ "TRANSACTION_CATEGORY_CODE = :transactionCategoryCode, "
				+ "RESPONSE_RECIEVED_TIMESTAMP = :responseRecievedTimestamp, "
				+ "PATH_TO_RESPONSE_FILE = :pathToResponseFile "
				+ "WHERE FEDERAL_RAPBACK_SUBSCRIPTION_ID = :federalRapbackSubscriptionId";

		paramMap.put("transactionCategoryCode", federalRapbackSubscription.getTransactionCategoryCode()); 
		paramMap.put("responseRecievedTimestamp", convertToDatabaseColumn(federalRapbackSubscription.getResponseRecievedTimestamp())); 
		paramMap.put("pathToResponseFile", federalRapbackSubscription.getPathToResponseFile()); 
		paramMap.put("federalRapbackSubscriptionId", federalRapbackSubscription.getFederalRapbackSubscriptionId()); 
		
		namedParameterJdbcTemplate.update(FEDERAL_SUBSCRIPTION_UPDATE, paramMap);

	}

	@Override
	public FederalRapbackSubscription retrieveFederalRapbackSubscriptionFromTCN(
			String transactionControlNumber) {
		final String SUBSCRIPTION_SELECT="SELECT * FROM FEDERAL_RAPBACK_SUBSCRIPTION WHERE TRANSACTION_CONTROL_REFERENCE_IDENTIFICATION = ?";
		
		List<FederalRapbackSubscription> federalRapbackSubscriptions = jdbcTemplate.query(SUBSCRIPTION_SELECT, new FederalRapbackSubscriptionRowMapper(), transactionControlNumber);
		return DataAccessUtils.singleResult(federalRapbackSubscriptions);	
	}
	
	private final class FederalRapbackSubscriptionRowMapper implements RowMapper<FederalRapbackSubscription> {
		public FederalRapbackSubscription mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			FederalRapbackSubscription federalRapbackSubscription = buildFederalRapbackSubscriptiont(rs);
			return federalRapbackSubscription;
		}

		private FederalRapbackSubscription buildFederalRapbackSubscriptiont(
				ResultSet rs) throws SQLException{

			FederalRapbackSubscription federalRapbackSubscription = new FederalRapbackSubscription();
			
			federalRapbackSubscription.setPathToRequestFile(rs.getString("PATH_TO_REQUEST_FILE"));
			federalRapbackSubscription.setPathToResponseFile(rs.getString("PATH_TO_RESPONSE_FILE"));
			federalRapbackSubscription.setRequestSentTimestamp(toLocalDateTime(rs.getTimestamp("REQUEST_SENT_TIMESTAMP")));
			federalRapbackSubscription.setResponseRecievedTimestamp(toLocalDateTime(rs.getTimestamp("RESPONSE_RECIEVED_TIMESTAMP")));
			federalRapbackSubscription.setTransactionCategoryCode(rs.getString("TRANSACTION_CATEGORY_CODE"));
			federalRapbackSubscription.setTransactionControlReferenceIdentification(rs.getString("TRANSACTION_CONTROL_REFERENCE_IDENTIFICATION"));
			federalRapbackSubscription.setFederalRapbackSubscriptionId(rs.getInt("FEDERAL_RAPBACK_SUBSCRIPTION_ID"));
			
			return federalRapbackSubscription;
		}
	}
	
	private LocalDateTime toLocalDateTime(Timestamp timestamp){
		return timestamp == null? null : timestamp.toLocalDateTime();
	}
	
    public Timestamp convertToDatabaseColumn(LocalDateTime locDateTime) {
    	return (locDateTime == null ? null : Timestamp.valueOf(locDateTime));
    }



}
