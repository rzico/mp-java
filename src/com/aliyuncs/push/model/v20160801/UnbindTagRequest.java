/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.aliyuncs.push.model.v20160801;

import com.aliyuncs.RpcAcsRequest;

/**
 * @author auto create
 * @version 
 */
public class UnbindTagRequest extends RpcAcsRequest<UnbindTagResponse> {
	
	public UnbindTagRequest() {
		super("Push", "2016-08-01", "UnbindTag");
	}

	private String tagName;

	private String clientKey;

	private Long appKey;

	private String keyType;

	public String getTagName() {
		return this.tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
		if(tagName != null){
			putQueryParameter("TagName", tagName);
		}
	}

	public String getClientKey() {
		return this.clientKey;
	}

	public void setClientKey(String clientKey) {
		this.clientKey = clientKey;
		if(clientKey != null){
			putQueryParameter("ClientKey", clientKey);
		}
	}

	public Long getAppKey() {
		return this.appKey;
	}

	public void setAppKey(Long appKey) {
		this.appKey = appKey;
		if(appKey != null){
			putQueryParameter("AppKey", appKey.toString());
		}
	}

	public String getKeyType() {
		return this.keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
		if(keyType != null){
			putQueryParameter("KeyType", keyType);
		}
	}

	@Override
	public Class<UnbindTagResponse> getResponseClass() {
		return UnbindTagResponse.class;
	}

}
