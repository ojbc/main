/*
 * Copyright 2016 SEARCH-The National Consortium for Justice Information and Statistics
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ojbc.web.portal;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("portal")
public class AppProperties {

	private Map<String, String> dispoCodeMapping = new HashMap<>();

	public AppProperties() {
		super();
		dispoCodeMapping.put("301", "Acquitted");
		dispoCodeMapping.put("305", "Charge Dismissed");
		dispoCodeMapping.put("310", "Found Guilty");
		dispoCodeMapping.put("326", "Municipal Prosecutor Declines to File");
		dispoCodeMapping.put("356", "Pled Nolo Contendere");
		dispoCodeMapping.put("357", "Guilty Plea");
		dispoCodeMapping.put("376", "Pled Guilty of Other Charge");
		dispoCodeMapping.put("377", "Pled Not Guilty, Case Dismissed");
		dispoCodeMapping.put("520", "Pled Nolo Contendere to Different Charge");
		dispoCodeMapping.put("331", "Bond Forfeiture");
		dispoCodeMapping.put("538", "Failure to Appear and/or Pay");
		dispoCodeMapping.put("348", "Found Guilty of Other Charge");
		dispoCodeMapping.put("380", "Deferred Judgement accelerated");
		dispoCodeMapping.put("388", "Sentence Revoked");
		dispoCodeMapping.put("398", "Charges Filed");
	}

	public Map<String, String> getDispoCodeMapping() {
		return dispoCodeMapping;
	}

	public void setDispoCodeMapping(Map<String, String> dispoCodeMapping) {
		this.dispoCodeMapping = dispoCodeMapping;
	}
}