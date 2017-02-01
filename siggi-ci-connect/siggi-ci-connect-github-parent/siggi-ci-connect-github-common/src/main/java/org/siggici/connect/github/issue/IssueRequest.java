/**
 * Copyright (C) 2016 Joerg Bellmann (joerg.bellmann@googlemail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.siggici.connect.github.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * @author jbellmann
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueRequest {

	private final String title;

	private String body;

	private String assignee;

	private Integer milestone;

	private String[] labels = new String[0];

	/**
	 * @param title
	 *            is mandatory
	 */
	public IssueRequest(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Integer getMilestone() {
		return milestone;
	}

	public void setMilestone(Integer milestone) {
		this.milestone = milestone;
	}

	public String[] getLabels() {
		return labels;
	}

	public void setLabels(String[] labels) {
		this.labels = labels;
	}

}
