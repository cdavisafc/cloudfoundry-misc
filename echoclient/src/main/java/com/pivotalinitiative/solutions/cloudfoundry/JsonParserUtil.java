package com.pivotalinitiative.solutions.cloudfoundry;

import java.util.Iterator;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class JsonParserUtil {
	
	   private static JsonObject getCredentials(JsonElement svcENV, String serviceTypeName, String serviceInstanceName) throws ServiceParserException {
	        
	        // This method searches the services env json object looking for a service type name containing the 
	        // serviceTypeName and an instance containing serviceInstanceName.
	        
	        JsonObject jsonobj = svcENV.getAsJsonObject();
	        Iterator<Entry<String, JsonElement>> svcIterator = jsonobj.entrySet().iterator();
	        JsonElement svcType = null;
	        // look through the hashmap for an element with the name containing serviceTypeName
	        while (svcIterator.hasNext()) {
	            Entry<String, JsonElement> ent = svcIterator.next();
	            if (ent.getKey().toLowerCase().contains(serviceTypeName.toLowerCase())) {
	                svcType = ent.getValue();
	                break;
	            }
	                
	        }
	        if(svcType!=null)
	        {
	            JsonArray jsonArray = svcType.getAsJsonArray();
	            
	            for(int i=0 ; i < jsonArray.size() ; i++) {
	                jsonobj = jsonArray.get(i).getAsJsonObject();
	                String name = jsonobj.get("name").toString();
	                // does this instance name contain serviceInstanceName?
	                if(name.toLowerCase().contains(serviceInstanceName.toLowerCase()))
	                {
	                    jsonobj = jsonobj.get("credentials").getAsJsonObject();
	                    return jsonobj;
	                }
	            }
	            throw new ServiceParserException("Echo service instance '" + serviceInstanceName + "' is not bound to the application");
	        } else {
	            throw new ServiceParserException("Echo service 1.0 is not bound to the application");
	        }

	    }
	public static EchoServiceCreds getServiceInstance(String servicesInJsonFormat, String serviceInstanceName) throws ServiceParserException {
		
		JsonElement json = new JsonParser().parse(servicesInJsonFormat); 
		JsonObject jsonobj = getCredentials(json, serviceInstanceName, serviceInstanceName);
		EchoServiceCreds creds = new EchoServiceCreds(Integer.valueOf(jsonobj.get("port").toString()), removeQuotes(jsonobj.get("host").toString()));
		return creds;

	}
	
	private static String removeQuotes(String str){
		str = str.substring(1, str.length()-1);
		return str;
	}
}
