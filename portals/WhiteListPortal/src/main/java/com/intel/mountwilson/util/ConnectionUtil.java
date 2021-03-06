/*
 * Copyright (c) 2013, Intel Corporation. 
 * All rights reserved.
 * 
 * The contents of this file are released under the BSD license, you may not use this file except in compliance with the License.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of Intel Corporation nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * This class is used to handle Exceptions, also return specific message based on the exception type. 
 */
package com.intel.mountwilson.util;

import java.io.IOException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.intel.mountwilson.common.WLMPortalException;
import com.intel.mtwilson.ApiException;
import com.sun.jersey.api.client.ClientHandlerException;

/**
 * @author yuvrajsX
 *
 */
public class ConnectionUtil {
	
	// variable used for logging. 
	//private static final Logger logger = Logger.getLogger(ConnectionUtil.class.getName()); 
        private static final Logger log = LoggerFactory.getLogger(ConnectionUtil.class.getName());
	
	// method to take a common Exception and return specific error based on Exception type.
	public static WLMPortalException handleException(Exception exceptionObject) throws WLMPortalException{
		exceptionObject.printStackTrace();
		if(exceptionObject.getClass().equals(ClientHandlerException.class)){
			return new WLMPortalException("Could not able to Connect to Server. Error Connection Refused.",exceptionObject);
		}
		if (exceptionObject.getClass().equals(JsonParseException.class)) {
			log.error("Error While Parsing Data. "+exceptionObject.getMessage());
			return new WLMPortalException("Error While parsing Data Using Jackson.",exceptionObject);
		}
		if (exceptionObject.getClass().equals(JsonMappingException.class)) {
			log.error("Error While Mapping Data. "+exceptionObject.getMessage());
			return new WLMPortalException("Error While Mapping Data Using Jackson.",exceptionObject);
		}
		if (exceptionObject.getClass().equals(IOException.class)) {
			return new WLMPortalException("IOEception."+exceptionObject.getMessage(),exceptionObject);
		}
		if (exceptionObject.getClass().equals(ApiException.class)) {
			/* Soni_Begin_17/09/2012_issue_for_consistent_Error_Message  */
			ApiException ae=(ApiException) exceptionObject;
                        // Added the error code to the display of the message
                        return new WLMPortalException(ae.getMessage() + "[" + ae.getErrorCode() + "]");
                        /* Soni_End_17/09/2012_issue_for_consistent_Error_Message  */
			//return new WLMPortalException("ApiException."+exceptionObject.getMessage(),exceptionObject);
		}
		if (exceptionObject.getClass().equals(IllegalArgumentException.class)) {
			return new WLMPortalException("IllegalArgumentException: "+exceptionObject.getMessage(),exceptionObject);
		}
		
		return new WLMPortalException("Error Cause, "+exceptionObject.getMessage(),exceptionObject);
	}
}
