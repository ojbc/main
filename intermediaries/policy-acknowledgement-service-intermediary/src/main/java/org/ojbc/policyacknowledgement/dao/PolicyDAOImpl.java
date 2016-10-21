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
package org.ojbc.policyacknowledgement.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("policyDAO")
public class PolicyDAOImpl implements PolicyDAO {
	
    private final Log log = LogFactory.getLog(this.getClass());
    
    @Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
	private JdbcTemplate jdbcTemplate; 
	private SimpleJdbcInsert insertUser;
	private static final String EMPTY_FED_ID_ERROR_MESSAGE="FederationId can not be empty"; 
	private static final String NON_EXISTING_FED_ID_ERROR_MESSAGE="FederationId must exist"; 
	private static final String INVALID_ORI_ERROR_MESSAGE="ORI empty or not found in database"; 
	private static final String ATTRIBUTES_MISSING_OR_INVALID="Login Error:  One or more required user attributes are missing or not valid";
	private static final String PRIVACY_COMPLIANCE_ERROR="Privacy Compliance Error:  No Agency Privacy Policy associated with this user's ORI";
	
    @Value("#{'${policyAcknowledgement.orisWithoutPrivacyPolicy:}'.split(',')}")
    private List<String> orisWithoutPrivacyPolicy;
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.insertUser = new SimpleJdbcInsert(dataSource).withTableName("ojbc_user").usingGeneratedKeyColumns("id");
    }
    
	private final String OUTSTANDING_POLICY_SELECT_BY_FED_ID = "SELECT p.*, o.* FROM policy p "
	        + "LEFT OUTER JOIN policy_ori po ON p.id = po.policy_id "
	        + "LEFT OUTER JOIN ori o ON o.id = po.ori_id "
	        + "LEFT JOIN user_policy_acknowledgement  up ON p.active=true AND up.policy_id = p.id AND up.user_id= :userId "
	        + "WHERE (up.acknowledge_date IS NULL OR up.acknowledge_date < p.update_date) "
	        + "AND (o.ori is null OR o.ori = :ori)"; 
	@Override
	public List<Policy> getOutstandingPoliciesForUser(String federationId, String ori) {
	    
		validateFedId(federationId);
		validateOri(ori);
		validateOriPolicyCompliance(ori); 
		
		Long userId = getUserIdByFedId(federationId); 
		if (userId == null){
		    userId = saveNewUser(federationId);
		}
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userId", userId); 
		paramMap.put("ori", ori); 
		
		List<Policy> outstandingPolicies = namedParameterJdbcTemplate.query(OUTSTANDING_POLICY_SELECT_BY_FED_ID, paramMap , new PolicyRowMapper());
		return outstandingPolicies;
	}

    private void validateOri(String ori) {
        if (!isValidOri(ori)) {
            log.error(INVALID_ORI_ERROR_MESSAGE + ":" + ori);
            throw new IllegalArgumentException(ATTRIBUTES_MISSING_OR_INVALID); 
		}
    }
    
    /**
     * Make sure there is a privacy policy associated with the ORI. 
     */
    private final String POLICY_COUNT_BY_ORI = "SELECT count(*) > 0 FROM policy p "
            + "LEFT JOIN policy_ori po ON po.policy_id = p.id "
            + "LEFT JOIN ori o ON o.id = po.ori_id "
            + "WHERE o.ori = ? AND p.active = true "; 
    private void validateOriPolicyCompliance(String ori) {
        
    	if (isCivilOri(ori)) 
    		return; 
    	
        Boolean oriCompliance = jdbcTemplate.queryForObject(POLICY_COUNT_BY_ORI, Boolean.class, ori);
        if (!oriCompliance) {
            log.error(PRIVACY_COMPLIANCE_ERROR + " :" + ori);
            throw new IllegalArgumentException(PRIVACY_COMPLIANCE_ERROR + ": " + ori); 
        }
    }

    private void validateFedId(String federationId) {
        if (StringUtils.isBlank(federationId)){
			log.error(EMPTY_FED_ID_ERROR_MESSAGE);
			throw new IllegalArgumentException(ATTRIBUTES_MISSING_OR_INVALID); 
		}
    }

	private final String ACKNOWLEGE_UPDATED_POLICIES = "UPDATE user_policy_acknowledgement up "
	        + "SET up.acknowledge_date = CURRENT_TIMESTAMP() "
	        + "WHERE user_id = ? "
	        + "    AND up.acknowledge_date < (SELECT update_date FROM policy WHERE id = up.policy_id)"; 
	
	@Override
	@Transactional
	public void acknowledgeOutstandingPolicies(String federationId, String ori) {
	    validateFedId(federationId);
	    
	    Long userId = getUserIdByFedId(federationId);  
	    if (userId == null){
	        log.error(NON_EXISTING_FED_ID_ERROR_MESSAGE);
	        throw new IllegalArgumentException(ATTRIBUTES_MISSING_OR_INVALID); 
	        
	    }
	    
	    jdbcTemplate.update(ACKNOWLEGE_UPDATED_POLICIES, userId);
	    List<Policy> newPolicies = this.getOutstandingPoliciesForUser(federationId, ori); 
	    
	    if (!newPolicies.isEmpty())
	    {
	        insertUserPolicyAcknowledgement(newPolicies, userId); 
	    }

	}
	
	@Transactional
	public void insertUserPolicyAcknowledgement(final List<Policy> policies, final Long userId) {
	     
        jdbcTemplate.batchUpdate("INSERT INTO user_policy_acknowledgement " +
            "(user_id, policy_id, acknowledge_date) VALUES (?, ?, CURRENT_TIMESTAMP())", new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i)
                throws SQLException {
                Policy policy = policies.get(i);
                ps.setLong(1, userId);
                ps.setLong(2, policy.getId());
            }
	            
            public int getBatchSize() {
                return policies.size();
            }
        });

    }
	 
	private static final class PolicyRowMapper implements RowMapper<Policy> {
		public Policy mapRow(ResultSet rs, int rowNum)
				throws SQLException {
	    Policy policy = new Policy();
	
	    policy.setId(rs.getLong("id"));
	    policy.setPolicyUri(rs.getString("policy_uri"));
	    policy.setPolicyLocation(rs.getString("policy_location"));
	    policy.setUpdateDate(rs.getTimestamp("update_date"));
	    policy.setActive(rs.getBoolean("active")); 
	
	    return policy;
		}
	}

	private final String USER_COUNT_BY_FED_ID = "SELECT count(*)=1 FROM ojbc_user WHERE federation_id = :federationId"; 
    @Override
    public boolean isExistingUser(String federationId) {
        validateFedId(federationId);
        
        Boolean existing = jdbcTemplate.queryForObject(USER_COUNT_BY_FED_ID, Boolean.class, federationId);
        return existing;
    }
    
    public boolean isCivilOri(String ori) {
    	final String sql = "select count(*)=1 from ori t where t.ori =? and civil_ori_indicator = true"; 
    	
    	Boolean isCivilOri = jdbcTemplate.queryForObject(sql, Boolean.class, ori);
    	return isCivilOri;
    }
    
    private final String GET_USER_ID_BY_FED_ID = "SELECT id FROM ojbc_user WHERE federation_id = ?"; 
    private Long getUserIdByFedId(String federationId) {
        validateFedId(federationId);
        
        List<Long> userIds = jdbcTemplate.queryForList(GET_USER_ID_BY_FED_ID, Long.class, federationId);
        return DataAccessUtils.singleResult(userIds);
    }
    
    private Long saveNewUser(String federationId){
        Map<String, Object> params = new HashMap<String, Object>(1);
        params.put("federation_id", federationId);
        
        final Number key = this.insertUser.executeAndReturnKey(params);
        return key.longValue(); 
    }
    
    private final String ORI_COUNT_BY_ORI_LIST = "SELECT count(*)>0 FROM ori t WHERE t.ori = ?; "; 
    private boolean isValidOri(String ori){
        
        if (StringUtils.isBlank(ori)) return false; 
        
        Boolean valid = jdbcTemplate.queryForObject(ORI_COUNT_BY_ORI_LIST, Boolean.class, ori);
        return valid; 
    }
}
