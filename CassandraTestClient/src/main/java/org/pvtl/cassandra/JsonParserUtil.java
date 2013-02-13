package org.pvtl.cassandra;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class JsonParserUtil {
	
	public static ServiceCreds getServiceInstance(String servicesInJsonFormat, String serviceInstanceName) throws ServiceParserException {
		
		JsonElement json = new JsonParser().parse(servicesInJsonFormat); 
		JsonObject jsonobj = json.getAsJsonObject();
		JsonElement elem = jsonobj.get("cassandra-1.0");
		if(elem!=null)
		{
			JsonArray jsonArray = elem.getAsJsonArray();
			
			for(int i=0 ; i < jsonArray.size() ; i++) {
				jsonobj = jsonArray.get(i).getAsJsonObject();
				String name = jsonobj.get("name").toString();
				name = removeQuotes(name);
				if(name.startsWith("cassandra"))                           //name.equalsIgnoreCase(serviceInstanceName))
				{
					jsonobj = jsonobj.get("credentials").getAsJsonObject();
					ServiceCreds creds = new ServiceCreds(removeQuotes(jsonobj.get("name").toString()), Integer.valueOf(jsonobj.get("port").toString()), removeQuotes(jsonobj.get("host").toString()));
					return creds;
				}
			}
			throw new ServiceParserException("Cassandra service instance '" + serviceInstanceName + "' is not bound to the application");
		} else {
			throw new ServiceParserException("Cassandra service 1.0 is not bound to the application");
		}
	}
	
	private static String removeQuotes(String str){
		str = str.substring(1, str.length()-1);
		return str;
	}
}
