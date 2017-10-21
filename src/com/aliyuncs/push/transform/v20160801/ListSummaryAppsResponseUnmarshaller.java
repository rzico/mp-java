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
package com.aliyuncs.push.transform.v20160801;

import java.util.ArrayList;
import java.util.List;

import com.aliyuncs.push.model.v20160801.ListSummaryAppsResponse;
import com.aliyuncs.push.model.v20160801.ListSummaryAppsResponse.SummaryAppInfo;
import com.aliyuncs.transform.UnmarshallerContext;


public class ListSummaryAppsResponseUnmarshaller {

	public static ListSummaryAppsResponse unmarshall(ListSummaryAppsResponse listSummaryAppsResponse, UnmarshallerContext context) {
		
		listSummaryAppsResponse.setRequestId(context.stringValue("ListSummaryAppsResponse.RequestId"));

		List<SummaryAppInfo> summaryAppInfos = new ArrayList<SummaryAppInfo>();
		for (int i = 0; i < context.lengthValue("ListSummaryAppsResponse.SummaryAppInfos.Length"); i++) {
			SummaryAppInfo summaryAppInfo = new SummaryAppInfo();
			summaryAppInfo.setAppName(context.stringValue("ListSummaryAppsResponse.SummaryAppInfos["+ i +"].AppName"));
			summaryAppInfo.setAppKey(context.longValue("ListSummaryAppsResponse.SummaryAppInfos["+ i +"].AppKey"));

			summaryAppInfos.add(summaryAppInfo);
		}
		listSummaryAppsResponse.setSummaryAppInfos(summaryAppInfos);
	 
	 	return listSummaryAppsResponse;
	}
}